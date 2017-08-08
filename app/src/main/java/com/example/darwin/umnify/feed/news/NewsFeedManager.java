package com.example.darwin.umnify.feed.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
    private NewsFeedAsyc newsHandler;

    private boolean isFetching = false;

    public NewsFeedManager(Context context, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView) {

        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;

        feedList = new ArrayList<News>();

        newsHandler = new NewsFeedAsyc(1);
        isFetching = true;
        newsHandler.execute("desc", feedList.size() + "", "7", "-1");

    }

    /* Template for the View */

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

    //refactor this two methods

    private void addEntries(String data) throws JSONException{

        JSONArray dataList = new JSONArray(data);
        int temp = feedList.size();

        for(int i = 0; i < dataList.length(); i++){

            JSONObject newsData = new JSONObject(dataList.getString(i));
            News news = new News(newsData, feedList.size(), this);
            feedList.add(news);

        }
        notifyItemRangeInserted(temp, dataList.length());

        for(int i = temp; i < feedList.size(); i++){
            feedList.get(i).fetchImage();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void addEntriesReverse(String data) throws  JSONException{

        JSONArray dataList = new JSONArray(data);

        for(int i = (dataList.length() - 1); i >= 0; i--){

            //JSONObject newsData = new JSONObject(dataList.getString(i));
            //News news = new News(newsData);
            //feedList.add(0, news);

        }

        notifyItemRangeInserted(0, dataList.length());
        recyclerView.scrollToPosition(0);
        swipeRefreshLayout.setRefreshing(false);


    }

    public void updateFeed(int direction){

        if(isFetching) return;

        if(direction == 1){

            newsHandler = new NewsFeedAsyc(1);
            isFetching = true;
            newsHandler.execute("desc", feedList.size() + "", "3", "-1");

        }else if(direction == -1){

            feedList.clear();
            notifyDataSetChanged();

            newsHandler = new NewsFeedAsyc(1);
            isFetching = true;
            newsHandler.execute("desc", 0 + "", "7", "-1");
        }
    }

    //refactor this two classes

    private class NewsFeedAsyc extends AsyncTask<String, Void, String> {

        private final String urlAddress;
        private final int READ_TIMEOUT = 10000;
        private final int CONNECT_TIMEOUT = 15000;
        private final String REQUEST_METHOD = "POST";

        private URL url;
        private HttpURLConnection urlConnection;
        private Uri.Builder builder;

        private final int DIRECTION;


        public NewsFeedAsyc(int DIRECTION){

            urlAddress = "http://192.168.0.100/~darwin/UMnifyMobileScripts/feed/news/fetch_news.php";
            this.DIRECTION = DIRECTION;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{

                setUpConnection();

                builder = new Uri.Builder()
                        .appendQueryParameter(AuthenticationKeys.IDENTIFICATION_KEY, AuthenticationKeys.IDENTIFICATION_VALUE)
                        .appendQueryParameter(AuthenticationKeys.USERNAME_KEY, AuthenticationKeys.USERNAME_VALUE)
                        .appendQueryParameter(AuthenticationKeys.PASSWORD_KEY, AuthenticationKeys.PASSWORD_VALUE)
                        .appendQueryParameter("order", strings[0])
                        .appendQueryParameter("offset", strings[1])
                        .appendQueryParameter("limit", strings[2])
                        .appendQueryParameter("id", strings[3]);
                String query = builder.build().getEncodedQuery();

                setRequest(query);
                urlConnection.connect();

                String response = getResponse();

                return response;
            }catch (Exception e){

            }

            return null;
        }

        private void setUpConnection() throws IOException{

            url = new URL(urlAddress);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

        }

        private void setRequest(String query) throws IOException{

            OutputStream stream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(stream, "UTF-8"));

            writer.write(query);
            writer.flush();
            writer.close();
            stream.close();
        }

        private String getResponse() throws  IOException{

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return reader.readLine();

        }

        @Override
        protected void onPostExecute(String response) {

            try {
                JSONObject str = new JSONObject(response);
                String data = str.getString("data");

               if(DIRECTION == 1)
                    addEntries(data);
                else if(DIRECTION == -1)
                    addEntriesReverse(data);

                isFetching = false;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}