package com.example.darwin.umnify.start;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.RemoteDbConn;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationKeys;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.login.LoginActivity;
import com.example.darwin.umnify.scratch.SampleConnActivity;

import java.io.IOException;

public class StartActivity extends AppCompatActivity {

    private UMnifyDbHelper databaseConnection;
    private SQLiteDatabase databaseConnectionRead;
    private SQLiteDatabase databaseConnectionWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        databaseConnection = UMnifyDbHelper.getInstance(this);
        //databaseConnectionRead = databaseConnection.getReadableDatabase();
        //databaseConnectionWrite = databaseConnection.getWritableDatabase();

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);

        //SQLiteDatabase dbWrite = databaseConnection.getWritableDatabase();


        /*ContentValues values = new ContentValues();
        values.put(UMnifyContract.UMnifyColumns.User.ID.toString(), 383221);
        values.put(UMnifyContract.UMnifyColumns.User.TYPE.toString(), 1);
        values.put(UMnifyContract.UMnifyColumns.User.PASSWORD.toString(), "darwinsardual");

        long id = dbWrite.insert(UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(), null, values);
        Log.e("ID", id +"");*/

        //Log.e("Calling", "User checker");
        //startService(new Intent(this, UserChecker.class));
        //startActivity(new Intent(this, SampleConnActivity.class));

        //finish();



    }

    private void handleUserCheck(){

        CheckUserAsync checkUserAsync;

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
            String urlAddress = AuthenticationAddress.CHECK_USER;
            checkUserAsync = new CheckUserAsync(urlAddress);
            //checkUserAsync.execute()

        }else{

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private class CheckUserAsync extends RemoteDbConn <String, Void, String>{

        private String urlAddress;

        public CheckUserAsync(String urlAddress){
            super(urlAddress);
            this.urlAddress = urlAddress;
        }

        @Override
        protected String doInBackground(String... strings) {

            try{

                setUpConnection();
                Uri.Builder queryBuilder = super.getQueryBuilder();
                queryBuilder.appendQueryParameter(AuthenticationKeys.USER_ID_KEY, strings[0])
                        .appendQueryParameter(AuthenticationKeys.USER_PASSWORD_KEY, strings[1]);

                super.setRequest(queryBuilder.build().getEncodedQuery());
                super.getUrlConnection().connect();

                String response = super.getRequest();

                return response;

            }catch (IOException e){

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
