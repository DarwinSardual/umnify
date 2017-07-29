package com.example.darwin.umnify.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.home.HomeActivity;
import com.example.darwin.umnify.scratch.SampleConnActivity;

import com.example.darwin.umnify.authentication.AuthenticationKeys;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

class LoginActivityController {

    private LoginActivityLayout layout;
    private LoginAsync loginHandler;
    private Activity activity;

    public LoginActivityController(Activity activity, LoginActivityLayout layout){

        this.layout = layout;
        this.activity = activity;
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

        loginHandler = new LoginAsync(activity, layout, source);
        loginHandler.execute(id, password);

    }



    private class LoginAsync extends AsyncTask<String, Void, String> {

        private final String urlAddress;
        private Activity activity;
        private final int READ_TIMEOUT = 10000;
        private final int CONNECT_TIMEOUT = 15000;
        private final String REQUEST_METHOD = "POST";

        private URL url;
        private HttpURLConnection urlConnection;
        private Uri.Builder builder;

        private LoginActivityLayout layout;
        private View source;


        public LoginAsync(Activity activity, LoginActivityLayout layout, View source){

            this.activity = activity;
            this.layout = layout;
            this.source = source;

            urlAddress = "http://192.168.0.100/~darwin/UMnifyMobileScripts/login/authenticate_login.php";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                setUpConnection();

                builder = new Uri.Builder()
                        .appendQueryParameter(AuthenticationKeys.IDENTIFICATION_KEY, AuthenticationKeys.IDENTIFICATION_VALUE)
                        .appendQueryParameter(AuthenticationKeys.USERNAME_KEY, AuthenticationKeys.USERNAME_VALUE)
                        .appendQueryParameter(AuthenticationKeys.PASSWORD_KEY, AuthenticationKeys.PASSWORD_VALUE)
                        .appendQueryParameter(AuthenticationKeys.USER_ID_KEY, layout.getUsernameField().getText().toString())
                        .appendQueryParameter(AuthenticationKeys.USER_PASSWORD_KEY, layout.getPasswordField().getText().toString());
                String query = builder.build().getEncodedQuery();

                setRequest(query);
                urlConnection.connect();

                String response = getResponse();

                return response;


            } catch (Exception e) {


                return null;
            }
        }

        private void setUpConnection() throws IOException{

            url = new URL(urlAddress);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
        }

        private void setRequest(String query) throws IOException{

            OutputStream stream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(stream, "UTF-8"));

            writer.write(query);
            writer.flush();
            writer.close();
            stream.close();
        }

        private String getResponse() throws  IOException{

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return reader.readLine();

        }

        @Override
        protected void onPostExecute(String response) {
            //super.onPostExecute(response);


            try{
                JSONObject json = new JSONObject(response);
                int code = json.getInt("code");

                if(code == AuthenticationCodes.USER_AUTHENTICATED){
                    Intent intent = new Intent(activity, HomeActivity.class);
                    //intent.putExtra("SAMPLE_KEY", Integer.toString(code));
                    activity.startActivity(intent);
                }else if(code == AuthenticationCodes.INVALID_USER_ID_PASSWORD){

                    Snackbar.make(source, "Hello Snackbar!",
                            Snackbar.LENGTH_LONG).show();
                }


            }catch (Exception e){
                e.printStackTrace();
            }





        }
    }
}