package com.example.darwin.umnify.gallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

/**
 * Created by darwin on 9/3/17.
 */

public class FetchGalleryThumbnailDataActionWrapper implements WebServiceAction {

    private ImageView imageView;
    private Activity activity;
    private WebServiceConnection connection;
    private InputStream inputStream;
    private String urlLocation;

    private Bitmap image;
    private Bitmap anotherImage;

    public FetchGalleryThumbnailDataActionWrapper(ImageView imageView, String urlLocation, Activity activity){

        this.imageView = imageView;
        this.activity = activity;
        this.urlLocation = urlLocation;
    }

    @Override
    public void processRequest() {

        connection = new WebServiceConnection(urlLocation, activity, true,
                true, true);

        if(connection != null){
            inputStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
        }

    }

    @Override
    public void processResult() {

        imageView.setImageBitmap(image);

    }
}
