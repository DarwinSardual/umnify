package com.example.darwin.umnify.pending;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.blogs.BlogCode;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.feed.news.NewsCode;
import com.example.darwin.umnify.pending.feed_manager.PendingFeedManagerSuperAdmin;
import com.example.darwin.umnify.pending.view_holder.PendingViewHolderSuperAdmin;

public class PendingActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Bundle userData;
    private FeedManager manager;
    private ImageButton backButon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        userData = getIntent().getExtras();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresher_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        manager = new PendingFeedManagerSuperAdmin<PendingViewHolderSuperAdmin>(this, swipeRefreshLayout, userData,PendingViewHolderSuperAdmin.class, R.layout.feed_pending);

        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);

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

        backButon = (ImageButton) findViewById(R.id.back);
        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PendingCode.VIEW_NEWS){
            if(resultCode == RESULT_OK){

                int action = data.getIntExtra("ACTION", -1);
                if(action == NewsCode.DELETE_NEWS){
                    String key = Integer.toString(data.getIntExtra("NEWS_ID", -1)) + "_" + "news";
                    manager.deleteFeedEntry(key);
                }else if(action == NewsCode.EDIT_NEWS){
                    data.putExtra("TYPE","news");
                    manager.updateFeedContent(data);
                }
            }
        }else if(requestCode == PendingCode.VIEW_BLOG){
            if(resultCode == RESULT_OK){

                int action = data.getIntExtra("ACTION", -1);
                if(action == BlogCode.DELETE_BLOG){
                    int id = data.getIntExtra("BLOG_ID", -1);
                    ((PendingFeedManagerSuperAdmin)manager).deleteFeedEntry(id + "_" + "blog");
                }else if(action == BlogCode.EDIT_BLOG){
                    data.putExtra("TYPE","blog");
                    manager.updateFeedContent(data);
                }
            }
        }
    }
}
