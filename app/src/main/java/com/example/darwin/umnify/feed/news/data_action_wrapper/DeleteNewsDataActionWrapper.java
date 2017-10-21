package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncDeleteAction;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 8/29/17.
 */

public class DeleteNewsDataActionWrapper implements WebServiceAction{

    private HashMap<String, String> textDataOutput;
    private Activity activity;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private String response;
    private PostAsyncDeleteAction postAsyncDeleteAction;

    public DeleteNewsDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity,
                                       PostAsyncDeleteAction postAsyncDeleteAction){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.postAsyncDeleteAction = postAsyncDeleteAction;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.DELETE_NEWS, activity,
                true, true, true);

        if(connection != null){
            connection.addAuthentication();
            DataHelper.writeTextUpload(textDataOutput, connection);

            connection.flushOutputStream();
            inputStream = connection.getInputStream();
            response = DataHelper.parseStringFromStream(inputStream);
        }
    }

    @Override
    public void processResult(){
        postAsyncDeleteAction.processResult(response);
    }
}
