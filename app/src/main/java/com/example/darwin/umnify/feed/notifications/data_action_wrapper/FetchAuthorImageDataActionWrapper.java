package com.example.darwin.umnify.feed.notifications.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.notifications.Notification;
import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

/**
 * Created by darwin on 9/16/17.
 */

public class FetchAuthorImageDataActionWrapper implements WebServiceAction {

    private InputStream inputStream;
    private Notification notification;
    private WebServiceConnection connection;
    private FeedManager manager;
    private Activity activity;
    private Bitmap image;

    public FetchAuthorImageDataActionWrapper(Notification notification, Activity activity, FeedManager manager){

        this.notification = notification;
        this.manager = manager;
        this.activity = activity;

    }

    @Override
    public void processRequest() {
        connection = new WebServiceConnection(AuthenticationAddress.AVATAR_IMAGE_FOLDER + "/" + notification.getAuthorImageFile(), activity,
                true, true, true);

        if(connection != null){
            inputStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
        }else{

        }

    }

    @Override
    public void processResult(){

        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        //image.compress(Bitmap.CompressFormat.JPEG, 100, out);

        //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

        if(image != null){
            notification.setAuthorImage(image);
            //NewsHelper.saveAuthorImageToInternal(image, news.getAuthorImageFile(), activity);
            GalleryHelper.saveImageToInternal(image,
                    notification.getAuthorImageFile(), activity, "avatar");
            image = null;
        }else{
            Bitmap authorImage = GalleryHelper.loadImageFromInternal(notification.getAuthorImageFile(), activity, "avatar");
            if(authorImage != null){
                notification.setAuthorImage(authorImage);
            }else{
                notification.setAuthorImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.missing_avatar));
            }
        }

        manager.notifyItemChanged(notification.getIndex());

    }
}

