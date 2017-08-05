package com.example.darwin.umnify.feed.news;

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


public class NewsFeedFragment extends Fragment{

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



        //recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        View view = inflater.inflate(R.layout.recycler_view, container, false);
        SwipeRefreshLayout layout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);


        final NewsFeedManager manager = new NewsFeedManager(getActivity());
        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if(!recyclerView.canScrollVertically(1)){
                    manager.updateFeed(1);
                }else if(!recyclerView.canScrollVertically(-1)){
                    //manager.updateFeed(-1);
                }
            }
        });


        return view;


    }

}