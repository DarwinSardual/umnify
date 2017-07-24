package com.example.darwin.umnify.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import com.example.darwin.umnify.home.HomeActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class LoginActivityController {

    LoginActivityLayout layout;

    public LoginActivityController(LoginActivityLayout layout){

        this.layout = layout;

    }

    public void init(){

    }

    private void setUpLogin(){

        Button login = layout.getLoginButton();

        login.setOnClickListener((view)->{

            //authenticateLogin();

            Intent intent = new Intent(view.getContext(), HomeActivity.class);
            view.getContext().startActivity(intent);

        });

    }

    private void authenticateLogin(){

        String temp = null;
        try{
            temp = processLogin();
        }catch (AuthenticationException e){

        }catch (IOException e){

        }
    }

    private String processLogin() throws AuthenticationException, IOException{

        URL url = new URL("http://192.168.0.107/~darwin/UMnifyMobileScripts/sample.php");
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();


        return urlConn.getResponseCode() + "";
    }

    private class LoginAsync extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}