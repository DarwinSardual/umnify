package com.example.darwin.umnify.feed.notifications.data_action_wrapper;

import android.app.Activity;
import android.util.Log;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.notifications.Notification;
import com.example.darwin.umnify.feed.notifications.feed_manager.NotificationFeedManager;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/16/17.
 */

public class FetchNotificationDataActionWrapper implements WebServiceAction {

    private WebServiceConnection connection;
    private Activity activity;
    private InputStream inputStream;
    private String response;
    private HashMap<String, String> textDataOutput;
    private FeedManager manager;

    public FetchNotificationDataActionWrapper(HashMap<String, String> textDataOutput, Activity activity,
                                              FeedManager manager){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.manager = manager;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.FETCH_NOTIFICATION, activity,
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

        if(response != null){

            try{
                JSONObject json = new JSONObject(response);
                String data = json.getString("data");

                JSONArray arr = new JSONArray(data);
                JSONObject firstRow = (JSONObject) arr.remove(0);
                int count = firstRow.getInt("count");
                NotificationFeedManager notificationFeedManager = (NotificationFeedManager) manager;

                manager.addFeedEntries(arr.toString());
                manager.setFetchingFeedEntry(false);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{

        }

    }
}
