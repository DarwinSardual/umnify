package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Bundle userData;

    private boolean isFetching = false;

    private String crlf = "\r\n";
    private String twoHyphens = "--";
    private String boundary = "*****";


    public NewsFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, Bundle userData) {

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.userData = userData;
        this.recyclerView = recyclerView;
        this.userData = userData;

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
        private Button newsStarButton;
        private CardView container;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.feed_news, parent, false));

            container = (CardView) itemView.findViewById(R.id.news_card_container);
            newsContentView = (TextView) itemView.findViewById(R.id.news_content);
            newsAuthorView = (TextView) itemView.findViewById(R.id.news_author);
            newsImageView = (ImageView) itemView.findViewById(R.id.news_image);
            newsAuthorImageView = (ImageView) itemView.findViewById(R.id.author_image);
            newsStarButton = (Button) itemView.findViewById(R.id.news_stars);
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
            holder.newsImageView.setImageBitmap(news.getImage());

            if(news.isStarred()){
                holder.newsStarButton.setTextColor(Color.YELLOW);
            }else{
                holder.newsStarButton.setTextColor(Color.BLACK);
            }
            holder.newsStarButton.setText(news.getStars() + "");


            // refactor this two code, this is heavy in memory
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(view.getContext(), NewsActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

            holder.newsStarButton.setOnClickListener(new StarButtonAction(userData.getInt("USER_ID"), news));
        }
    }

    private class UserNewsWrapper{

        private int userId;
        private News news;
        private Bundle extraData;


        public UserNewsWrapper(int userId, News news, Bundle extraData){

            this.userId = userId;
            this.news = news;
            this.extraData = extraData;
        }

        public void setExtraData(Bundle extraData){
            this.extraData = extraData;
        }

        public Bundle getExtraData() {
            return extraData;
        }
    }

    //handle when star button is clicked
    private class StarButtonAction implements View.OnClickListener{

        private UserNewsWrapper wrapper;

        public StarButtonAction(int userId, News news){

            this.wrapper = new UserNewsWrapper(userId, news, null);
        }

        @Override
        public void onClick(View view) {
            StarredNewsAsync starredNewsAsync = new StarredNewsAsync(AuthenticationAddress.STAR_NEWS, NewsFeedManager.this.activity);
            starredNewsAsync.execute(wrapper);
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    private void addEntries(String data) throws JSONException{

        Log.e("News data", data);

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

        Uri uri = data.getData();
        Cursor returnCursor;
        DataWrapper newsWrapper = null;

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

            try{
                image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }

             newsWrapper = new DataWrapper(data.getStringExtra("ADD_NEWS_CONTENT"),
                    imageFile, userData.getInt("USER_ID"), userData.getInt("USER_TYPE"), image);

        }else{
            newsWrapper = new DataWrapper(data.getStringExtra("ADD_NEWS_CONTENT"),
                    null, userData.getInt("USER_ID"), userData.getInt("USER_TYPE"), null);
        }

        AddNewsAsync addNewsAsync = new AddNewsAsync(AuthenticationAddress.ADD_NEWS, activity);
        addNewsAsync.execute(newsWrapper);

    }

    private class DataWrapper{

        private String content;
        private Bitmap image;
        private int authorId;
        private String imageFile;
        private int userType;

        public DataWrapper(String content, String imageFile, int authorId, int userType, Bitmap image){
            this.content = content;
            this.image = image;
            this.authorId = authorId;
            this.imageFile = imageFile;
            this.userType = userType;
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

        public int getUserType() {
            return userType;
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
                //edit this when switching to https
                HttpURLConnection urlConnection = super.getUrlConnection();

                if(wrapper[0].getImageFile() != null){

                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("Cache-Control", "no-cache");
                    urlConnection.setRequestProperty(
                            "Content-Type", "multipart/form-data;boundary=" + NewsFeedManager.this.boundary);

                    OutputStream out = urlConnection.getOutputStream();
                    DataOutputStream outputStream = new DataOutputStream(out);

                    // start writing the iamge to buffer
                    outputStream.writeBytes(NewsFeedManager.this.twoHyphens + NewsFeedManager.this.boundary + NewsFeedManager.this.crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                            "image" + "\";filename=\"" +
                            wrapper[0].getImageFile() + "\"" + NewsFeedManager.this.crlf);
                    outputStream.writeBytes(NewsFeedManager.this.crlf);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    wrapper[0].getImage().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    outputStream.write(byteArray);

                    outputStream.writeBytes(NewsFeedManager.this.crlf);
                    outputStream.writeBytes(NewsFeedManager.this.twoHyphens + NewsFeedManager.this.boundary +
                            NewsFeedManager.this.twoHyphens + NewsFeedManager.this.crlf);

                    // end writing the image

                    // authentication

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\""+AuthenticationKeys.IDENTIFICATION_KEY+"\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + AuthenticationKeys.IDENTIFICATION_VALUE + crlf);
                    //outputStream.writeBytes(crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\""+AuthenticationKeys.USERNAME_KEY+"\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + AuthenticationKeys.USERNAME_VALUE+ crlf);


                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\""+AuthenticationKeys.PASSWORD_KEY+"\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + AuthenticationKeys.PASSWORD_VALUE+ crlf);
                    // end authentication

                    // text data

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"content\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getContent() + crlf);
                    //outputStream.writeBytes(crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"image_file\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getImageFile() + crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"user_type\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getUserType()  + crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"author\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getAuthorId() + crlf);

                    outputStream.flush();
                    outputStream.close();
                    out.close();



                }else{
                    Uri.Builder queryBuilder = getQueryBuilder();
                    queryBuilder.appendQueryParameter("content", wrapper[0].getContent())
                            .appendQueryParameter("image", wrapper[0].getImageFile())
                            .appendQueryParameter("user_type", wrapper[0].getUserType() + "")
                            .appendQueryParameter("author", wrapper[0].getAuthorId() + "");

                    super.setRequest(getQueryBuilder().build().getEncodedQuery());
                }


                urlConnection.connect();
                String response = getRequest();

                //BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                //return reader.readLine() + " " + reader.readLine();
                return  response;

            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.e("Request", s);
        }
    }

    private class StarredNewsAsync extends RemoteDbConn<UserNewsWrapper, Void, UserNewsWrapper>{

        public StarredNewsAsync(String urlAddress, Activity activity){
            super(urlAddress, activity);
        }

        @Override
        protected UserNewsWrapper doInBackground(UserNewsWrapper... wrappers) {

            try{

                super.setUpConnection();
                Uri.Builder queryBuilder = super.getQueryBuilder();

                if(wrappers[0].news.isStarred()){
                    queryBuilder.appendQueryParameter("action", "remove")
                            .appendQueryParameter("news", wrappers[0].news.getId() + "")
                            .appendQueryParameter("user", wrappers[0].userId + "");
                }else{
                    queryBuilder.appendQueryParameter("action", "add")
                            .appendQueryParameter("news", wrappers[0].news.getId() + "")
                            .appendQueryParameter("user", wrappers[0].userId + "");
                }

                String query = queryBuilder.build().getEncodedQuery();

                super.setRequest(query);
                super.getUrlConnection().connect();

                String response = super.getRequest();

                Bundle extraData = new Bundle();
                extraData.putString("response", response);
                wrappers[0].setExtraData(extraData);

                return wrappers[0];
            }catch(IOException e){
                e.printStackTrace();

                return null;
            }
        }

        @Override
        protected void onPostExecute(UserNewsWrapper wrapper) {

            if(wrapper == null){
                Log.e("NewsFeedManager", "StarredNewsAsync - wrapper is null");
                return;
            }

            String response = wrapper.getExtraData().getString("response");

            if(response != null){

                try{

                    JSONObject json = new JSONObject(response);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }




        }
    }

}