package com.example.darwin.umnify.feed.blogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.blogs.feed_manager.BlogFeedManagerAdmin;
import com.example.darwin.umnify.feed.blogs.feed_manager.BlogFeedManagerGuest;
import com.example.darwin.umnify.feed.blogs.feed_manager.BlogFeedManagerSuperAdmin;
import com.example.darwin.umnify.feed.blogs.view_holder.BlogTileViewHolderGuest;

public class BlogFeedFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    //private BlogFeedManager manager;
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

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresher_layout);

        //manager = new BlogFeedManager(getActivity(), swipeRefreshLayout, recyclerView);


        if(userData == null){
            manager = new BlogFeedManagerGuest<BlogTileViewHolderGuest>(getActivity(), swipeRefreshLayout,
                    BlogTileViewHolderGuest.class, R.layout.feed_blog_tile_guest);
        }else{
            int type = userData.getInt("USER_TYPE");

            if(type == AuthenticationCodes.NORMAL_USER){
                manager = new BlogFeedManagerGuest<BlogTileViewHolderGuest>(getActivity(), swipeRefreshLayout,
                        BlogTileViewHolderGuest.class, R.layout.feed_blog_tile_guest);
            }else if(type == AuthenticationCodes.ADMIN_USER){
                manager = new BlogFeedManagerAdmin<BlogTileViewHolderGuest>(getActivity(), swipeRefreshLayout,
                BlogTileViewHolderGuest.class, R.layout.feed_blog_tile_guest, userData);
            }else if(type == AuthenticationCodes.SUPER_ADMIN_USER){
                manager = new BlogFeedManagerSuperAdmin<BlogTileViewHolderGuest>(getActivity(), swipeRefreshLayout,
                        BlogTileViewHolderGuest.class, R.layout.feed_blog_tile_guest, userData);
            }
        }


        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

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

    public void addBlog(Intent data){

        manager.newFeedEntry(data);
    }
}