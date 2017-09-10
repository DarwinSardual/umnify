package com.example.darwin.umnify.feed.blogs.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.blogs.BlogHelper;
import com.example.darwin.umnify.feed.blogs.BlogTile;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogImageDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogTileDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.view_holder.BlogTileViewHolderGuest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by darwin on 8/26/17.
 */

public class BlogFeedManagerGuest<E extends BlogTileViewHolderGuest> extends FeedManager<E, BlogTile>{

    private Class<E> cls;
    private int layoutId;

    public BlogFeedManagerGuest(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                Class<E> cls, int layoutId){
        super(activity, swipeRefreshLayout);
        this.layoutId = layoutId;
        this.cls = cls;

        HashMap<String, String> fetchBlogTextData = new HashMap<>();
        fetchBlogTextData.put("type", "tile");
        fetchBlogTextData.put("order", "desc");
        fetchBlogTextData.put("offset", super.getFeedListSize() +"");
        fetchBlogTextData.put("limit", "8");

        WebServiceAsync asyncFetchBlog = new WebServiceAsync();
        FetchBlogTileDataActionWrapper fetchBlogTileDataActionWrapper = new FetchBlogTileDataActionWrapper(fetchBlogTextData,
                activity, this);

        asyncFetchBlog.execute(fetchBlogTileDataActionWrapper);
    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {

        FetchBlogImageDataActionWrapper fetchBlogImageDataActionWrapper;
        WebServiceAsync asyncFetchBlogImage;

        JSONObject blogData = new JSONObject(jsonData);
        BlogTile blogTile = BlogHelper.createBlogTileFromJSON(blogData, super.getFeedListSize());
        fetchBlogImageDataActionWrapper = new FetchBlogImageDataActionWrapper(blogTile,
                super.getActivity(), this);

        super.addToFeedList(super.getFeedListSize(), blogTile);
        notifyItemInserted(blogTile.getIndex());

        asyncFetchBlogImage = new WebServiceAsync();
        asyncFetchBlogImage.execute(fetchBlogImageDataActionWrapper);
    }

    @Override
    public void addFeedEntries(String jsonDataArray) throws JSONException {

        JSONArray dataList = new JSONArray(jsonDataArray);
        for(int i = 0; i < dataList.length(); i++){

            addFeedEntry(dataList.getString(i));

        }

        super.setFetchingFeedEntry(false);
        super.getSwipeRefreshLayout().setRefreshing(false);
        dataList = null;

    }

    @Override
    public void updateFeed(int direction) {

        if(super.isFetchingFeedEntry()) return;

        WebServiceAsync asyncFetchBlog = new WebServiceAsync();
        FetchBlogTileDataActionWrapper fetchBlogTileDataActionWrapper;
        HashMap<String, String> fetchBlogTextData = new HashMap<>();

        fetchBlogTextData.put("type", "tile");
        fetchBlogTextData.put("order", "desc");

        if(direction == 1){

            super.setFetchingFeedEntry(true);

            fetchBlogTextData.put("offset", super.getFeedListSize() + "");
            String oddEven = super.getFeedListSize()/2 ==0? "2":"3";
            fetchBlogTextData.put("limit", oddEven);
            fetchBlogTileDataActionWrapper = new FetchBlogTileDataActionWrapper(fetchBlogTextData,
                    super.getActivity(), this);

            asyncFetchBlog.execute(fetchBlogTileDataActionWrapper);

        }else if(direction == -1){

            super.setFetchingFeedEntry(true);
            super.clearFeedList();
            notifyDataSetChanged();

            fetchBlogTextData.put("offset", super.getFeedListSize() + "");
            fetchBlogTextData.put("limit", "8");

            fetchBlogTileDataActionWrapper = new FetchBlogTileDataActionWrapper(fetchBlogTextData,
                    super.getActivity(), this);

            asyncFetchBlog.execute(fetchBlogTileDataActionWrapper);

        }

        fetchBlogTextData = null;
        fetchBlogTileDataActionWrapper = null;
        asyncFetchBlog = null;
    }

    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(parent);
    }

    private E createViewHolder(ViewGroup parent){
        E viewHolder = null;
        try{
            viewHolder=  cls.getConstructor(LayoutInflater.class, ViewGroup.class, int.class).newInstance(LayoutInflater.from(parent.getContext()), parent, layoutId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(E holder, int position) {

        final BlogTile blogTile = super.getEntryFromFeedList(position);
        if(blogTile != null){
            holder.getBlogTileImageView().setImageBitmap(blogTile.getImage());
            holder.getBlogTileHeadingView().setText(blogTile.getHeading());

            holder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("BLOG_TILE_ID", blogTile.getId());
                    intent.putExtra("BLOG_TILE_HEADING", blogTile.getHeading());
                    intent.putExtra("BLOG_TILE_IMAGE_FILE",blogTile.getImageFile());
                    intent.putExtra("BLOG_TILE_INDEX",blogTile.getIndex());
                    intent.putExtra("BLOG_TILE_AUTHOR",blogTile.getAuthor());

                    view.getContext().startActivity(intent);
                }
            });

        }
    }

    @Override
    public void newFeedEntry(Intent data) {
        // dummy
    }

    @Override
    public void deleteFeedEntry(BlogTile tile) {

    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }
}
