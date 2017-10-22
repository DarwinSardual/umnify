package com.example.darwin.umnify.feed.news.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darwin.umnify.*;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.database.UMnifyContract;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.feed.FeedManager;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncImageAction;
import com.example.darwin.umnify.feed.news.News;
import com.example.darwin.umnify.feed.news.NewsHelper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchAuthorImageDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchNewsDataActionWrapper;
import com.example.darwin.umnify.feed.news.data_action_wrapper.FetchNewsImageDataActionWrapper;
import com.example.darwin.umnify.feed.news.view_holder.NewsViewHolderGuest;
import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.gallery.ViewImageActivity;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsFeedManagerGuest<E extends NewsViewHolderGuest> extends FeedManager<E, News>{

    private Class<E> cls;
    private int layoutId;
    private SQLiteDatabase databaseRead;
    private UMnifyDbHelper databaseConnection;
    private boolean hasConnection = true;
    private RecyclerView recyclerView;
    private ArrayList<String> index;
    private ProcessPostFetchData processPostFetchData;
    private int offset;
    private BitmapFactory.Options options;

    public NewsFeedManagerGuest(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView,
                                Class<E> cls, int layoutId){

        super(activity, swipeRefreshLayout, 50);
        super.setOnRemoveFromCache(new RemoveFromCache());
        this.cls = cls;
        this.recyclerView = recyclerView;
        this.layoutId = layoutId;
        this.index = new ArrayList<>();
        offset = 0;
        options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        databaseConnection = UMnifyDbHelper.getInstance(super.getActivity());
        databaseRead = databaseConnection.getReadableDatabase();
        processPostFetchData = new ProcessPostFetchData();

        updateFeed(-1);
    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {

        JSONObject newsData = new JSONObject(jsonData);
        News news = NewsHelper.createNewsFromJSON(newsData, -1);
        NewsHelper.addNewsToLocalDb(news, super.getActivity());

        String key = Integer.toString(news.getId());
        int position = index.size();

        super.addToFeedList(key, news);
        index.add(key);
        offset++;
        notifyItemInserted(position);

        if(news.getAuthorImageFile() != null){
            Bitmap authorImage = GalleryHelper.loadImageFromInternal(news.getAuthorImageFile(), super.getActivity(), "avatar", options);
            if(authorImage != null){
                news.setAuthorImage(DataHelper.resizeImageAspectRatio(authorImage, authorImage.getWidth(), authorImage.getHeight()));
                notifyItemChanged(position);
            }else {

                WebServiceAction imageAction;
                WebServiceAsync async;

                PostAsyncImageAction postProcess =
                        new ProcessPostFetchAuthorImage(news, position, super.getActivity());

                imageAction = new ImageActionWrapper(super.getActivity(), AuthenticationAddress.AVATAR_IMAGE_FOLDER + "/" + news.getAuthorImageFile(), postProcess);
                async = new WebServiceAsync();
                async.execute(imageAction);
            }
        }

        if(news.getImageFile() != null){
            Bitmap newsImage = GalleryHelper.loadImageFromInternal(news.getImageFile(), super.getActivity(), "feed/news");
            if(newsImage != null){
                news.setImage(DataHelper.resizeImageAspectRatio(newsImage, 640, 360));
                //news.setImage(newsImage);
                notifyItemChanged(position);
            }else{
                WebServiceAction imageAction;
                WebServiceAsync async;

                PostAsyncImageAction postProcess =
                        new ProcessPostFetchNewsImage(news, position, super.getActivity());

                imageAction = new ImageActionWrapper(super.getActivity(), AuthenticationAddress.NEWS_IMAGE_FOLDER + "/preview/" + news.getImageFile(), postProcess);
                async = new WebServiceAsync();
                async.execute(imageAction);
            }
        }
    }

    public void addFeedEntry(News news){

        Bitmap authorImage = GalleryHelper.loadImageFromInternal(news.getAuthorImageFile(), super.getActivity(), "avatar");
        if(authorImage != null){
            news.setAuthorImage(authorImage);
        }else{
            news.setAuthorImage(BitmapFactory.decodeResource(super.getActivity().getResources(), R.drawable.missing_avatar));
        }
        Bitmap newsImage = GalleryHelper.loadImageFromInternal(news.getImageFile(), super.getActivity(), "feed/news");

        if(newsImage != null){
            news.setImage(DataHelper.resizeImageAspectRatio(newsImage, 640, 360));
        }

        String key = Integer.toString(news.getId());
        int position = index.size();

        super.addToFeedList(key, news);
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
            //fetch from local db
            hasConnection = false;
            Cursor cursor;

            if(super.getFeedListSize() > 0){
                String query = "select * from News where published_date < ? and id != ? order by datetime(published_date) desc limit 3";
                //String query = "select * from News where id < ? order by datetime(published_date) desc limit 3";

                int position = index.size() - 1;

                String key = index.get(position);
                News news = super.getEntryFromFeedList(key);

                String date = news.getPublishedDate();
                String idKey = Integer.toString(news.getId());

                String[] selectionArgs = {date, idKey};
                cursor = databaseRead.rawQuery(query, selectionArgs);

            }else{
                String query = "select * from News order by datetime(published_date) desc limit 5";
                cursor = databaseRead.rawQuery(query, null);
            }

            while(cursor.moveToNext()){

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.ID.toString()));

                if(index.indexOf(id) > 0) continue;

                String content = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.CONTENT.toString()));
                String imageFile = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.IMAGE.toString()));
                int author = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.AUTHOR.toString()));
                String publshedDate = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.PUBLISHED_DATE.toString()));
                int signature = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.SIGNATURE.toString()));

                String authorFirstname = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.AUTHOR_FIRSTNAME.toString()));
                String authorLastname = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.AUTHOR_LASTNAME.toString()));
                String authorImageFile = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.AUTHOR_IMAGE.toString()));

                News news = new News(id, content, imageFile, author, publshedDate, signature, 0, false,
                        super.getFeedListSize(), authorFirstname, authorLastname, authorImageFile);

                addFeedEntry(news);
            }
        }
    }

    @Override
    public void updateFeed(int direction) {

        if(super.isFetchingFeedEntry()) return;

        WebServiceAsync asyncFetchNews = new WebServiceAsync();
        WebServiceAction action;
        HashMap<String, String> params = new HashMap<>();
        params.put("order", "desc");


        if(direction == 1){

            super.setFetchingFeedEntry(true);

            if(hasConnection){

                params.put("offset", offset + "");
                params.put("limit", "3");
                params.put("id", "-1");

                action = new DataActionWrapper(params, super.getActivity(), AuthenticationAddress.FETCH_NEWS, processPostFetchData);
                asyncFetchNews.execute(action);

            }else{
                try{
                    addFeedEntries(null);
                    super.setFetchingFeedEntry(false);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }else if(direction == -1){

                super.clearFeedList();
                index.clear();
                offset = 0;
                notifyDataSetChanged();
                super.setFetchingFeedEntry(true);
                hasConnection = true;

                params.put("offset", offset + "");
                params.put("limit", "5");
                params.put("id", "-1");

                action = new DataActionWrapper(params, super.getActivity(), AuthenticationAddress.FETCH_NEWS, processPostFetchData);
                asyncFetchNews.execute(action);

        }

        action = null;
        params = null;
        asyncFetchNews = null;
    }

    @Override
    public void newFeedEntry(Intent data){
        //dummy
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
    public E onCreateViewHolder(ViewGroup parent, int viewType){


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

            String id = index.get(position);
            Log.e("id", id + "");
            final News news = super.getEntryFromFeedList(id);

            if(news != null){
                holder.getNewsAuthorView().setText(news.getAuthorFirstname() + " " + news.getAuthorLastname());
                holder.getNewsContentView().setText(news.getContent());
                holder.getNewsAuthorImageView().setImageBitmap(news.getAuthorImage());
                holder.getNewsImageView().setImageBitmap(news.getImage());

                String dateSplit[] = news.getPublishedDate().split(" ");
                holder.getNewsDateView().setText(DateHelper.convertDateToMDY(dateSplit[0]) + " " + DateHelper.convert24Hourto12Hour(dateSplit[1]));

                holder.getNewsImageView().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(NewsFeedManagerGuest.this.getActivity(), ViewImageActivity.class);
                        intent.putExtra("ROOT_LOCATION", AuthenticationAddress.NEWS_IMAGE_FOLDER_NON);
                        intent.putExtra("FOLDER", "feed/news");
                        intent.putExtra("IMAGE_FILE", news.getImageFile());
                        NewsFeedManagerGuest.this.getActivity().startActivity(intent);
                    }
                });
            }
    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }

    private class ProcessPostFetchData implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            try {

                if(jsonResponse == null){

                    setHasConnection(false);
                    addFeedEntries(null);
                    setFetchingFeedEntry(false);
                    getSwipeRefreshLayout().setRefreshing(false);
                }else{

                    JSONObject str = new JSONObject(jsonResponse);
                    String data = str.getString("data");

                    addFeedEntries(data);
                    setFetchingFeedEntry(false);
                    getSwipeRefreshLayout().setRefreshing(false);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class ProcessPostFetchNewsImage implements PostAsyncImageAction {

        private News news;
        private Activity activity;
        private int position;

        public ProcessPostFetchNewsImage(News news, int position, Activity activity){
            this.news = news;
            this.activity = activity;
            this.position = position;
        }

        @Override
        public String getImageFile() {
            return news.getImageFile();
        }

        @Override
        public void processResult(Bitmap image) {
            if(image != null){
                news.setImage(DataHelper.resizeImageAspectRatio(image, 640, 360));
                GalleryHelper.saveImageToInternal(image,
                        news.getImageFile(), activity, "feed/news");
                notifyItemChanged(position);
            }

            image = null;
        }
    }

    private class ProcessPostFetchAuthorImage implements PostAsyncImageAction {

        private News news;
        private Activity activity;
        private int position;

        public ProcessPostFetchAuthorImage(News news, int position, Activity activity){

            this.news = news;
            this.activity = activity;
            this.position = position;
        }

        @Override
        public String getImageFile() {
           return news.getAuthorImageFile();
        }

        @Override
        public void processResult(Bitmap image) {
            if(image != null){
                news.setAuthorImage(DataHelper.resizeImageAspectRatio(image, image.getWidth()/2, image.getHeight()/2));
                GalleryHelper.saveImageToInternal(image,
                        news.getAuthorImageFile(), activity, "avatar");
                image = null;
            }else{

                    news.setAuthorImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.missing_avatar));

            }

            notifyItemChanged(position);
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

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ArrayList<String> getIndex() {
        return index;
    }
}
