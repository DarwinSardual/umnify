package com.example.darwin.umnify.feed.news.data_action_wrapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.connection.WebServiceConnection;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.InputStream;

public class FetchAuthorImageDataActionWrapper implements WebServiceAction {

    private InputStream inputStream;
    private News news;
    private WebServiceConnection connection;
    private FeedManager manager;
    private Activity activity;
    private Bitmap image;

    public FetchAuthorImageDataActionWrapper(News news, Activity activity, FeedManager manager){

        this.news = news;
        this.manager = manager;
        this.activity = activity;

    }

    @Override
    public void processRequest() {
        connection = new WebServiceConnection(AuthenticationAddress.AVATAR_IMAGE_FOLDER + "/" + news.getAuthorImageFile(), activity,
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
            news.setAuthorImage(Bitmap.createScaledBitmap(image, 100, 100, false));
            manager.notifyItemChanged(news.getIndex());
            image = null;
        }




    }
}
