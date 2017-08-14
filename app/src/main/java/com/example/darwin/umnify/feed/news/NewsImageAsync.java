package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.darwin.umnify.async.RemoteDbConn;

import java.io.IOException;
import java.io.InputStream;

class NewsImageAsync extends RemoteDbConn<NewsWrapper, Void, NewsWrapper> {

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
            super.setUpConnection();
            super.getUrlConnection().connect();

            InputStream imageStream = getUrlConnection().getInputStream();
            Bitmap image = BitmapFactory.decodeStream(imageStream);

            wrapper[0].news.setAuthorImage(image);

            return wrapper[0];

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(NewsWrapper wrapper) {

        wrapper.manager.notifyItemChanged(wrapper.news.getIndex());
    }
}