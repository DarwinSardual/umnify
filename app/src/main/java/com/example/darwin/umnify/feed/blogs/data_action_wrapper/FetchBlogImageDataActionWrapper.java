package com.example.darwin.umnify.feed.blogs.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

public class FetchBlogImageDataActionWrapper implements WebServiceAction{

    private Activity activity;

    private WebServiceConnection connection = null;
    private InputStream inputStream;
    private Bitmap image;
    private PostAsyncImageAction postAsyncImageAction;

    public FetchBlogImageDataActionWrapper(Activity activity,
                                           PostAsyncImageAction postAsyncImageAction){
        this.activity = activity;
        this.postAsyncImageAction = postAsyncImageAction;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.BLOG_IMAGE_FOLDER + "/" + postAsyncImageAction.getImageFile(),
                activity, true, true, false);

        if(connection != null){
            inputStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
        }else{

        }
    }

    @Override
    public void processResult() {

        postAsyncImageAction.processResult(image);

    }
}
