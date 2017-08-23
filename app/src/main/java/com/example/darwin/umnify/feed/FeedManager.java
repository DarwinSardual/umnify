package com.example.darwin.umnify.feed;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import org.json.JSONException;

public abstract class FeedManager<E extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<E> {

    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;


    public FeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                       RecyclerView recyclerView){

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;
    }

    public abstract void updateFeed(int direction);
    public abstract void addFeedEntry(String jsonData) throws JSONException;
    public abstract void addFeedEntries(String jsonDataArray) throws JSONException;

    public Activity getActivity() {
        return activity;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
