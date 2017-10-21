package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

public class FetchNewsDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private InputStream inputStream;
    private WebServiceConnection connection;
    private Activity activity;
    private String response = null;
    private PostAsyncAction postAsyncAction;

    public FetchNewsDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity, PostAsyncAction postAsyncAction){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.postAsyncAction = postAsyncAction;
    }

    public void setTextDataOutput(HashMap<String, String> textDataOutput) {
        this.textDataOutput = textDataOutput;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_NEWS, activity,
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
