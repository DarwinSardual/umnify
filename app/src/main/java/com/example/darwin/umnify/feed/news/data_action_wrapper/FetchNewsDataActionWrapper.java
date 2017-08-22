package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;
import android.util.Log;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.news.NewsFeedManager;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class FetchNewsDataActionWrapper implements WebServiceAction {

    private HashMap<String, String> textDataOutput;
    private NewsFeedManager manager;
    private InputStream inputStream;
    private WebServiceConnection connection;
    private Activity activity;
    private String response;

    public FetchNewsDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity, NewsFeedManager manager){

        this.textDataOutput = textDataOutput;
        this.manager = manager;
        this.activity = activity;
    }

    public void setTextDataOutput(HashMap<String, String> textDataOutput) {
        this.textDataOutput = textDataOutput;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_NEWS, activity,
                true, true, true);

        connection.addAuthentication();
        DataHelper.writeTextUpload(textDataOutput, connection);
        connection.flushOutputStream();
        inputStream = connection.getInputStream();
        try{
            response = DataHelper.parseStringFromStream(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }



    }

    @Override
    public void processResult() {

        try {

            JSONObject str = new JSONObject(response);
            String data = str.getString("data");

            manager.addEntries(data);
            manager.isFetching = false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
