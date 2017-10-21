package com.example.darwin.umnify.feed.blogs.data_action_wrapper;

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
 * Created by darwin on 9/26/17.
 */

public class UpdateBlogDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private HashMap<String, byte[]> fileDataOutput;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private Activity activity;
    private String response;

    private PostAsyncAction postAsyncAction;

    public UpdateBlogDataActionWrapper(HashMap<String, String> textDataOutput, HashMap<String, byte[]> fileDataOutput,
                                       Activity activity, PostAsyncAction postAsyncAction){

        this.textDataOutput = textDataOutput;
        this.fileDataOutput = fileDataOutput;
        this.activity = activity;
        this.postAsyncAction = postAsyncAction;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.UPDATE_BLOG, activity,
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

        postAsyncAction.processResult(response);

    }
}
