package com.example.darwin.umnify.feed.news.view_holder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 8/26/17.
 */

public class NewsViewHolderAdmin extends NewsViewHolderNormal {

    private Spinner newsOptionView;

    public NewsViewHolderAdmin(LayoutInflater inflater, ViewGroup parent, int layoutId){
        super(inflater, parent, layoutId);

        newsOptionView = (Spinner) itemView.findViewById(R.id.news_spinner);

    }

    public Spinner getNewsOptionView() {
        return newsOptionView;
    }

}
