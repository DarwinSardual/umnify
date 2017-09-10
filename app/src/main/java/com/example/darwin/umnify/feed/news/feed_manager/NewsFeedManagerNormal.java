package com.example.darwin.umnify.feed.news.feed_manager;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.feed.news.feed_manager.NewsFeedManagerGuest;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderNormal;

public class NewsFeedManagerNormal<E extends NewsViewHolderNormal> extends NewsFeedManagerGuest<E> {

    private Drawable emptyStar;
    private Drawable filledStar;
    private Bundle userData;

    public NewsFeedManagerNormal(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                Bundle userData, Class<E> cls, int layoutId){
        super(activity, swipeRefreshLayout, cls, layoutId);
        this.userData = userData;

        filledStar = ContextCompat.getDrawable(super.getActivity(), R.drawable.filled_star);
        emptyStar = ContextCompat.getDrawable(super.getActivity(), R.drawable.empty_star);
    }

    @Override
    public void onBindViewHolder(E holder, int position) {
        super.onBindViewHolder(holder, position);
        // check for star and comments
    }

    public Bundle getUserData() {
        return userData;
    }
}
