package com.example.darwin.umnify.feed;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.ViewGroup;

import com.example.darwin.umnify.feed.news.News;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public abstract class FeedManager<E extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<E> {

    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    //private List<T> feedList;
    private LruCache<Integer, T> feedList;
    private boolean isFetchingFeedEntry = false;


    public FeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout){

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        //this.feedList = new ArrayList<>();
        this.feedList = new LruCache<>(50);
    }

    public abstract void updateFeed(int direction);
    public abstract void addFeedEntry(String jsonData) throws JSONException;
    public abstract void addFeedEntries(String jsonDataArray) throws JSONException;
    public abstract void deleteFeedEntry(T item);

    public void removeFromFeedList(T item){
        //feedList.remove(item);
    }

    public void removeFromFeedList(int pos){
        feedList.remove(pos);
    }

    public Activity getActivity() {
        return activity;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void addToFeedList(int pos, T feed){

        //feedList.add(feed);
        feedList.put(pos,feed);
    }

    public void clearFeedList(){
        feedList.evictAll();
    }

    public int getFeedListSize(){
        return feedList.size();
    }

    public T getEntryFromFeedList(int position){

        if(position >= feedList.size())
            return null;

        return feedList.get(position);
    }

    public abstract void newFeedEntry(Intent data);

    public void setFetchingFeedEntry(boolean fetching) {
        isFetchingFeedEntry = fetching;
    }

    public boolean isFetchingFeedEntry() {
        return isFetchingFeedEntry;
    }
}
