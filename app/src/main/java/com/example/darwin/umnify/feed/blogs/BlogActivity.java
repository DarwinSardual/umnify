package com.example.darwin.umnify.feed.blogs;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.darwin.umnify.R;

public class BlogActivity extends AppCompatActivity {

    private BlogTile blogTile;
    private Bundle extraData;
    private Blog blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        extraData = getIntent().getExtras();

        TextView text = (TextView) findViewById(R.id.blog_activity_heading);
        text.setText(extraData.getInt("BLOG_TILE_ID") + "");

        blogTile = new BlogTile(extraData.getInt("BLOG_TILE_ID"), extraData.getString("BLOG_TILE_HEADING"), null);

    }

    private class BlogActivityAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
