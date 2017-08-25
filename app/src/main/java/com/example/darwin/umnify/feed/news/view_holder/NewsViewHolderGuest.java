package com.example.darwin.umnify.feed.news.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.R;

public class NewsViewHolderGuest extends RecyclerView.ViewHolder{

    private TextView newsContentView;
    private ImageView newsImageView;
    private TextView newsAuthorView;
    private ImageView newsAuthorImageView;


    public NewsViewHolderGuest(LayoutInflater inflater, ViewGroup parent, int layoutId){
        super(inflater.inflate(layoutId, parent, false));

        newsContentView = (TextView) itemView.findViewById(R.id.news_content);
        newsImageView = (ImageView) itemView.findViewById(R.id.news_image);
        newsAuthorView = (TextView) itemView.findViewById(R.id.news_author);
        newsAuthorImageView = (ImageView) itemView.findViewById(R.id.author_image);
    }

    public TextView getNewsContentView() {
        return newsContentView;
    }

    public ImageView getNewsImageView() {
        return newsImageView;
    }

    public TextView getNewsAuthorView() {
        return newsAuthorView;
    }

    public ImageView getNewsAuthorImageView() {
        return newsAuthorImageView;
    }
}
