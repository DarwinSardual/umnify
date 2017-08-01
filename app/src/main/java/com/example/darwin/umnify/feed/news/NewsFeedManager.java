package com.example.darwin.umnify.feed.news;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.List;

public class NewsFeedManager extends RecyclerView.Adapter<NewsFeedManager.ViewHolder> {

    private int feedCount;
    private List<News> feedEntryList;
    private NewsFeedAsyc newsHandler;


    public NewsFeedManager(Context context) {

        feedCount = 0;

        feedEntryList = new ArrayList<News>();

        newsHandler = new NewsFeedAsyc();
        newsHandler.execute("5", "desc");

    }

    /* Template for the View */

    public final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView newsContentView;
        private ImageView newsImageView;
        private TextView newsAuthorView;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.feed_news, parent, false));

            newsContentView = (TextView) itemView.findViewById(R.id.news_content);
            newsAuthorView = (TextView) itemView.findViewById(R.id.news_author);
            newsImageView = (ImageView) itemView.findViewById(R.id.news_image);

            CardView card = (CardView) itemView.findViewById(R.id.card_view);

            newsContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("View", "Clicked content");
                }
            });

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("View", "Clicked Card");
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position < feedCount){

            News news = feedEntryList.get(position);
            holder.newsAuthorView.setText(news.getAuthor() + "");
            holder.newsContentView.setText(news.getContent());
        }

    }

    private void addEntries(String data) throws JSONException{

        JSONArray dataList = new JSONArray(data);

        for(int i = 0; i < dataList.length(); i++){

            JSONObject newsData = new JSONObject(dataList.getString(i));
            News news = new News(newsData);
            feedEntryList.add(news);
            notifyItemInserted(feedCount);
            feedCount++;

        }
    }

    private class NewsFeedAsyc extends AsyncTask<String, Void, String> {

        private final String urlAddress;
        private final int READ_TIMEOUT = 10000;
        private final int CONNECT_TIMEOUT = 15000;
        private final String REQUEST_METHOD = "POST";

        private URL url;
        private HttpURLConnection urlConnection;
        private Uri.Builder builder;


        public NewsFeedAsyc(){

            urlAddress = "http://192.168.0.100/~darwin/UMnifyMobileScripts/feed/news/fetch_news.php";
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
                        .appendQueryParameter("limit", strings[0])
                        .appendQueryParameter("order", strings[1]);
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

                addEntries(data);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return feedCount;
    }
}