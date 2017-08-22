package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;
import com.example.darwin.umnify.R;;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.news.data_action_wrapper.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFeedManager extends RecyclerView.Adapter<NewsFeedManager.ViewHolder> {

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<News> feedList;
    public HashMap<News, ViewHolder> newsViewHolderMap;

    private Activity activity;
    private Bundle userData;

    public boolean isFetching = false;

    public Drawable emptyStar;
    public Drawable filledStar;

    public NewsFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, Bundle userData) {

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.userData = userData;
        this.userData = userData;
        newsViewHolderMap = new HashMap<>();

        filledStar = ContextCompat.getDrawable(NewsFeedManager.this.activity, R.drawable.filled_star);
        emptyStar = ContextCompat.getDrawable(NewsFeedManager.this.activity, R.drawable.empty_star);

        feedList = new ArrayList<>();

        HashMap<String, String> fetchNewsDataParams = new HashMap<>();
        fetchNewsDataParams.put("order", "desc");
        fetchNewsDataParams.put("offset", feedList.size() + "");
        fetchNewsDataParams.put("limit", "5");
        fetchNewsDataParams.put("id", "-1");

        WebServiceAsync asyncFetchNews = new WebServiceAsync();
        FetchNewsDataActionWrapper fetchNewsWrapper = new FetchNewsDataActionWrapper(fetchNewsDataParams, activity, this);
        asyncFetchNews.execute(fetchNewsWrapper);

        fetchNewsWrapper = null;
        asyncFetchNews = null;
        fetchNewsDataParams = null;
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView newsContentView;
        public ImageView newsImageView;
        private TextView newsAuthorView;
        public ImageView newsAuthorImageView;
        private ImageButton newsStarButton;
        private CardView container;
        private TextView newsStarsCountView;
        private ImageButton newsCommentButton;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.feed_news, parent, false));

            container = (CardView) itemView.findViewById(R.id.news_card_container);
            newsContentView = (TextView) itemView.findViewById(R.id.news_content);
            newsStarsCountView = (TextView) itemView.findViewById(R.id.news_stars_count);
            newsAuthorView = (TextView) itemView.findViewById(R.id.news_author);
            newsImageView = (ImageView) itemView.findViewById(R.id.news_image);
            newsAuthorImageView = (ImageView) itemView.findViewById(R.id.author_image);
            newsStarButton = (ImageButton) itemView.findViewById(R.id.news_stars);
            newsCommentButton = (ImageButton) itemView.findViewById(R.id.news_comment);

            newsStarButton.setTag(newsStarsCountView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position < feedList.size()){

            final News news = feedList.get(position);
            holder.newsAuthorView.setText(news.getAuthorFirstname() + " " + news.getAuthorLastname());
            holder.newsContentView.setText(news.getContent());
            holder.newsAuthorImageView.setImageBitmap(news.getAuthorImage());
            holder.newsImageView.setImageBitmap(news.getImage());

            if(news.isStarred()){

                holder.newsStarButton.setImageDrawable(filledStar);
            }else{
                holder.newsStarButton.setImageDrawable(emptyStar);
            }
            holder.newsStarsCountView.setText(news.getStars() + " have starred this");

            HashMap<String, String> textData = new HashMap<>();
            textData.put("user", userData.getInt("USER_ID") + "");
            holder.newsStarButton.setOnClickListener(new StarButtonAction(textData, news));

            HashMap<String, String> starTextData = new HashMap<>();
            starTextData.put("id", userData.getInt("USER_ID") + "");

            newsViewHolderMap.put(news, holder);

            holder.newsCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NewsFeedManager.this.activity, NewsCommentActivity.class);
                    intent.putExtra("NEWS_ID", news.getId());
                    intent.putExtra("NEWS_STARS", news.getStars() + "");
                    NewsFeedManager.this.activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void addEntries(String data) throws JSONException{

        FetchAuthorImageDataActionWrapper fetchAuthorImageDataActionWrapper;
        FetchNewsImageDataActionWrapper fetchNewsImageDataActionWrapper;
        WebServiceAsync asyncFetchAuthorImage;
        WebServiceAsync asyncFetchNewsImage;
        News news;

        JSONArray dataList = new JSONArray(data);

        for(int i = 0; i < dataList.length(); i++){

            JSONObject newsData = new JSONObject(dataList.getString(i));
            news = NewsHelper.createNewsFromJSON(newsData, feedList.size());
            fetchAuthorImageDataActionWrapper = new FetchAuthorImageDataActionWrapper( news, activity, this);
            fetchNewsImageDataActionWrapper = new FetchNewsImageDataActionWrapper(news, activity, this);
            feedList.add(news);
            notifyItemInserted(news.getIndex());

            asyncFetchAuthorImage = new WebServiceAsync();
            asyncFetchAuthorImage.execute(fetchAuthorImageDataActionWrapper);
            asyncFetchNewsImage = new WebServiceAsync();
            asyncFetchNewsImage.execute(fetchNewsImageDataActionWrapper);
        }


        news = null;
        fetchAuthorImageDataActionWrapper = null;
        fetchNewsImageDataActionWrapper = null;
        asyncFetchAuthorImage = null;
        asyncFetchNewsImage = null;
        dataList = null;
        isFetching = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    public void updateFeed(int direction){

        if(isFetching) return;

        WebServiceAsync asyncFetchNews = new WebServiceAsync();
        FetchNewsDataActionWrapper fetchNewsWrapper;

        if(direction == 1){

            isFetching = true;

            HashMap<String, String> fetchNewsDataParamsUpdate = new HashMap<>();
            fetchNewsDataParamsUpdate.put("order", "desc");
            fetchNewsDataParamsUpdate.put("offset", feedList.size() + "");
            fetchNewsDataParamsUpdate.put("limit", "3");
            fetchNewsDataParamsUpdate.put("id", "-1");

            fetchNewsWrapper = new FetchNewsDataActionWrapper(fetchNewsDataParamsUpdate, activity, this);
            asyncFetchNews.execute(fetchNewsWrapper);

            fetchNewsDataParamsUpdate = null;

        }else if(direction == -1){
            feedList.clear();
            notifyDataSetChanged();

            isFetching = true;

            HashMap<String, String> fetchNewsDataParams = new HashMap<>();
            fetchNewsDataParams.put("order", "desc");
            fetchNewsDataParams.put("offset", feedList.size() + "");
            fetchNewsDataParams.put("limit", "5");
            fetchNewsDataParams.put("id", "-1");

            fetchNewsWrapper = new FetchNewsDataActionWrapper(fetchNewsDataParams, activity, this);
            asyncFetchNews.execute(fetchNewsWrapper);

            fetchNewsDataParams = null;
        }

        fetchNewsWrapper = null;
        asyncFetchNews = null;
    }
    public void addNews(Intent data, Bundle userData){

        Uri uri = data.getData();
        Cursor returnCursor;

        HashMap<String, String> textData = new HashMap<>();
        HashMap<String, byte[]> fileData = new HashMap<>();


        if(uri != null){

            returnCursor =
                    activity.getContentResolver()
                            .query(uri,
                                    null, null,
                                    null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();

            String imageFile = returnCursor.getString(nameIndex);
            Bitmap image = null;
            byte[] byteArray = null;

            try{
                image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();
            }catch (IOException e){
                e.printStackTrace();
            }

            textData.put("content", data.getStringExtra("ADD_NEWS_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") +"");

            fileData.put(imageFile, byteArray);

        }else{
            textData.put("content", data.getStringExtra("ADD_NEWS_CONTENT"));
            textData.put("author", userData.getInt("USER_ID") +"");
            textData.put("user_type", userData.getInt("USER_TYPE") +"");
        }

        AddNewsDataActionWrapper addNewsDataActionWrapper = new AddNewsDataActionWrapper(textData,
                fileData, activity);

        WebServiceAsync asyncAddNews = new WebServiceAsync();
        asyncAddNews.execute(addNewsDataActionWrapper);

    }

    private class StarButtonAction implements View.OnClickListener{

        private HashMap<String, String> textDataOutput;
        private News news;


        public StarButtonAction(HashMap<String, String> textDataOutput, News news){

            this.textDataOutput = textDataOutput;
            this.news = news;
        }

        @Override
        public void onClick(View view) {

            StarredNewsDataActionWrapper starredNewsDataActionWrapper  = new StarredNewsDataActionWrapper(textDataOutput, news,
                    view, NewsFeedManager.this.activity, NewsFeedManager.this);

            WebServiceAsync asyncStarredNews = new WebServiceAsync();
            asyncStarredNews.execute(starredNewsDataActionWrapper);

            asyncStarredNews = null;

        }
    }
}