package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;
import android.util.Log;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class AddNewsDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private HashMap<String, byte[]> fileDataOutput;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private Activity activity;

    public AddNewsDataActionWrapper(HashMap<String, String> textDataOutput, HashMap<String, byte[]> fileDataOutput,
                                    Activity activity){

        this.textDataOutput = textDataOutput;
        this.fileDataOutput = fileDataOutput;
        this.activity = activity;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.ADD_NEWS, activity,
                true, true, true);

        connection.addAuthentication();
        DataHelper.writeFileUpload("image", fileDataOutput, connection);
        DataHelper.writeTextUpload(textDataOutput, connection);
        connection.flushOutputStream();

        inputStream = connection.getInputStream();
    }

    @Override
    public void processResult() {

        try{

            String response = DataHelper.parseStringFromStream(inputStream);
            Log.e("AddNewsData", response);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}