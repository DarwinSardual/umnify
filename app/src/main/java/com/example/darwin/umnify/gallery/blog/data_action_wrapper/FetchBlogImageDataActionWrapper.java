package com.example.darwin.umnify.gallery.blog.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.gallery.ImageWrapper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

/**
 * Created by darwin on 9/3/17.
 */

public class FetchBlogImageDataActionWrapper implements WebServiceAction{

    private ImageWrapper wrapper;
    private Activity activity;
    private FeedManager manager;

    private WebServiceConnection connection;
    private InputStream inputStream;
    private Bitmap image;

    public FetchBlogImageDataActionWrapper(ImageWrapper wrapper, Activity activity,
                                           FeedManager manager){
        this.wrapper = wrapper;
        this.activity = activity;
        this.manager = manager;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.BLOG_IMAGE_FOLDER + "/" + wrapper.getImageFile(), activity, true,
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

