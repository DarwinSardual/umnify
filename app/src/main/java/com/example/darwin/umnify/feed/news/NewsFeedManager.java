package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.RemoteDbConn;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationKeys;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFeedManager extends RecyclerView.Adapter<NewsFeedManager.ViewHolder> {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private final int MAX_FEED_SIZE = 50;

    private List<News> feedList;
    private NewsFeedAsync newsHandler;

    private Activity activity;

    private boolean isFetching = false;



    public NewsFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView) {

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;

        feedList = new ArrayList<>();

        newsHandler = new NewsFeedAsync(AuthenticationAddress.FETCH_NEWS);
        isFetching = true;
        newsHandler.execute("desc", feedList.size() + "", "5", "-1");

    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView newsContentView;
        private ImageView newsImageView;
        private TextView newsAuthorView;
        private ImageView newsAuthorImageView;
        private CardView container;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.feed_news, parent, false));

            container = (CardView) itemView.findViewById(R.id.news_card_container);
            newsContentView = (TextView) itemView.findViewById(R.id.news_content);
            newsAuthorView = (TextView) itemView.findViewById(R.id.news_author);
            newsImageView = (ImageView) itemView.findViewById(R.id.news_image);
            newsAuthorImageView = (ImageView) itemView.findViewById(R.id.author_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position < feedList.size()){

            News news = feedList.get(position);
            holder.newsAuthorView.setText(news.getAuthorFirstname() + " " + news.getAuthorLastname());
            holder.newsContentView.setText(news.getContent());
            holder.newsAuthorImageView.setImageBitmap(news.getAuthorImage());

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), NewsActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    private void addEntries(String data) throws JSONException{

        JSONArray dataList = new JSONArray(data);
        int temp = feedList.size();

        for(int i = 0; i < dataList.length(); i++){

            JSONObject newsData = new JSONObject(dataList.getString(i));
            News news = NewsHelper.createNewsFromJSON(newsData, feedList.size());
            NewsHelper.fetchImage(news, this, activity);
            feedList.add(news);
        }
        notifyItemRangeInserted(temp, dataList.length());
        swipeRefreshLayout.setRefreshing(false);
    }

    public void updateFeed(int direction){

        if(isFetching) return;

        if(direction == 1){

            newsHandler = new NewsFeedAsync(AuthenticationAddress.FETCH_NEWS);
            isFetching = true;
            newsHandler.execute("desc", feedList.size() + "", "3", "-1");

        }else if(direction == -1){

            feedList.clear();
            notifyDataSetChanged();

            newsHandler = new NewsFeedAsync(AuthenticationAddress.FETCH_NEWS);
            isFetching = true;
            newsHandler.execute("desc", 0 + "", "5", "-1");
        }
    }

    private class NewsFeedAsync extends RemoteDbConn<String, Void, String>{

        public NewsFeedAsync(String urlAddress){
            super(urlAddress, NewsFeedManager.this.activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{

                super.setUpConnection();
                Uri.Builder queryBuilder = super.getQueryBuilder();
                queryBuilder.appendQueryParameter("order", strings[0])
                        .appendQueryParameter("offset", strings[1])
                        .appendQueryParameter("limit", strings[2])
                        .appendQueryParameter("id", strings[3]);

                super.setRequest(queryBuilder.build().getEncodedQuery());
                super.getUrlConnection().connect();

                String response = super.getRequest();
                return  response;

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            try {
                JSONObject str = new JSONObject(response);
                String data = str.getString("data");

                addEntries(data);
                isFetching = false;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /* Handle adding of news here */

    public void addNews(Intent data, Bundle userData){

        //int authorId =



        DataWrapper newsWrapper = new DataWrapper(data.getStringExtra("ADD_NEWS_CONTENT"),
                null, userData.getInt("USER_ID"), null);

    }

    private class DataWrapper{

        private String content;
        private Bitmap image;
        private int authorId;
        private String imageFile;

        public DataWrapper(String content, String imageFile, int authorId, Bitmap image){
            this.content = content;
            this.image = image;
            this.authorId = authorId;
            this.imageFile = imageFile;
        }

        public String getContent() {
            return content;
        }

        public int getAuthorId() {
            return authorId;
        }

        public Bitmap getImage() {
            return image;
        }

        public String getImageFile() {
            return imageFile;
        }
    }

    private class AddNewsAsync extends RemoteDbConn<DataWrapper, Void, String>{

        public AddNewsAsync(String urlAddress, Activity activity){
            super(urlAddress, activity);
        }

        @Override
        protected String doInBackground(DataWrapper... wrapper) {

            try{

                setUpConnection();
                Uri.Builder queryBuilder = getQueryBuilder();
                queryBuilder.appendQueryParameter("content", wrapper[0].getContent())
                        .appendQueryParameter("image", wrapper[0].getImageFile())
                        .appendQueryParameter("author", wrapper[0].getAuthorId() + "");

                super.setRequest(getQueryBuilder().build().getEncodedQuery());
                super.getUrlConnection().connect();


            }catch (Exception e){

            }


            return null;
        }
    }

}