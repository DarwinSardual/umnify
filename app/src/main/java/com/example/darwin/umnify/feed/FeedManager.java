package com.example.darwin.umnify.feed;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.ViewGroup;

import com.example.darwin.umnify.LeastRecentlyUsedCache;
import com.example.darwin.umnify.feed.news.News;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public abstract class FeedManager<E extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<E> {

    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    //private List<T> feedList;
    //private LruCache<String, T> feedList;
    private LeastRecentlyUsedCache<String, T> feedCache;
    private boolean isFetchingFeedEntry = false;
    private RecyclerView recyclerView;

    public FeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout, int feedSize){

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.feedCache = new LeastRecentlyUsedCache<>(feedSize);
        //this.feedList = new LruCache<>(50);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    public void setOnRemoveFromCache(LeastRecentlyUsedCache.OnRemoveFromCache<String> onRemoveFromCache){
        feedCache.setOnRemoveFromCache(onRemoveFromCache);
    }

    public abstract void updateFeed(int direction);
    public abstract void addFeedEntry(String jsonData) throws JSONException;
    public abstract void addFeedEntries(String jsonDataArray) throws JSONException;
    public abstract void deleteFeedEntry(String key);

    public void removeFromFeedList(T item){
        //feedList.remove(item);
    }

    public void removeFromFeedList(String key){
        feedCache.remove(key);
    }

    public Activity getActivity() {
        return activity;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void addToFeedList(String pos, T feed){

        //feedList.add(feed);
        feedCache.put(pos,feed);
    }

    public void clearFeedList(){
        feedCache.evictAll();
    }

    /*public int getFeedListSize(){
        return feedList.size();
    }*/

    public int getFeedListSize(){
        return feedCache.getSize();
    }

    public T getEntryFromFeedList(String id){

        T t = feedCache.get(id);

        return t;
    }


    public abstract void newFeedEntry(Intent data);
    public abstract void updateFeedContent(Intent data);

    public void setFetchingFeedEntry(boolean fetching) {
        isFetchingFeedEntry = fetching;
    }

    public boolean isFetchingFeedEntry() {
        return isFetchingFeedEntry;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
