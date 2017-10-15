package com.example.darwin.umnify.feed.blogs.data_action_wrapper;

import android.app.Activity;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

public class FetchBlogDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private WebServiceConnection connection;
    private Activity activity;
    private InputStream inputStream;

    private String response;
    private PostAsyncAction postAsyncAction;

    public FetchBlogDataActionWrapper(HashMap<String, String> textDataOutput,
                                      Activity activity, PostAsyncAction postAsyncAction){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.postAsyncAction = postAsyncAction;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_BLOG, activity,
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
