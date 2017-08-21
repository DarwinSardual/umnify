package com.example.darwin.umnify.feed.blogs.data_action_wrapper;

import android.app.Activity;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

public class AddBlogDataActionWrapper implements WebServiceAction{

    private HashMap<String, String> textDataOutput;
    private HashMap<String, byte[]> fileDataOutput;
    private Activity activity;

    private InputStream inputStream;
    private WebServiceConnection connection = null;

    public AddBlogDataActionWrapper(HashMap<String, String> textDataOutput,
                                    HashMap<String, byte[]> fileDataOutput,
                                    Activity activity){

        this.textDataOutput = textDataOutput;
        this.fileDataOutput = fileDataOutput;

    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.ADD_BLOG, activity,
                true, true, true);

        if(connection != null){

            connection.addAuthentication();
            DataHelper.writeTextUpload(textDataOutput, connection);
            DataHelper.writeFileUpload("image", fileDataOutput, connection);

            connection.flushOutputStream();

            inputStream = connection.getInputStream();
        }

    }

    @Override
    public void processResult() {

        try{

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
