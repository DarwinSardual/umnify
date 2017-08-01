package com.example.darwin.umnify.feed.blogs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.R;

public class BlogFeedFragment extends Fragment{

    private RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        BlogFeedManager manager = new BlogFeedManager(getActivity());
        recyclerView.setAdapter(manager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return recyclerView;
    }


}