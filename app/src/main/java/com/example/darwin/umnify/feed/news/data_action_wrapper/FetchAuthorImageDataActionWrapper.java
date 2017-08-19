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

public class FetchAuthorImageDataActionWrapper implements WebServiceAction {

    private InputStream inputStream;
    private News news;
    private WebServiceConnection connection;
    private NewsFeedManager manager;
    private Activity activity;
    private Bitmap image;

    public FetchAuthorImageDataActionWrapper(News news, Activity activity, NewsFeedManager manager){

        this.news = news;
        this.manager = manager;
        this.activity = activity;

    }

    @Override
    public void processRequest() {
        connection = new WebServiceConnection(AuthenticationAddress.AVATAR_IMAGE_FOLDER + "/" + news.getAuthorImageFile(), activity,
                true, true, true);

        inputStream = connection.getInputStream();
        image = BitmapFactory.decodeStream(inputStream);
    }

    @Override
    public void processResult(){



        news.setAuthorImage(image);
        manager.notifyItemChanged(news.getIndex());
    }
}
