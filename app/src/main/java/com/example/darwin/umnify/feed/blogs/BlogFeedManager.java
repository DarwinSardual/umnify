package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.List;

public class BlogFeedManager extends RecyclerView.Adapter<BlogFeedManager.ViewHolder>{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private List<BlogTile> feedList;
    private BlogFeedAsync blogHandler;

    private boolean isFetching = false;

    private Activity activity;

    public BlogFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView){

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;

        feedList = new ArrayList<>();

        blogHandler = new BlogFeedAsync(AuthenticationAddress.FETCH_BLOGS);
        isFetching = true;
        blogHandler.execute("tile", "desc", feedList.size() + "", "6");

    }

    public final class ViewHolder extends RecyclerView.ViewHolder{

        private TextView blogTileHeadingView;
        private ImageView blogTileImageView;
        private RelativeLayout container;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.feed_blogs, parent, false));

            container = (RelativeLayout) itemView.findViewById(R.id.blog_tile_container);
            blogTileHeadingView = (TextView) itemView.findViewById(R.id.blog_tile_heading);
            blogTileImageView = (ImageView) itemView.findViewById(R.id.blog_tile_image);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position < feedList.size()){

            final BlogTile blogTile = feedList.get(position);
            holder.blogTileHeadingView.setText(blogTile.getHeading());

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("BLOG_TILE_ID", blogTile.getId());
                    intent.putExtra("BLOG_TILE_HEADING", blogTile.getHeading());
                    intent.putExtra("BLOG_TILE_IMAGE","");

                    view.getContext().startActivity(intent);
                }
            });

        }
    }

    private void addEntries(String data) throws JSONException{

        JSONArray dataList = new JSONArray(data);
        int temp = feedList.size();

        for(int i = 0; i < dataList.length(); i++){

            JSONObject blogData = new JSONObject(dataList.getString(i));
            BlogTile blogTile = new BlogTile(blogData);
            feedList.add(blogTile);
        }

        notifyItemRangeInserted(temp, dataList.length());
        swipeRefreshLayout.setRefreshing(false);
    }

    public void updateFeed(int direction){

        if(isFetching) return;

        if(direction == 1){

            blogHandler = new BlogFeedAsync(AuthenticationAddress.FETCH_BLOGS);
            String temp = feedList.size()/2 ==0? "2":"3";
            isFetching = true;
            blogHandler.execute("tile", "desc", feedList.size() + "", temp);
        }else if(direction == -1){

        }

    }

    private class BlogFeedAsync extends RemoteDbConn<String, Void, String>{

        public BlogFeedAsync(String urlAdress){
            super(urlAdress, BlogFeedManager.this.activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{

                setUpConnection();
                Uri.Builder queryBuilder = super.getQueryBuilder();
                queryBuilder.appendQueryParameter("type", strings[0])
                        .appendQueryParameter("order", strings[1])
                        .appendQueryParameter("offset", strings[2])
                        .appendQueryParameter("limit", strings[3]);

                super.setRequest(queryBuilder.build().getEncodedQuery());
                super.getUrlConnection().connect();

                String response = super.getRequest();

                return response;
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            //super.onPostExecute(s);

            try {
                JSONObject str = new JSONObject(response);
                String data = str.getString("data");

                Log.e("response", response);

               // if(DIRECTION == 1)
                    addEntries(data);
                //else if(DIRECTION == -1);

                isFetching = false;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*private class BlogFeedAsyc extends AsyncTask<String, Void, String>{

        private final String urlAddress;
        private final int READ_TIMEOUT = 10000;
        private final int CONNECT_TIMEOUT = 15000;
        private final String REQUEST_METHOD = "POST";

        private URL url;
        private HttpURLConnection urlConnection;
        private Uri.Builder builder;

        private final int DIRECTION;

        public BlogFeedAsyc(int DIRECTION){

            urlAddress = "http://192.168.0.100/~darwin/UMnifyMobileScripts/feed/blogs/fetch_blogs.php";
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
                        .appendQueryParameter("type", strings[0])
                        .appendQueryParameter("order", strings[1])
                        .appendQueryParameter("offset", strings[2])
                        .appendQueryParameter("limit", strings[3]);
                String query = builder.build().getEncodedQuery();

                setRequest(query);
                urlConnection.connect();

                String response = getResponse();

                return response;

            }catch (Exception e){

            }

            return null;
        }

        private void setUpConnection() throws IOException {

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

                Log.e("response", response);

                if(DIRECTION == 1)
                    addEntries(data);
                else if(DIRECTION == -1);

                isFetching = false;
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }*/

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}