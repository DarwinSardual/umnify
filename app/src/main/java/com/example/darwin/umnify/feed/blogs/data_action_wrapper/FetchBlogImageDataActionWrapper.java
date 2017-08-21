package com.example.darwin.umnify.feed.blogs.data_action_wrapper;

import android.app.Activity;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.blogs.BlogFeedManager;
import com.example.darwin.umnify.feed.blogs.BlogTile;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

public class FetchBlogImageDataActionWrapper implements WebServiceAction{

    private BlogTile tile;
    private Activity activity;
    private BlogFeedManager manager;

    private WebServiceConnection connection = null;
    private InputStream inputStream;

    public FetchBlogImageDataActionWrapper(BlogTile tile, Activity activity,
                                           BlogFeedManager manager){

        this.tile = tile;
        this.activity = activity;
        this.manager = manager;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.BLOG_IMAGE_FOLDER + "/" + tile.getImageFile(),
                activity, true, true, false);


    }

    @Override
    public void processResult() {

    }
}
