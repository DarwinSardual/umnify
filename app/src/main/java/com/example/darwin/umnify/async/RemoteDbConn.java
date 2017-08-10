package com.example.darwin.umnify.async;

import android.net.Uri;
import android.os.AsyncTask;
import com.example.darwin.umnify.authentication.AuthenticationKeys;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class RemoteDbConn <Parameters, Progess, Result> extends AsyncTask <Parameters, Progess, Result>{

    private final String urlAddress;
    private final int READ_TIMEOUT = 10000;
    private final int CONNECT_TIMEOUT = 15000;
    private final String REQUEST_METHOD = "POST";

    private URL url;
    private HttpURLConnection urlConnection;
    private Uri.Builder builder;

    public RemoteDbConn(String urlAddress){
        this.urlAddress = urlAddress;
        builder  = new Uri.Builder()
                .appendQueryParameter(AuthenticationKeys.IDENTIFICATION_KEY, AuthenticationKeys.IDENTIFICATION_VALUE)
                .appendQueryParameter(AuthenticationKeys.USERNAME_KEY, AuthenticationKeys.USERNAME_VALUE)
                .appendQueryParameter(AuthenticationKeys.PASSWORD_KEY, AuthenticationKeys.PASSWORD_VALUE);
    }

    public final void setUpConnection() throws IOException{

        url = new URL(urlAddress);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
        urlConnection.setRequestMethod(REQUEST_METHOD);

        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

    }

    public final void setRequest(String query) throws IOException{

        OutputStream stream = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(stream, "UTF-8"));

        writer.write(query);
        writer.flush();
        writer.close();
        stream.close();
    }

    public final Uri.Builder getQueryBuilder() {
        return builder;
    }

    public final HttpURLConnection getUrlConnection(){
        return urlConnection;
    }

    public final String getRequest() throws IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        return reader.readLine();
    }
}
