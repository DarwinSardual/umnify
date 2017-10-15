package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

public class FetchAuthorImageDataActionWrapper implements WebServiceAction {

    private InputStream inputStream;
    private WebServiceConnection connection;
    private Activity activity;
    private Bitmap image;

    private PostAsyncImageAction postAsyncImageAction;

    public FetchAuthorImageDataActionWrapper(Activity activity, PostAsyncImageAction postAsyncImageAction){

        this.postAsyncImageAction = postAsyncImageAction;
        this.activity = activity;

    }

    @Override
    public void processRequest() {
        connection = new WebServiceConnection(AuthenticationAddress.AVATAR_IMAGE_FOLDER + "/" + postAsyncImageAction.getImageFile(), activity,
                true, true, true);

        if(connection != null){
            inputStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
        }else{

        }

    }

    @Override
    public void processResult(){

        postAsyncImageAction.processResult(image);

    }
}
