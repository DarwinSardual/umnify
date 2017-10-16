package com.example.darwin.umnify.gallery.news.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.LeastRecentlyUsedCache;
import com.example.darwin.umnify.PostResultAction;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.gallery.ImageWrapper;
import com.example.darwin.umnify.gallery.ViewImageActivity;
import com.example.darwin.umnify.gallery.ViewImageListener;
import com.example.darwin.umnify.gallery.blog.data_action_wrapper.FetchBlogImageFileDataActionWrapper;
import com.example.darwin.umnify.gallery.news.data_action_wrapper.FetchNewsImageDataActionWrapper;
import com.example.darwin.umnify.gallery.news.data_action_wrapper.FetchNewsImageFileDataActionWrapper;
import com.example.darwin.umnify.gallery.view_holder.GalleryViewHolder;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by darwin on 9/2/17.
 */

public class GalleryNewsFeedManager<E extends GalleryViewHolder> extends FeedManager<E, ImageWrapper> {

    private Class<E> cls;
    private ArrayList<String> index;
    private int offset;

    public GalleryNewsFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                  Class<E> cls){
        super(activity, swipeRefreshLayout, 100);
        super.setOnRemoveFromCache(new RemoveFromCache());
        offset = 0;
        this.index = new ArrayList<>();
        this.cls = cls;


    updateFeed(-1);
    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {
        FetchNewsImageDataActionWrapper fetchNewsImageDataActionWrapper;
        WebServiceAsync async;

        JSONObject imageData = new JSONObject(jsonData);
        ImageWrapper wrapper = GalleryHelper.createImageWrapperFromJSON(imageData, super.getFeedListSize());


        if(wrapper.getImageFile() != null){
            async = new WebServiceAsync();
            fetchNewsImageDataActionWrapper = new FetchNewsImageDataActionWrapper(wrapper, super.getActivity(),
                    this);
            int position = index.size();
            String key = position + "";
            super.addToFeedList(key, wrapper);
            index.add(key);
            notifyItemInserted(position);
            async.execute(fetchNewsImageDataActionWrapper);
        }

    }

    @Override
    public void addFeedEntries(String jsonDataArray) throws JSONException {

        JSONArray dataList = new JSONArray(jsonDataArray);
        //skip index 0
        dataList.remove(0);
        for(int i = 0; i < dataList.length(); i++){

            addFeedEntry(dataList.getString(i));
        }

        dataList = null;
        super.setFetchingFeedEntry(false);
        super.getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void updateFeed(int direction) {

        if(super.isFetchingFeedEntry()) return;

        if(direction == 1){

            super.setFetchingFeedEntry(true);

            HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("offset", offset +"");
            textDataOutput.put("limit", 30 + "");
            WebServiceAsync async = new WebServiceAsync();
            WebServiceAction action =
                    new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.FETCH_NEWS_IMAGE_FILE, new ProcessPostFetchNewsImageFile());

            async.execute(action);

        }else if(direction == -1){

            super.clearFeedList();
            notifyDataSetChanged();
            offset = 0;
            super.setFetchingFeedEntry(true);

            HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("offset", offset +"");
            textDataOutput.put("limit", 30 + "");

            WebServiceAsync async = new WebServiceAsync();
            WebServiceAction action =
                    new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.FETCH_NEWS_IMAGE_FILE, new ProcessPostFetchNewsImageFile());

            async.execute(action);
        }
    }

    @Override
    public void newFeedEntry(Intent data) {
        // dummy
    }

    @Override
    public void deleteFeedEntry(String key) {
        // dummy
    }

    @Override
    public void updateFeedContent(Intent data) {

    }

    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType) {

        return createViewHolder(parent);

    }

    private E createViewHolder(ViewGroup parent){
        E viewHolder = null;
        try{
            viewHolder=  cls.getConstructor(LayoutInflater.class, ViewGroup.class).newInstance(LayoutInflater.from(parent.getContext()), parent);
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final E holder, int position) {
        if(!(position < index.size())) return;

        String key = index.get(position);
        final ImageWrapper wrapper = super.getEntryFromFeedList(key);

        if(wrapper != null){

            holder.getImageView().setImageBitmap(wrapper.getImage());



            Bundle bundle = new Bundle();
            bundle.putString("FOLDER", "feed/news");
            bundle.putString("IMAGE_FILE", wrapper.getImageFile());
            bundle.putString("ROOT_LOCATION", AuthenticationAddress.NEWS_IMAGE_FOLDER);
                holder.getImageView().setOnClickListener(new ViewImageListener(bundle, GalleryNewsFeedManager.this.getActivity()));
        }
    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }

    private class ProcessPostFetchNewsImageFile implements PostAsyncAction  {

        @Override
        public void processResult(String jsonResponse){

            if(jsonResponse != null){

                try{
                    JSONObject json = new JSONObject(jsonResponse);
                    String data = json.getString("data");
                    addFeedEntries(data);
                    setFetchingFeedEntry(false);
                    getSwipeRefreshLayout().setRefreshing(false);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }
    }

    private class RemoveFromCache implements LeastRecentlyUsedCache.OnRemoveFromCache{

        @Override
        public void onRemove(Object key) {
            String k = (String) key;
            int position = index.indexOf(k);
            index.remove(position);
            notifyItemRemoved(position);
        }
    }
}
