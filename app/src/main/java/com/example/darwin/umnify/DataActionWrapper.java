package com.example.darwin.umnify;

import android.app.Activity;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 10/1/17.
 */

public class DataActionWrapper implements WebServiceAction{

    private HashMap<String, String> textDataOutput;
    private InputStream inputStream;
    private WebServiceConnection connection;
    private Activity activity;
    private String response = null;
    private PostAsyncAction postAsyncAction;
    private String link;

    public DataActionWrapper(HashMap<String, String> textDataOutput, Activity activity, String link, PostAsyncAction postAsyncAction){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.postAsyncAction = postAsyncAction;
        this.link = link;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(link, activity,
                true, true, true);

        if(connection != null){

            connection.addAuthentication();
            DataHelper.writeTextUpload(textDataOutput, connection);
            connection.flushOutputStream();
            inputStream = connection.getInputStream();

            response = DataHelper.parseStringFromStream(inputStream);
        }else{

        }
    }

    @Override
    public void processResult() {

        postAsyncAction.processResult(response);
    }
}
