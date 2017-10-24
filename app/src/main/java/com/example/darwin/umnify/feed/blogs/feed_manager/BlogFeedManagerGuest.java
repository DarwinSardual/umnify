package com.example.darwin.umnify.feed.blogs.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.ImageActionWrapper;
import com.example.darwin.umnify.LeastRecentlyUsedCache;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.feed.blogs.Blog;
import com.example.darwin.umnify.feed.blogs.BlogActivity;
import com.example.darwin.umnify.feed.blogs.BlogHelper;
import com.example.darwin.umnify.feed.blogs.BlogCode;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.data_action_wrapper.FetchBlogImageDataActionWrapper;
import com.example.darwin.umnify.feed.blogs.view_holder.BlogTileViewHolderGuest;
import com.example.darwin.umnify.feed.news.feed_manager.NewsFeedManagerGuest;
import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by darwin on 8/26/17.
 */

public class BlogFeedManagerGuest<E extends BlogTileViewHolderGuest> extends FeedManager<E, Blog>{

    private Class<E> cls;
    private int layoutId;
    private SQLiteDatabase databaseRead;
    private UMnifyDbHelper databaseConnection;
    private boolean hasConnection = true;
    private ArrayList<String> index;
    private int offset;

    public final static int VIEW_BLOG_CODE = 3;

