package com.example.darwin.umnify.feed.blogs;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationKeys;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogDataActionWrapper;
import com.example.darwin.umnify.scratch.CollapsingToolbar;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class BlogActivity extends AppCompatActivity {

    private BlogTile blogTile;
    private Bundle extraData;
    private Blog blog;

    private int id;
    private String heading;
    private String imageFile;

    private ImageView featuredImageView;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView contentView;

    /* When caching is already on work, always check first on the cache if the data
    * is already cached, if yes and its up to date, just fetch on the cache
    * else fetch the updated data, replace the data in the cache and save it to the db*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        extraData = getIntent().getExtras();


        id = extraData.getInt("BLOG_TILE_ID");
        heading = extraData.getString("BLOG_TILE_HEADING");
        imageFile = extraData.getString("BLOG_TILE_IMAGE_FILE");

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.blog_collapsing_toolbar);
        featuredImageView = (ImageView) findViewById(R.id.blog_featured_image);
        contentView = (TextView) findViewById(R.id.blog_content);

        WebServiceAsync async = new WebServiceAsync();
        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", id + "");
        textDataOutput.put("type", "activity");


        FetchBlogDataActionWrapper fetchBlogDataActionWrapper = new FetchBlogDataActionWrapper(id, heading, imageFile,
                textDataOutput, this);

        async.execute(fetchBlogDataActionWrapper);
    }

    public CollapsingToolbarLayout getToolbarLayout() {
        return toolbarLayout;
    }

    public TextView getContentView() {
        return contentView;
    }

    public ImageView getFeaturedImageView() {
        return featuredImageView;
    }
}
