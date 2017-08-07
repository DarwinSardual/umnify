package com.example.darwin.umnify.feed.blogs;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationKeys;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class BlogActivity extends AppCompatActivity {

    private BlogTile blogTile;
    private Bundle extraData;
    private Blog blog;

    private BlogActivityAsync blogActivityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        extraData = getIntent().getExtras();

        TextView text = (TextView) findViewById(R.id.blog_activity_heading);
        text.setText(extraData.getInt("BLOG_TILE_ID") + "");

        blogTile = new BlogTile(extraData.getInt("BLOG_TILE_ID"), extraData.getString("BLOG_TILE_HEADING"), null);

        Log.e("sdsdssds", blogTile.getId() + "");

        blogActivityHandler = new BlogActivityAsync();
        blogActivityHandler.execute("activity", blogTile.getId() + "");
    }

    private class BlogActivityAsync extends AsyncTask<String, Void, String>{

        private final String urlAddress;
        private final int READ_TIMEOUT = 10000;
        private final int CONNECT_TIMEOUT = 15000;
        private final String REQUEST_METHOD = "POST";

        private URL url;
        private HttpURLConnection urlConnection;
        private Uri.Builder builder;

        public BlogActivityAsync(){

            urlAddress = "http://192.168.0.100/~darwin/UMnifyMobileScripts/feed/blogs/fetch_blogs.php";
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
                        .appendQueryParameter("id", strings[1]);
                String query = builder.build().getEncodedQuery();

                setRequest(query);
                urlConnection.connect();

                String response = getResponse();

                return response;



            }catch (Exception e){
                e.printStackTrace();
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
            //super.onPostExecute(aVoid);

            //Log.e("Response", response);
        }

    }
}
