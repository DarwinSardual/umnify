package com.example.darwin.umnify.gallery.blog;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.darwin.umnify.PostResultAction;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.gallery.blog.feed_manager.GalleryBlogFeedManager;
import com.example.darwin.umnify.gallery.news.data_action_wrapper.FetchNewsImageFileDataActionWrapper;
import com.example.darwin.umnify.gallery.news.feed_manager.GalleryNewsFeedManager;
import com.example.darwin.umnify.gallery.view_holder.GalleryViewHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class GalleryBlogActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FeedManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_blog);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresher_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        manager = new GalleryBlogFeedManager(this, swipeRefreshLayout,
                GalleryViewHolder.class);

        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);
    }


}
