package com.example.darwin.umnify.gallery.news;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.darwin.umnify.PostResultAction;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.gallery.news.data_action_wrapper.FetchNewsImageFileDataActionWrapper;
import com.example.darwin.umnify.gallery.news.feed_manager.GalleryNewsFeedManager;
import com.example.darwin.umnify.gallery.view_holder.GalleryViewHolder;

import org.json.JSONException;

import java.util.HashMap;

public class GalleryNewsActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FeedManager manager;

    private ImageButton toolbarBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_news);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresher_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        toolbarBackButton = (ImageButton) findViewById(R.id.back);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        manager = new GalleryNewsFeedManager(this, swipeRefreshLayout,
                GalleryViewHolder.class);

        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(dy > 0){
                    if(!recyclerView.canScrollVertically(1)){
                        manager.updateFeed(1);
                    }
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                manager.updateFeed(-1);

            }
        });
    }

}
