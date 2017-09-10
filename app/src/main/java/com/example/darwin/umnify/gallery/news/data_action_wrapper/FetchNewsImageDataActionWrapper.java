package com.example.darwin.umnify.gallery.news.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.gallery.ImageWrapper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by darwin on 9/3/17.
 */

public class FetchNewsImageDataActionWrapper implements WebServiceAction{

    private ImageWrapper wrapper;
    private Activity activity;
    private FeedManager manager;

    private WebServiceConnection connection;
    private InputStream inputStream;
    private Bitmap image;


    public FetchNewsImageDataActionWrapper(ImageWrapper wrapper, Activity activity,
                                           FeedManager manager){
        this.wrapper = wrapper;
        this.activity = activity;
        this.manager = manager;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.NEWS_IMAGE_FOLDER + "/" + wrapper.getImageFile(), activity, true,
                true, true);

        if(connection != null){
            inputStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
        }

    }

    @Override
    public void processResult() {

        wrapper.setImage(image);
        manager.notifyItemChanged(wrapper.getIndex());
    }
}
