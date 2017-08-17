package com.example.darwin.umnify.start;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.RemoteDbConn;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.authentication.AuthenticationKeys;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.home.HomeActivity;
import com.example.darwin.umnify.login.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity {

    private UMnifyDbHelper databaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //initialize needed directory

        File directory = this.getDir("umnify", Context.MODE_PRIVATE);
        if(!directory.exists())
            directory.mkdirs();

        databaseConnection = UMnifyDbHelper.getInstance(this);
        handleUserCheck();

    }

    private void handleUserCheck(){

        CheckUserAsync checkUserAsync;
        SQLiteDatabase databaseConnectionRead = databaseConnection.getReadableDatabase();

        String[] projection = {

                UMnifyContract.UMnifyColumns.User.ID.toString(),
                UMnifyContract.UMnifyColumns.User.TYPE.toString(),
                UMnifyContract.UMnifyColumns.User.PASSWORD.toString()
        };

        Cursor cursor = databaseConnectionRead.query(
                UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(),
                projection,
                null,
                null,
                null,
                null,
                null);

        if(cursor.getCount() == 1){
            //check if the stored credentials is valid
            //get user credentials
            int id = 0;
            int type = 0;
            String password = null;

            while(cursor.moveToNext()){
                id = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.User.ID.toString()));
                type = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.User.TYPE.toString()));
                password = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.User.PASSWORD.toString()));
            }

            //process
            String urlAddress = AuthenticationAddress.CHECK_USER;
            checkUserAsync = new CheckUserAsync(urlAddress);
            checkUserAsync.execute(id +"", type + "",password);

        }else{

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            //Log.e("User", "does not exist");
            finish();
        }
    }

    private class CheckUserAsync extends RemoteDbConn <String, Void, HashMap<String, String>>{

        public CheckUserAsync(String urlAddress){
            super(urlAddress, StartActivity.this);
        }

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {

            try{
                setUpConnection();
                Uri.Builder queryBuilder = super.getQueryBuilder();
                queryBuilder.appendQueryParameter(AuthenticationKeys.USER_ID_KEY, strings[0])
                        .appendQueryParameter(AuthenticationKeys.USER_PASSWORD_KEY, strings[2]);

                super.setRequest(queryBuilder.build().getEncodedQuery());
                super.getUrlConnection().connect();

                // response, id, type, password
                HashMap<String, String> response = new HashMap<>();
                response.put("request", super.getRequest());
                response.put("id", strings[0]);
                response.put("type", strings[1]);
                response.put("password", strings[2]);

                return response;

            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, String> response) {
            //super.onPostExecute(s);
            Intent intent;

            try{
                JSONObject json = new JSONObject(response.get("request"));
                int code = json.getInt("code");

                if(code == AuthenticationCodes.USER_AUTHENTICATED){

                    JSONObject data = new JSONObject(json.getString("user"));

                    intent = new Intent(StartActivity.this, HomeActivity.class);
                    intent.putExtra("USER_ID", Integer.parseInt(response.get("id")));
                    intent.putExtra("USER_TYPE", Integer.parseInt(response.get("type")));
                    intent.putExtra("USER_PASSWORD", response.get("password"));
                    intent.putExtra("USER_FIRSTNAME", data.getString("firstname"));
                    intent.putExtra("USER_LASTNAME", data.getString("lastname"));
                    intent.putExtra("USER_IMAGE_FILE", data.getString("image"));
                    intent.putExtra("USER_EMAIL", data.getString("email"));
                    startActivity(intent);

                }else if(code == AuthenticationCodes.INVALID_USER_ID_PASSWORD){

                    // delete the user and person in the db first

                    SQLiteDatabase databaseConnectionWrite = StartActivity.this.databaseConnection.getWritableDatabase();
                    databaseConnectionWrite.delete(UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(), null, null);

                    String selection = UMnifyContract.UMnifyColumns.Person.ID.toString() + " = ?";
                    String[] selectionArgs = {response.get("id")};
                    databaseConnectionWrite.delete(UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString(), selection, selectionArgs);

                    // start intent
                    intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                finish();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
