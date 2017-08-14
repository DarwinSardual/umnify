package com.example.darwin.umnify.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.darwin.umnify.async.RemoteDbConn;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.home.HomeActivity;
import com.example.darwin.umnify.scratch.SampleConnActivity;

import com.example.darwin.umnify.authentication.AuthenticationKeys;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

class LoginActivityController {

    private LoginActivityLayout layout;
    private LoginAsync loginHandler;
    private Activity activity;
    private UMnifyDbHelper databaseConnection;

    public LoginActivityController(Activity activity, LoginActivityLayout layout){

        this.layout = layout;
        this.activity = activity;
        databaseConnection = UMnifyDbHelper.getInstance(activity);

        setUpLoginButton();
    }

    private void setUpLoginButton(){

        Button login = layout.getLoginButton();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                authenticateLogin(view);
            }
        });
    }

    private void authenticateLogin(View source){

        String id = layout.getUsernameField().getText().toString();
        String password = layout.getPasswordField().getText().toString();
        String urlAddress = AuthenticationAddress.AUTHENTICATE_LOGIN;

        loginHandler = new LoginAsync(urlAddress, source);
        loginHandler.execute(id, password);

    }

    private class LoginAsync extends RemoteDbConn<String, Void, String>{

        private View source;

        public LoginAsync(String urlAddres, View source){

            super(urlAddres, LoginActivityController.this.activity);
            this.source = source;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                super.setUpConnection();
                Uri.Builder queryBuilder = super.getQueryBuilder();
                queryBuilder.appendQueryParameter(AuthenticationKeys.USER_ID_KEY, strings[0])
                        .appendQueryParameter(AuthenticationKeys.USER_PASSWORD_KEY, strings[1]);

                super.setRequest(queryBuilder.build().getEncodedQuery());
                super.getUrlConnection().connect();

                String response = super.getRequest();

                return response;

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            try{

                JSONObject json = new JSONObject(response);
                int code = json.getInt("code");

                if(code == AuthenticationCodes.USER_AUTHENTICATED){


                    JSONObject user = new JSONObject(json.getString("user"));
                    JSONObject person = new JSONObject(json.getString("person"));

                    //store database credentials here

                    UMnifyDbHelper databaseConnection = UMnifyDbHelper.getInstance(LoginActivityController.this.activity);
                    SQLiteDatabase databaseConnectionWrite = databaseConnection.getWritableDatabase();


                    // insert person data here
                    ContentValues values = new ContentValues();
                    values.put(UMnifyContract.UMnifyColumns.Person.ID.toString(), user.getInt("id"));
                    values.put(UMnifyContract.UMnifyColumns.Person.FIRSTNAME.toString(), person.getString("firstname"));
                    values.put(UMnifyContract.UMnifyColumns.Person.MIDDLENAME.toString(), person.getString("middlename"));
                    values.put(UMnifyContract.UMnifyColumns.Person.NAME_EXT.toString(), person.getString("name_ext"));
                    values.put(UMnifyContract.UMnifyColumns.Person.BIRTHDATE.toString(), person.getString("birthdate"));
                    values.put(UMnifyContract.UMnifyColumns.Person.GENDER.toString(), person.getString("gender"));
                    values.put(UMnifyContract.UMnifyColumns.Person.ADDRESS.toString(), person.getString("address"));
                    values.put(UMnifyContract.UMnifyColumns.Person.CONTACT.toString(), person.getString("contact"));
                    values.put(UMnifyContract.UMnifyColumns.Person.IMAGE.toString(), person.getString("image"));

                    long person_id  = databaseConnectionWrite.insert(
                            UMnifyContract.UMnifyColumns.Person.TABLE_NAME.toString(),
                            null,
                            values
                    );

                    //insert user data
                    values = new ContentValues();
                    values.put(UMnifyContract.UMnifyColumns.User.ID.toString(), user.getInt("id"));
                    values.put(UMnifyContract.UMnifyColumns.User.TYPE.toString(), user.getInt("type"));
                    values.put(UMnifyContract.UMnifyColumns.User.PASSWORD.toString(), user.getString("password"));

                    long user_id = databaseConnectionWrite.insert(
                            UMnifyContract.UMnifyColumns.User.TABLE_NAME.toString(),
                            null,
                            values
                    );

                    Intent intent = new Intent(LoginActivityController.this.activity, HomeActivity.class);
                    LoginActivityController.this.activity.startActivity(intent);
                    LoginActivityController.this.activity.finish();
                }else if(code == AuthenticationCodes.INVALID_USER_ID_PASSWORD){

                    Snackbar.make(source, "User not authenticated!",
                            Snackbar.LENGTH_SHORT).show();
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}