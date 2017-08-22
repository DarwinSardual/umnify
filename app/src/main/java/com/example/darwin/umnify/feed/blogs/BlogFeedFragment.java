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

public class BlogFeedFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BlogFeedManager manager;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.feed_view, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresher_layout);

        manager = new BlogFeedManager(getActivity(), swipeRefreshLayout, recyclerView);
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

        Bundle args = super.getArguments();
        manager.addBlog(data, args);


    }

}