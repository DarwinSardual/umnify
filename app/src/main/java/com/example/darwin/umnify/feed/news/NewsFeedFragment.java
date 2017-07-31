package com.example.darwin.umnify.feed.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.darwin.umnify.R;


public class NewsFeedFragment extends Fragment{

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);



        NewsFeedEntryManager manager = new NewsFeedEntryManager(getActivity());
        recyclerView.setAdapter(manager);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
               // Log.e("Scrolled", recyclerView.get + "");
            }
        });


        return recyclerView;


    }

}