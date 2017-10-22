package com.example.darwin.umnify.connection;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class WebServiceConnection{

    private String urlAddress;
    private final int READ_TIMEOUT = 10000;
    private final int CONNECT_TIMEOUT = 5000;
    private final String REQUEST_METHOD = "POST";

    private URL url;
    private HttpsURLConnection urlConnection;
    private Uri.Builder builder;

    private String crlf = "\r\n";
    private String twoHyphens = "--";
    private String boundary = "*****";

    private Activity activity;
    private OutputStream outputStream;
    private InputStream inputStream;
    private DataOutputStream dataOutputStream;

    SSLContext context;

    public WebServiceConnection(String urlAddress, Activity activity,
                                boolean doInput, boolean doOuput, boolean useCaches){

        this.activity = activity;
        this.urlAddress = urlAddress;

        builder  = new Uri.Builder()
                .appendQueryParameter(AuthenticationKeys.IDENTIFICATION_KEY, AuthenticationKeys.IDENTIFICATION_VALUE)
                .appendQueryParameter(AuthenticationKeys.USERNAME_KEY, AuthenticationKeys.USERNAME_VALUE)
                .appendQueryParameter(AuthenticationKeys.PASSWORD_KEY, AuthenticationKeys.PASSWORD_VALUE);

        try{

            url = new URL(urlAddress);

            urlConnection = (HttpsURLConnection) url.openConnection();
            //trustCertificate();
            /*/urlConnection.setSSLSocketFactory(context.getSocketFactory());
            urlConnection.setHostnameVerifier(new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });*/


            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Cache-Control", "no-cache");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);

            urlConnection.setDoInput(doInput);
            urlConnection.setDoOutput(doOuput);
            urlConnection.setUseCaches(useCaches);



            outputStream = urlConnection.getOutputStream();
            dataOutputStream = null;

        }catch (IOException e){
            e.printStackTrace();
        }
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

    protected final void setRequest(String query){

        try{

            outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            writer.write(query);
            writer.flush();
            writer.close();
            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addFileUpload(String name, String filename, byte[] byteArray){

        try{

            if(outputStream == null){
                return;
            }

            if(dataOutputStream == null)
                dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeBytes(twoHyphens +boundary + crlf);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\";filename=\"" + filename + "\"" + crlf);
            dataOutputStream.writeBytes(crlf);

            dataOutputStream.write(byteArray);

            dataOutputStream.writeBytes(crlf);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addTextUpload(String name, String value){

        try{

            if(outputStream == null){
                return;
            }

            if(dataOutputStream == null)
                 dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeBytes(twoHyphens+ boundary + crlf);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\";" + crlf);
            dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
            dataOutputStream.writeBytes(crlf + value + crlf);

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public void addAuthentication(){



        addTextUpload(AuthenticationKeys.IDENTIFICATION_KEY, AuthenticationKeys.IDENTIFICATION_VALUE);
        addTextUpload(AuthenticationKeys.USERNAME_KEY, AuthenticationKeys.USERNAME_VALUE);
        addTextUpload(AuthenticationKeys.PASSWORD_KEY, AuthenticationKeys.PASSWORD_VALUE);

    }

    public void flushOutputStream(){

        if(dataOutputStream == null){
            return;
        }

        try{

            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    protected final String getUrlAddress() {
        return urlAddress;
    }

    protected final Uri.Builder getQueryBuilder() {
        return builder;
    }

    protected final HttpsURLConnection getUrlConnection(){
        return urlConnection;
    }

    public InputStream getInputStream(){

        try{
            return urlConnection.getInputStream();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    protected final String getRequest() throws IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        return reader.readLine() + reader.readLine();
    }
}
