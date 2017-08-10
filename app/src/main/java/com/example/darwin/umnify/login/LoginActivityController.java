package com.example.darwin.umnify.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.darwin.umnify.async.RemoteDbConn;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
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
                //Log.e("User dir", System.getProperty("user.dir"));
            }
        });
    }

    private void authenticateLogin(View source){

        String id = layout.getUsernameField().getText().toString();
        String password = layout.getPasswordField().getText().toString();
        String urlAddress = AuthenticationAddress.AUTHENTICATE_LOGIN;

        loginHandler = new LoginAsync(urlAddress, layout, source);
        loginHandler.execute(id, password);

    }

    private class LoginAsync extends RemoteDbConn<String, Void, String>{

        private final String urlAddres;
        private LoginActivityLayout layout;
        private View source;

        public LoginAsync(String urlAddres, LoginActivityLayout layout, View source){

            super(urlAddres, LoginActivityController.this.activity);
            this.urlAddres = urlAddres;
            this.layout = layout;
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

            Log.e("Response", response);

            /*try{

                JSONObject json = new JSONObject(response);
                int code = json.getInt("code");

                if(code == AuthenticationCodes.USER_AUTHENTICATED){
                    Intent intent = new Intent(LoginActivityController.this.activity, HomeActivity.class);

                    JSONObject data = new JSONObject(json.getString("data"));
                    intent.putExtra("USER_ID", data.getString("id"));
                    intent.putExtra("USER_TYPE", data.getString("type"));

                    activity.startActivity(intent);
                }else if(code == AuthenticationCodes.INVALID_USER_ID_PASSWORD){

                    Snackbar.make(source, "User not authenticated!",
                            Snackbar.LENGTH_SHORT).show();
                }

            }catch (JSONException e){

            }*/
        }
    }
}