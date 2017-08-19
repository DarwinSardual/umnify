package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.feed.news.NewsFeedManager;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

public class FetchNewsImageDataActionWrapper implements WebServiceAction {

    private InputStream inputStream;
    private News news;
    private WebServiceConnection connection;
    private NewsFeedManager manager;
    private Activity activity;
    private Bitmap image;

    public FetchNewsImageDataActionWrapper(News news, Activity activity, NewsFeedManager manager){

        this.manager = manager;
        this.news = news;
        this.activity = activity;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.NEWS_IMAGE_FOLDER + "/" + news.getImageFile(), activity,
                true, true, true);
        inputStream = connection.getInputStream();
        image = BitmapFactory.decodeStream(inputStream);
    }

    @Override
    public void processResult() {

        news.setImage(image);
        manager.notifyItemChanged(news.getIndex());
    }
}