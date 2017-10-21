package com.example.darwin.umnify.feed.announcements.data_action_wrapper;

import android.app.Activity;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncDeleteAction;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/29/17.
 */

public class DeleteAnnouncementDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private Activity activity;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private String response;
    private PostAsyncDeleteAction postAsyncDeleteAction;

    public DeleteAnnouncementDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity,
                                       PostAsyncDeleteAction postAsyncDeleteAction){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.postAsyncDeleteAction = postAsyncDeleteAction;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.DELETE_ANNOUNCEMENT, activity,
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

        if(response != null){
            postAsyncDeleteAction.processResult(response);
        }else{
            postAsyncDeleteAction.processResult(null);
        }
    }
}
