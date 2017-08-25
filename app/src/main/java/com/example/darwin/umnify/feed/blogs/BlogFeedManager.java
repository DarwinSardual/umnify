package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;

import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.AddBlogDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogImageDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogTileDataActionWrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//public class BlogFeedManager extends RecyclerView.Adapter<BlogFeedManager.ViewHolder>

public class BlogFeedManager extends FeedManager<BlogFeedManager.ViewHolder>{

    private List<BlogTile> feedList;
    private boolean isFetching = false;

    public BlogFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView){

        super(activity, swipeRefreshLayout);

        feedList = new ArrayList<>();

        HashMap<String, String> fetchBlogTextData = new HashMap<>();
        fetchBlogTextData.put("type", "tile");
        fetchBlogTextData.put("order", "desc");
        fetchBlogTextData.put("offset", feedList.size() +"");
        fetchBlogTextData.put("limit", "8");

        WebServiceAsync asyncFetchBlog = new WebServiceAsync();
        FetchBlogTileDataActionWrapper fetchBlogTileDataActionWrapper = new FetchBlogTileDataActionWrapper(fetchBlogTextData,
                activity, this);

        asyncFetchBlog.execute(fetchBlogTileDataActionWrapper);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder{

        private TextView blogTileHeadingView;
        private ImageView blogTileImageView;
        private RelativeLayout container;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.feed_blogs, parent, false));

            container = (RelativeLayout) itemView.findViewById(R.id.blog_tile_container);
            blogTileHeadingView = (TextView) itemView.findViewById(R.id.blog_tile_heading);
            blogTileImageView = (ImageView) itemView.findViewById(R.id.blog_tile_image);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position < feedList.size()){

            final BlogTile blogTile = feedList.get(position);
            holder.blogTileHeadingView.setText(blogTile.getHeading());
            holder.blogTileImageView.setImageBitmap(blogTile.getImage());

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("BLOG_TILE_ID", blogTile.getId());
                    intent.putExtra("BLOG_TILE_HEADING", blogTile.getHeading());
                    intent.putExtra("BLOG_TILE_IMAGE_FILE",blogTile.getImageFile());
                    intent.putExtra("BLOG_TILE_INDEX",blogTile.getIndex());

                    view.getContext().startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    @Override
    public void addFeedEntry(String jsonData)throws JSONException{

        FetchBlogImageDataActionWrapper fetchBlogImageDataActionWrapper;
        WebServiceAsync asyncFetchBlogImage;

        JSONObject blogData = new JSONObject(jsonData);
        BlogTile blogTile = BlogHelper.createBlogTileFromJSON(blogData, feedList.size());
        fetchBlogImageDataActionWrapper = new FetchBlogImageDataActionWrapper(blogTile,
                super.getActivity(), this);

        feedList.add(blogTile);
        notifyItemInserted(blogTile.getIndex());

        asyncFetchBlogImage = new WebServiceAsync();
        asyncFetchBlogImage.execute(fetchBlogImageDataActionWrapper);

    }

    @Override
    public void addFeedEntries(String data) throws JSONException{

        /* Everytime we add an entry we save it to the database
         * Save the image in the internal folder for caching */

        JSONArray dataList = new JSONArray(data);
        for(int i = 0; i < dataList.length(); i++){

            addFeedEntry(dataList.getString(i));

        }

        isFetching = false;
        super.getSwipeRefreshLayout().setRefreshing(false);
        dataList = null;
    }

    @Override
    public void updateFeed(int direction){

        if(isFetching) return;

        WebServiceAsync asyncFetchBlog = new WebServiceAsync();
        FetchBlogTileDataActionWrapper fetchBlogTileDataActionWrapper;
        HashMap<String, String> fetchBlogTextData = new HashMap<>();

        fetchBlogTextData.put("type", "tile");
        fetchBlogTextData.put("order", "desc");

        if(direction == 1){

            isFetching = true;

            fetchBlogTextData.put("offset", feedList.size() + "");
            String oddEven = feedList.size()/2 ==0? "2":"3";
            fetchBlogTextData.put("limit", oddEven);
            fetchBlogTileDataActionWrapper = new FetchBlogTileDataActionWrapper(fetchBlogTextData,
                    super.getActivity(), this);

            asyncFetchBlog.execute(fetchBlogTileDataActionWrapper);

        }else if(direction == -1){

            isFetching = true;
            feedList.clear();
            notifyDataSetChanged();

            fetchBlogTextData.put("offset", feedList.size() + "");
            fetchBlogTextData.put("limit", "8");

            fetchBlogTileDataActionWrapper = new FetchBlogTileDataActionWrapper(fetchBlogTextData,
                    super.getActivity(), this);

            asyncFetchBlog.execute(fetchBlogTileDataActionWrapper);

        }

        fetchBlogTextData = null;
        fetchBlogTileDataActionWrapper = null;
        asyncFetchBlog = null;

    }

    public void addBlog(Intent data, Bundle userData){

        Uri uri = data.getData();
        Cursor cursor;

        HashMap<String, String> textData = new HashMap<>();
        HashMap<String, byte[]> fileData = new HashMap<>();

        if(uri != null){

            cursor = super.getActivity().getContentResolver().query(uri,
                    null, null,
                    null, null);

            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();

            String imageFile = cursor.getString(nameIndex);
            Bitmap image = null;
            byte[] byteArray = null;

            try{
                image = MediaStore.Images.Media.getBitmap(super.getActivity().getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();

            }catch (IOException e){
                e.printStackTrace();
            }

            textData.put("heading", data.getStringExtra("ADD_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("ADD_BLOG_CONTENT"));
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");

            fileData.put(imageFile, byteArray);

        }else{
            textData.put("heading", data.getStringExtra("ADD_BLOG_HEADING"));
            textData.put("content", data.getStringExtra("ADD_BLOG_CONTENT"));
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");
        }

        AddBlogDataActionWrapper addBlogDataActionWrapper = new AddBlogDataActionWrapper(textData,
                fileData, super.getActivity());

        WebServiceAsync asyncAddBlog = new WebServiceAsync();
        asyncAddBlog.execute(addBlogDataActionWrapper);
    }
}