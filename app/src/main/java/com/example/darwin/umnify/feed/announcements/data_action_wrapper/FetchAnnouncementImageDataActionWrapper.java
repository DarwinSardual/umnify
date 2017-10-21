package com.example.darwin.umnify.feed.announcements.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

/**
 * Created by darwin on 9/29/17.
 */

public class FetchAnnouncementImageDataActionWrapper implements WebServiceAction {

    private InputStream inputStream;
    private WebServiceConnection connection;
    private Activity activity;
    private Bitmap image;

    private PostAsyncImageAction postAsyncImageAction;
    public FetchAnnouncementImageDataActionWrapper(Activity activity, PostAsyncImageAction postAsyncImageAction){

        this.postAsyncImageAction = postAsyncImageAction;
        this.activity = activity;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(AuthenticationAddress.ANNOUNCEMENT_IMAGE_FOLDER + "/preview/" + postAsyncImageAction.getImageFile(), activity,
                true, true, true);

        if(connection != null){
            inputStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
        }else{

        }
    }

    @Override
    public void processResult() {

        if(image != null){
            postAsyncImageAction.processResult(image);
        }else{
            postAsyncImageAction.processResult(null);
        }
    }
}
