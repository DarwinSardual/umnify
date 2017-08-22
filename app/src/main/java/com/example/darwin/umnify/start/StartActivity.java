package com.example.darwin.umnify.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.authentication.AuthenticationKeys;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.home.HomeActivity;
import com.example.darwin.umnify.login.LoginActivity;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

        //File newsImageDirectory = this.getDir("umnify/images/feed/news/", Context.MODE_PRIVATE);
        File newsImageDirectory = new File(directory.getAbsolutePath() + "/images/feed/news");
        if(!newsImageDirectory.exists())
            newsImageDirectory.mkdirs();

        File blogImageDirectory = new File(directory.getAbsolutePath() + "/images/feed/blog");
        if(!blogImageDirectory.exists())
            blogImageDirectory.mkdirs();

        //handle start
        databaseConnection = UMnifyDbHelper.getInstance(this);
        handleUserCheck();

    }

    private void handleUserCheck(){

        //CheckUserAsync checkUserAsync;
        SQLiteDatabase databaseConnectionRead = databaseConnection.getReadableDatabase();

        String[] projection = {

                UMnifyContract.UMnifyColumns.User.ID.toString(),
                UMnifyContract.UMnifyColumns.User.TYPE.toString(),
                UMnifyContract.UMnifyColumns.User.PASSWORD.toString()
        };

        Cursor cursor = databaseConnectionRead.query(
                UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(),
                projection, null, null, null, null, null);

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

            StartDataActionWrapper wrapper;
            WebServiceConnection connection;
            HashMap<String, String> textData = new HashMap<>();
            textData.put(AuthenticationKeys.USER_ID_KEY, id + "");
            textData.put("type", type + "");
            textData.put(AuthenticationKeys.USER_PASSWORD_KEY, password);


                wrapper = new StartDataActionWrapper(textData, this);
                WebServiceAsync async = new WebServiceAsync();
                async.execute(wrapper);

        }else{

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    private class StartDataActionWrapper implements WebServiceAction{

        private HashMap<String, String> textDataOutput;
        private WebServiceConnection connection;
        private Activity activity;

        /* Dependent data */

        private InputStream inputStream;

        public StartDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity){

            this.textDataOutput = textDataOutput;
            this.activity = activity;
        }

        @Override
        public void processRequest() {


            connection = new WebServiceConnection(AuthenticationAddress.CHECK_USER,
                    activity, true, true, true);

                connection.addAuthentication();
                DataHelper.writeTextUpload(textDataOutput, connection);
                connection.flushOutputStream();

                inputStream = connection.getInputStream();

        }

        @Override
        public void processResult() {

            Log.e("process", "result");

            try{

                Intent intent;
                String response = DataHelper.parseStringFromStream(inputStream);

                if(response == null){
                    Log.e("Response", "null");
                    return;

                }else{
                    Log.e("Response", response);
                }

                JSONObject json = new JSONObject(response);
                int code = json.getInt("code");

                if(code == AuthenticationCodes.USER_AUTHENTICATED){

                    JSONObject data = new JSONObject(json.getString("user"));

                    intent = new Intent(StartActivity.this, HomeActivity.class);
                    intent.putExtra("USER_ID", Integer.parseInt(textDataOutput.get(AuthenticationKeys.USER_ID_KEY)));
                    intent.putExtra("USER_TYPE", Integer.parseInt(textDataOutput.get("type")));
                    intent.putExtra("USER_PASSWORD", textDataOutput.get(AuthenticationKeys.USER_PASSWORD_KEY));
                    intent.putExtra("USER_FIRSTNAME", data.getString("firstname"));
                    intent.putExtra("USER_LASTNAME", data.getString("lastname"));
                    intent.putExtra("USER_IMAGE_FILE", data.getString("image"));
                    intent.putExtra("USER_EMAIL", data.getString("email"));
                    startActivity(intent);

                }else if(code == AuthenticationCodes.INVALID_USER_ID_PASSWORD){

                    SQLiteDatabase databaseConnectionWrite = StartActivity.this.databaseConnection.getWritableDatabase();
                    databaseConnectionWrite.delete(UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(), null, null);

                    String selection = UMnifyContract.UMnifyColumns.Person.ID.toString() + " = ?";
                    String[] selectionArgs = {textDataOutput.get("id")};
                    databaseConnectionWrite.delete(UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString(), selection, selectionArgs);

                    intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                finish();
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
