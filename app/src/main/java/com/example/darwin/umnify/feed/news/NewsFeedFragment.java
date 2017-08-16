package com.example.darwin.umnify.feed.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;


public class NewsFeedFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsFeedManager manager;

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

        manager = new NewsFeedManager(this.getActivity(), swipeRefreshLayout, recyclerView, userData);

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

        Bundle args = super.getArguments();

        manager.addNews(data, args);
    }

}