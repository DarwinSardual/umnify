package com.example.darwin.umnify.feed.news.view_holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.darwin.umnify.R;

/**
 * Created by NS-Darwin on 8/25/2017.
 */

public class NewsViewHolderNormal extends NewsViewHolderGuest {

    private ImageButton newsStarButton;
    private ImageButton newsCommentButton;


    public NewsViewHolderNormal(LayoutInflater inflater, ViewGroup parent, int layoutId){
        super(inflater, parent, layoutId);

        newsStarButton = (ImageButton) itemView.findViewById(R.id.news_stars);
        newsCommentButton = (ImageButton) itemView.findViewById(R.id.news_comment);
    }

    public ImageButton getNewsStarButton() {
        return newsStarButton;
    }

    public ImageButton getNewsCommentButton() {
        return newsCommentButton;
    }
}
