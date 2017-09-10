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
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.authentication.AuthenticationKeys;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogDataActionWrapper;

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

        Log.e("Blog Activity", "called");


        extraData = getIntent().getExtras();


        id = extraData.getInt("BLOG_TILE_ID");
        heading = extraData.getString("BLOG_TILE_HEADING");
        imageFile = extraData.getString("BLOG_TILE_IMAGE_FILE");

        if(extraData.containsKey("USER_TYPE")){
            int type = extraData.getInt("USER_TYPE");
            if(type == AuthenticationCodes.ADMIN_USER){
                setContentView(R.layout.activity_blog_admin);
                BlogActivityControllerAdmin blogActivityControllerAdmin = new BlogActivityControllerAdmin(this, extraData);
            }else if(type == AuthenticationCodes.SUPER_ADMIN_USER){
                setContentView(R.layout.activity_blog_admin);
                BlogActivityControllerSuperAdmin blogActivityControllerSuperAdmin = new BlogActivityControllerSuperAdmin(this, extraData);
            }else if(type == AuthenticationCodes.NORMAL_USER){

            }
        }else{
            setContentView(R.layout.activity_blog_guest);
            BlogActivityControllerGuest blogActivityControllerGuest = new BlogActivityControllerGuest(this, extraData);
        }
    }

    public Bundle getExtraData() {
        return extraData;
    }
}
