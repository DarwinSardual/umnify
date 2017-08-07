package com.example.darwin.umnify.feed.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class News {

    private int id;
    private String content;
    private Drawable image;
    private String publishedDate;
    private int signature;

    private int authorId;
    private String authorFirstname;
    private String authorLastname;
    private String authorImageFile;

    private Bitmap authorImage;

    private int index;
    private NewsFeedManager manager;

    private JSONObject data;

    public News(String content,Drawable image){

        this.content = content;
        this.image = image;
    }

    public News(int id, String content, Drawable image, int authorId, String publishedDate, int signature){

        this.id = id;
        this.content = content;
        this.image = null; //replace this
        this.authorId = authorId;
        this.publishedDate = publishedDate;
        this.signature = signature;

    }

    public News(JSONObject data, int index, NewsFeedManager manager) throws JSONException{
        this.data = data;
        this.index = index;
        this.manager = manager;

        setFieldsFromJSON();
    }

    private void setFieldsFromJSON() throws JSONException{

        id = data.getInt("id");
        content = data.getString("content");
        image = null;
        authorId = data.getInt("author");
        publishedDate = data.getString("published_date");
        signature = data.getInt("signature");

        authorFirstname = data.getString("firstname");
        authorLastname = data.getString("lastname");
        authorImageFile = data.getString("author_image");

        authorImage = null;
    }

    private class ImageAsync extends AsyncTask<String, Void, Bitmap> {

        public String urlAddress;

        private final int READ_TIMEOUT = 10000;
        private final int CONNECT_TIMEOUT = 15000;
        private final String REQUEST_METHOD = "POST";

        private URL url;
        private HttpURLConnection urlConnection;
        private Uri.Builder builder;

        public ImageAsync(String filename){

            urlAddress = "http://192.168.0.100/~darwin/UMnifyMobileScripts/images/avatar/";
            urlAddress += filename;

        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            try{

                url = new URL(urlAddress);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection.setRequestMethod(REQUEST_METHOD);
                urlConnection.setDoInput(true);

                urlConnection.connect();

                InputStream imageStream = urlConnection.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(imageStream);

                return image;
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //super.onPostExecute(bitmap);

            authorImage = bitmap;
            manager.notifyItemChanged(index);

        }
    }

    public void fetchImage(){

        ImageAsync imageAsync = new ImageAsync(authorImageFile);
        imageAsync.execute();

    }

    public int getId(){
        return id;
    }

    public String getContent(){
        return content;
    }

    public Drawable getImage() {
         return image;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getSignature() {
        return signature;
    }

    public JSONObject getData() {
        return data;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthorFirstname(){ return authorFirstname;}

    public String getAuthorLastname() {
        return authorLastname;
    }

    public String getAuthorImageFile() {
        return authorImageFile;
    }

    public Bitmap getAuthorImage() {
        return authorImage;
    }
}