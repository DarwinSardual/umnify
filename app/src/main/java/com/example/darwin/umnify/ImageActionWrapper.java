package com.example.darwin.umnify;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

/**
 * Created by darwin on 10/1/17.
 */

public class ImageActionWrapper implements WebServiceAction{

    private InputStream inputStream;
    private WebServiceConnection connection;
    private Activity activity;
    private Bitmap image;
    private String link;
    private PostAsyncImageAction postAsyncImageAction;

    public ImageActionWrapper(Activity activity, String link, PostAsyncImageAction postAsyncImageAction){

        this.postAsyncImageAction = postAsyncImageAction;
        this.activity = activity;
        this.link = link;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(link, activity,
                true, true, true);

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
