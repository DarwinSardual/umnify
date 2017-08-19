package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import com.example.darwin.umnify.connection.RemoteDbConn;
import com.example.darwin.umnify.authentication.AuthenticationAddress;

import java.io.IOException;
import java.io.InputStream;

public class NewsImageAsync{

}

/*class NewsImageAsync extends RemoteDbConn<NewsWrapper, Void, NewsWrapper> {

    public NewsImageAsync(String urlAddress, Activity activity){
        super(urlAddress, activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected NewsWrapper doInBackground(NewsWrapper... wrapper) {

        try{
            super.setUpConnection(true, true, false);
            super.getUrlConnection().connect();
            InputStream imageStream;
            Bitmap image;

            if(wrapper[0].news.getAuthorImageFile() != null){
                imageStream = getUrlConnection().getInputStream();
                image = BitmapFactory.decodeStream(imageStream);
                wrapper[0].news.setAuthorImage(image);
            }

            if(wrapper[0].news.getImageFile() != null){

                super.resetUrl(AuthenticationAddress.NEWS_IMAGE_FOLDER + "/" + wrapper[0].news.getImageFile());
                super.setUpConnection(true, true, false);
                super.getUrlConnection().connect();

                imageStream = getUrlConnection().getInputStream();
                image = BitmapFactory.decodeStream(imageStream);
                wrapper[0].news.setImage(image);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return wrapper[0];
    }

    @Override
    protected void onPostExecute(NewsWrapper wrapper) {

        wrapper.manager.notifyItemChanged(wrapper.news.getIndex());
    }
}*/