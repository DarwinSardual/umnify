package com.example.darwin.umnify.feed.blogs.data_action_wrapper;

import android.app.Activity;
import android.util.Log;
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
    String response;

    public AddBlogDataActionWrapper(HashMap<String, String> textDataOutput,
                                    HashMap<String, byte[]> fileDataOutput,
                                    Activity activity){

        this.textDataOutput = textDataOutput;
        this.fileDataOutput = fileDataOutput;
        this.activity = activity;

    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.ADD_BLOG, activity,
                true, true, true);

        if(connection != null){

            connection.addAuthentication();
            DataHelper.writeFileUpload("image", fileDataOutput, connection);
            DataHelper.writeTextUpload(textDataOutput, connection);

            connection.flushOutputStream();

            inputStream = connection.getInputStream();
            response = DataHelper.parseStringFromStream(inputStream);
        }else{

        }

    }

    @Override
    public void processResult() {

        try{

            Log.e("Add blog", response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
