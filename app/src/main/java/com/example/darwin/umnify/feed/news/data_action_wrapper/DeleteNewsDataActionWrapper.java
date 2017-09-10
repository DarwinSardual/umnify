package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;
import android.util.Log;
import android.util.LruCache;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 8/29/17.
 */

public class DeleteNewsDataActionWrapper implements WebServiceAction{

    private HashMap<String, String> textDataOutput;
    private Activity activity;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private String response;
    private FeedManager manager;
    private News news;

    public DeleteNewsDataActionWrapper(HashMap<String, String> textDataOutput, News news, Activity activity,
                                       FeedManager manager){

        this.textDataOutput = textDataOutput;
        this.activity = activity;
        this.manager= manager;
        this.news = news;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.DELETE_NEWS, activity,
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

        manager.removeFromFeedList(news);
        manager.notifyItemRemoved(news.getIndex());
        news = null;

    }
}
