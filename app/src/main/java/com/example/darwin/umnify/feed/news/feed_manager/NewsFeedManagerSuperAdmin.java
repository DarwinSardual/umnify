package com.example.darwin.umnify.feed.news.feed_manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.feed.news.NewsOptionViewListener;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderAdmin;

/**
 * Created by darwin on 8/26/17.
 */

public class NewsFeedManagerSuperAdmin<E extends NewsViewHolderAdmin> extends NewsFeedManagerAdmin<E>{

    public NewsFeedManagerSuperAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                     Bundle userData, Class<E> cls, int layoutId){
        super(activity, swipeRefreshLayout, userData, cls, layoutId);
    }

    @Override
    public void onBindViewHolder(E holder, int position) {

        super.onBindViewHolder(holder, position);
        holder.getNewsOptionView().setEnabled(true);
        holder.getNewsOptionView().setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteFeedEntry(News news) {
       super.deleteFeedEntry(news);
    }
}
