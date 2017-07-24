package com.example.darwin.umnify.feed.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.example.darwin.umnify.R;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedEntryManager extends RecyclerView.Adapter<NewsFeedEntryManager.ViewHolder> {

    private int feedCount;
    private List<NewsFeedEntry> feedEntryList;


    public NewsFeedEntryManager(Context context) {

        //feedEntryList = new ArrayList<>();
        //feedEntryList.add(new NewsFeedEntry("This is a sample text", ContextCompat.getDrawable(context, R.drawable.employees)));


        feedCount = 0;

    }

    /* Template for the View */

    public final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView newsEntryTextView;
        private ImageView newsEntryImageView;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.feed_news, parent, false));

            newsEntryTextView = (TextView) itemView.findViewById(R.id.news_entry_text);
            newsEntryImageView = (ImageView) itemView.findViewById(R.id.news_entry_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (feedCount > 0) {


            NewsFeedEntry feed = feedEntryList.get(position % feedEntryList.size());

            holder.newsEntryTextView.setText(feed.getText());
            holder.newsEntryImageView.setImageDrawable(feed.getImageAt(0));
        }


    }

    @Override
    public int getItemCount() {
        return feedCount;
    }
}