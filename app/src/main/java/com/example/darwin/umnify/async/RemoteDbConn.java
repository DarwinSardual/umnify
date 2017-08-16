package com.example.darwin.umnify.async;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.example.darwin.umnify.authentication.AuthenticationKeys;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public abstract class RemoteDbConn <Parameters, Progess, Result> extends AsyncTask <Parameters, Progess, Result>{

    private String urlAddress;
    private final int READ_TIMEOUT = 10000;
    private final int CONNECT_TIMEOUT = 15000;
    private final String REQUEST_METHOD = "POST";

    private URL url;
    private HttpURLConnection urlConnection;
    private Uri.Builder builder;

    private Activity activity;

    SSLContext context;

    public RemoteDbConn(String urlAddress, Activity activity){

        this.activity = activity;
        this.urlAddress = urlAddress;
        builder  = new Uri.Builder()
                .appendQueryParameter(AuthenticationKeys.IDENTIFICATION_KEY, AuthenticationKeys.IDENTIFICATION_VALUE)
                .appendQueryParameter(AuthenticationKeys.USERNAME_KEY, AuthenticationKeys.USERNAME_VALUE)
                .appendQueryParameter(AuthenticationKeys.PASSWORD_KEY, AuthenticationKeys.PASSWORD_VALUE);

        //trustCertificate();
    }

    private final void trustCertificate(){

        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            AssetManager am = activity.getAssets();
            InputStream caInput = am.open("ssl.crt");

            Certificate ca;
            try{
                ca = cf.generateCertificate(caInput);
                //System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            }finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }catch(CertificateException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(KeyStoreException e){
            e.printStackTrace();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (KeyManagementException e){
            e.printStackTrace();
        }
    }

    protected final void setUpConnection() throws IOException{

        url = new URL(urlAddress);
        //urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection = (HttpURLConnection) url.openConnection();
        //urlConnection.setSSLSocketFactory(context.getSocketFactory());
        /*urlConnection.setHostnameVerifier(new HostnameVerifier()
        {
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        });*/

        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
        urlConnection.setRequestMethod(REQUEST_METHOD);

        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

    }

    protected final void setRequest(String query) throws IOException{

        OutputStream stream = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(stream, "UTF-8"));

        writer.write(query);
        writer.flush();
        writer.close();
        stream.close();
    }

    protected final void resetUrl(String urlAddress){
        this.urlAddress = urlAddress;
    }

    protected final String getUrlAddress() {
        return urlAddress;
    }

    protected final Uri.Builder getQueryBuilder() {
        return builder;
    }

    /*protected final HttpsURLConnection getUrlConnection(){
        return urlConnection;
    }*/
    protected final HttpURLConnection getUrlConnection(){
        return urlConnection;
    }

    protected final String getRequest() throws IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        return reader.readLine() + reader.readLine();
    }
}
