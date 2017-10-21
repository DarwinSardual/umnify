package com.example.darwin.umnify.gallery.blog.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.PostResultAction;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.gallery.ImageWrapper;
import com.example.darwin.umnify.gallery.ViewImageListener;
import com.example.darwin.umnify.gallery.blog.data_action_wrapper.FetchBlogImageDataActionWrapper;
import com.example.darwin.umnify.gallery.blog.data_action_wrapper.FetchBlogImageFileDataActionWrapper;
import com.example.darwin.umnify.gallery.news.feed_manager.GalleryNewsFeedManager;
import com.example.darwin.umnify.gallery.view_holder.GalleryViewHolder;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by darwin on 9/3/17.
 */

public class GalleryBlogFeedManager<E extends GalleryViewHolder> extends FeedManager<E, ImageWrapper> {

    private Class<E> cls;

    public GalleryBlogFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                  Class<E> cls){

        super(activity, swipeRefreshLayout, 100);
        this.cls = cls;

        updateFeed(-1);

    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {
        FetchBlogImageDataActionWrapper fetchBlogImageDataActionWrapper;
        WebServiceAsync async;

        JSONObject imageData = new JSONObject(jsonData);
        ImageWrapper wrapper = GalleryHelper.createImageWrapperFromJSON(imageData, super.getFeedListSize());

        if(wrapper.getImageFile() != null){

            async = new WebServiceAsync();
            fetchBlogImageDataActionWrapper = new FetchBlogImageDataActionWrapper(wrapper, super.getActivity(),
                    this);
            super.addToFeedList(super.getFeedListSize() + "", wrapper);
            notifyItemInserted(wrapper.getIndex());
            async.execute(fetchBlogImageDataActionWrapper);
        }
    }

    @Override
    public void addFeedEntries(String jsonDataArray) throws JSONException {

        JSONArray dataList = new JSONArray(jsonDataArray);
        //remove first
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
            textDataOutput.put("offset", super.getFeedListSize() +"");
            textDataOutput.put("limit", 5 + "");
            WebServiceAsync async = new WebServiceAsync();
            WebServiceAction action =
                    new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.FETCH_BLOG_IMAGE_FILE, new ProcessPostFetchImageFile());

            async.execute(action);

        }else if(direction == -1){

            super.clearFeedList();
            notifyDataSetChanged();
            super.setFetchingFeedEntry(true);

            HashMap<String, String> textDataOutput = new HashMap<>();
            textDataOutput.put("offset", 0 +"");
            textDataOutput.put("limit", 30 + "");

            WebServiceAsync async = new WebServiceAsync();
            WebServiceAction action =
                    new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.FETCH_BLOG_IMAGE_FILE, new ProcessPostFetchImageFile());

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
    public void onBindViewHolder(E holder, int position) {

        ImageWrapper wrapper = super.getEntryFromFeedList(position + "");


        if(wrapper != null){

            holder.getImageView().setImageBitmap(wrapper.getImage());

            if(wrapper.getImage() != null){

                GalleryHelper.saveImageToInternal(wrapper.getImage(),
                        wrapper.getImageFile(), GalleryBlogFeedManager.this.getActivity(), "blog");

                Bundle bundle = new Bundle();
                bundle.putString("FOLDER", "feed/blog");
                bundle.putString("IMAGE_FILE", wrapper.getImageFile());
                bundle.putString("ROOT_LOCATION", AuthenticationAddress.BLOG_IMAGE_FOLDER);

                holder.getImageView().setOnClickListener(new ViewImageListener(bundle, GalleryBlogFeedManager.this.getActivity()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }

    private class ProcessPostFetchImageFile implements PostAsyncAction{

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



}

