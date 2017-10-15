package com.example.darwin.umnify.feed.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.news.feed_manager.NewsFeedManagerAdmin;
import com.example.darwin.umnify.feed.news.feed_manager.NewsFeedManagerGuest;
import com.example.darwin.umnify.feed.news.feed_manager.NewsFeedManagerNormal;
import com.example.darwin.umnify.feed.news.feed_manager.NewsFeedManagerSuperAdmin;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderAdmin;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderGuest;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderNormal;


public class NewsFeedFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    //private NewsFeedManager manager;
    //private NewsFeedManagerGuest<NewsViewHolderGuest> manager;
    private FeedManager manager;

    private Bundle userData;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        userData = super.getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.feed_view, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresher_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        if(userData == null){
            manager = new NewsFeedManagerGuest<NewsViewHolderGuest>(this.getActivity(), swipeRefreshLayout, recyclerView,
                    NewsViewHolderGuest.class, R.layout.feed_news_guest);
        }else{

            int type = userData.getInt("USER_TYPE");

            if(type == AuthenticationCodes.NORMAL_USER){
                manager = new NewsFeedManagerNormal<NewsViewHolderNormal>(this.getActivity(), swipeRefreshLayout, recyclerView, userData,
                        NewsViewHolderNormal.class, R.layout.feed_news_normal);
            }else if(type == AuthenticationCodes.ADMIN_USER){
                manager = new NewsFeedManagerAdmin<NewsViewHolderAdmin>(this.getActivity(), swipeRefreshLayout, recyclerView, userData,
                        NewsViewHolderAdmin.class, R.layout.feed_news_admin);
            }else if(type == AuthenticationCodes.SUPER_ADMIN_USER){
                manager = new NewsFeedManagerSuperAdmin<NewsViewHolderAdmin>(this.getActivity(), swipeRefreshLayout, recyclerView, userData,
                        NewsViewHolderAdmin.class, R.layout.feed_news_admin);
            }
        }

        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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

        return view;
    }

    public void addNews(Intent data){

        manager.newFeedEntry(data);
    }

    public void deleteNews(Intent data){

        String key = Integer.toString(data.getIntExtra("INDEX", -1));
        manager.deleteFeedEntry(key);
    }

    public void updateNews(Intent data){
        manager.updateFeedContent(data);
    }

}