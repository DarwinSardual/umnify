package com.example.darwin.umnify.feed.announcements.data_action_wrapper;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/29/17.
 */

public class UpdateAnnouncementDataActionWrapper implements WebServiceAction{

    private HashMap<String, String> textDataOutput;
    private HashMap<String, byte[]> fileDataOutput;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private Activity activity;
    private String response;
    private PostAsyncAction postAsyncAction;

    public UpdateAnnouncementDataActionWrapper(HashMap<String, String> textDataOutput, HashMap<String, byte[]> fileDataOutput,
                                       Activity activity, PostAsyncAction postAsyncAction){

        this.textDataOutput = textDataOutput;
        this.fileDataOutput = fileDataOutput;
        this.activity = activity;
        this.postAsyncAction = postAsyncAction;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.UPDATE_ANNOUNCEMENT, activity,
                true, true, true);

        if(connection != null){

            connection.addAuthentication();
            DataHelper.writeFileUpload("image", fileDataOutput, connection);
            DataHelper.writeTextUpload(textDataOutput, connection);
            connection.flushOutputStream();

            inputStream = connection.getInputStream();
            response = DataHelper.parseStringFromStream(inputStream);
        }
    }

    @Override
    public void processResult() {

        if(response != null){
            postAsyncAction.processResult(response);
        }else{
            postAsyncAction.processResult(null);

        }

    }
}