    public BlogFeedManagerGuest(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                Class<E> cls, int layoutId){
        super(activity, swipeRefreshLayout, 50);
        super.setOnRemoveFromCache(new RemoveFromCache());
        this.layoutId = layoutId;
        this.cls = cls;
        this.index = new ArrayList<>();
        databaseConnection = UMnifyDbHelper.getInstance(super.getActivity());
        databaseRead = databaseConnection.getReadableDatabase();
        offset = 0;

        updateFeed(-1);
    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {

        JSONObject blogData = new JSONObject(jsonData);
        Blog blog = BlogHelper.createBlogFromJSON(blogData, -1);
        BlogHelper.addBlogToLocalDb(blog, super.getActivity());

        int position = index.size();
        String key = Integer.toString(blog.getId());

        super.addToFeedList(key, blog);
        index.add(key);
        offset++;
        notifyItemInserted(position);

        Bitmap image = GalleryHelper.loadImageFromInternal(blog.getImageFile(), super.getActivity(), "feed/blog");
        if(image != null){
            Bitmap resizeImage = DataHelper.resizeImageAspectRatio(image, 512, 288);
            blog.setImage(resizeImage);
            notifyItemChanged(position);
        }else{
            WebServiceAction imageAction;
            WebServiceAsync async;

            PostAsyncImageAction postProcess =
                    new ProcessPostFetchImage(blog, position, super.getActivity());

            imageAction = new ImageActionWrapper(super.getActivity(), AuthenticationAddress.BLOG_IMAGE_FOLDER + "/preview/" + blog.getImageFile(), postProcess);
            async = new WebServiceAsync();
            async.execute(imageAction);
        }
    }

    public void addFeedEntry(Blog blog){

        Bitmap image = GalleryHelper.loadImageFromInternal(blog.getImageFile() , super.getActivity(), "feed/blog");
        if(image != null){
            Bitmap resizeImage = DataHelper.resizeImageAspectRatio(image, 512, 288);
            blog.setImage(resizeImage);
        }

        int position = index.size();
        String key = Integer.toString(blog.getId());

        super.addToFeedList(key, blog);
        index.add(key);
        offset++;
        notifyItemInserted(position);
    }

    @Override
    public void addFeedEntries(String jsonDataArray) throws JSONException {

        if(jsonDataArray != null){
            JSONArray dataList = new JSONArray(jsonDataArray);
            for(int i = 0; i < dataList.length(); i++){

                addFeedEntry(dataList.getString(i));
            }
            dataList = null;
        }else{

            hasConnection = false;
            Cursor cursor;

            if(super.getFeedListSize() > 0){
                String query = "select * from Blog where  published_date < ? and id != ? order by datetime(published_date) desc limit 3";
                //String query = "select * from Blog where id < ? order by datetime(published_date) desc limit 3";
                String id = index.get(index.size() - 1);
                Blog blog = super.getEntryFromFeedList(id);

                String date = blog.getPublishedDate();
                String idKey = Integer.toString(blog.getId());

                String[] selectionArgs = {date, idKey};
                cursor = databaseRead.rawQuery(query, selectionArgs);

            }else{
                String query = "select * from Blog order by datetime(published_date) desc limit 8";
                cursor = databaseRead.rawQuery(query, null);
            }


            while(cursor.moveToNext()){

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Blog.ID.toString()));

                if(index.indexOf(id) > 0) continue;

                String heading = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Blog.HEADING.toString()));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Blog.CONTENT.toString()));
                String imageFile = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Blog.IMAGE.toString()));
                int author = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Blog.AUTHOR.toString()));
                String createdDate = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Blog.CREATED_DATE.toString()));
                String publishedDate = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Blog.PUBLISHED_DATE.toString()));
                int signature = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Blog.SIGNATURE.toString()));

                String authorFirstname = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.AUTHOR_FIRSTNAME.toString()));
                String authorLastname = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.AUTHOR_LASTNAME.toString()));
                String authorImageFile = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.AUTHOR_IMAGE.toString()));

                Blog blog = new Blog(id, heading, content, author, createdDate, publishedDate, imageFile, null, signature,
                        super.getFeedListSize(), authorFirstname, authorLastname, authorImageFile);

                addFeedEntry(blog);

            }
        }


        super.setFetchingFeedEntry(false);
        super.getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void updateFeed(int direction) {

        if(super.isFetchingFeedEntry()) return;

        WebServiceAsync async = new WebServiceAsync();
        WebServiceAction action;
        HashMap<String, String> fetchBlogTextData = new HashMap<>();

        fetchBlogTextData.put("type", "tile");
        fetchBlogTextData.put("order", "desc");

        if(direction == 1){

            super.setFetchingFeedEntry(true);

            if(hasConnection){

                fetchBlogTextData.put("offset", offset + "");
                String oddEven = super.getFeedListSize()/2 ==0? "2":"3";
                fetchBlogTextData.put("limit", oddEven);

                action = new DataActionWrapper(fetchBlogTextData,
                        super.getActivity(), AuthenticationAddress.FETCH_BLOG, new ProcessPostFetchData());
                async.execute(action);

            }else{
                try{
                    addFeedEntries(null);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }else if(direction == -1){


            index.clear();
            super.clearFeedList();
            offset = 0;
            notifyDataSetChanged();
            super.setFetchingFeedEntry(true);

            fetchBlogTextData.put("offset", offset + "");
            fetchBlogTextData.put("limit", "8");

            action = new DataActionWrapper(fetchBlogTextData,
                    super.getActivity(), AuthenticationAddress.FETCH_BLOG, new ProcessPostFetchData());

            async.execute(action);

        }

        fetchBlogTextData = null;
        action = null;
        async = null;
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

        if(!(position < index.size())) return;

        String key = index.get(position);
        final Blog blog = super.getEntryFromFeedList(key);

        if(blog != null){
            holder.getBlogTileImageView().setImageBitmap(blog.getImage());
            holder.getBlogTileHeadingView().setText(blog.getHeading());

            holder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("STATUS", 1);
                    intent.putExtra("BLOG_ID", blog.getId());
                    intent.putExtra("BLOG_HEADING", blog.getHeading());
                    intent.putExtra("BLOG_CONTENT", blog.getContent());
                    intent.putExtra("BLOG_IMAGE",blog.getImageFile());
                    intent.putExtra("BLOG_AUTHOR",blog.getAuthor());
                    intent.putExtra("BLOG_PUBLISHED_DATE",blog.getPublishedDate());
                    intent.putExtra("BLOG_SIGNATURE",blog.getSignature());

                    intent.putExtra("BLOG_AUTHOR_FIRSTNAME",blog.getAuthorFirstname());
                    intent.putExtra("BLOG_AUTHOR_LASTNAME",blog.getAuthorLastname());
                    intent.putExtra("BLOG_AUTHOR_IMAGE",blog.getAuthorImage());

                    intent.putExtra("BLOG_INDEX",blog.getIndex());

                    BlogFeedManagerGuest.super.getActivity().startActivityForResult(intent, BlogCode.VIEW_BLOG);
                }
            });

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
        // dummy
    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }

    private class ProcessPostFetchData implements PostAsyncAction{
        @Override
        public void processResult(String jsonResponse) {
            try{

                if(jsonResponse != null){
                    //Log.e("Message", jsonResponse);
                    JSONObject json = new JSONObject(jsonResponse);
                    String data = json.getString("data");
                    addFeedEntries(data);
                }else{
                    addFeedEntries(null);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class ProcessPostFetchImage implements PostAsyncImageAction {

        private Blog blog;
        private Activity activity;
        private int position;

        public ProcessPostFetchImage(Blog blog, int position, Activity activity){
            this.blog = blog;
            this.activity = activity;
            this.position = position;
        }

        @Override
        public String getImageFile() {
            return blog.getImageFile();
        }

        @Override
        public void processResult(Bitmap image) {

            if(image != null){
                GalleryHelper.saveImageToInternal(image, blog.getImageFile(), activity, "feed/blog");
                Bitmap resizeImage = DataHelper.resizeImageAspectRatio(image, 512, 288);
                image = null;
                blog.setImage(resizeImage);
                BlogFeedManagerGuest.this.notifyItemChanged(position);
            }
        }
    }

    private class RemoveFromCache implements LeastRecentlyUsedCache.OnRemoveFromCache{

        @Override
        public void onRemove(Object key) {
            String k = (String) key;
            int position = getIndex().indexOf(k);
            getIndex().remove(position);
            notifyItemRemoved(position);
        }
    }

    public ArrayList<String> getIndex() {
        return index;
    }
}
