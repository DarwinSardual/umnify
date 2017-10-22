package com.example.darwin.umnify.feed.announcements.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.darwin.umnify.feed.announcements.Announcement;
import com.example.darwin.umnify.feed.announcements.AnnouncementHelper;
import com.example.darwin.umnify.feed.announcements.data_action_wrapper.FetchAnnouncementDataActionWrapper;
import com.example.darwin.umnify.feed.announcements.data_action_wrapper.FetchAnnouncementImageDataActionWrapper;
import com.example.darwin.umnify.feed.announcements.view_holder.AnnouncementViewHolder;
import com.example.darwin.umnify.gallery.GalleryHelper;
import com.example.darwin.umnify.gallery.ViewImageActivity;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by darwin on 9/29/17.
 */

public class AnnouncementFeedManager <E extends AnnouncementViewHolder> extends FeedManager<E, Announcement>{

    private int layoutId;
    private Class<E> cls;
    private List<String> index;
    private boolean hasConnection = true;
    private int offset;

    private SQLiteDatabase databaseRead;
    private UMnifyDbHelper databaseConnection;

    public AnnouncementFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                Class<E> cls, int layoutId){
        super(activity, swipeRefreshLayout, 30);
        super.setOnRemoveFromCache(new RemoveFromCache());
        this.layoutId = layoutId;
        this.cls = cls;
        this.index = new ArrayList<>();
        offset = 0;
        databaseConnection = UMnifyDbHelper.getInstance(super.getActivity());
        databaseRead = databaseConnection.getReadableDatabase();

        updateFeed(-1);

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

        String id = index.get(position);
        final Announcement announcement = super.getEntryFromFeedList(id);

        if(announcement != null){

            holder.getTitleView().setText(announcement.getTitle());
            holder.getContentView().setText(announcement.getContent());
            holder.getImageView().setImageBitmap(announcement.getImage());

            holder.getImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                    intent.putExtra("ROOT_LOCATION", AuthenticationAddress.ANNOUNCEMENT_IMAGE_FOLDER_NON);
                    intent.putExtra("FOLDER", "feed/announcement");
                    intent.putExtra("IMAGE_FILE", announcement.getImageFile());
                    getActivity().startActivity(intent);
                }
            });
        }

    }

    @Override
    public void updateFeed(int direction) {

        if(super.isFetchingFeedEntry()) return;
        WebServiceAsync asyncFetchAnnouncement = new WebServiceAsync();
        PostAsyncAction processFetchAnnouncement = new ProcessPostFetchAnnouncement();
        WebServiceAction action;
        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("order", "desc");


        if(direction == 1){

            super.setFetchingFeedEntry(true);

            if(hasConnection){

                textDataOutput.put("offset", offset + "");
                textDataOutput.put("limit", "3");

                action = new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.FETCH_ANNOUNCEMENT ,processFetchAnnouncement);
                asyncFetchAnnouncement.execute(action);

                textDataOutput = null;
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

            textDataOutput.put("offset", offset + "");
            textDataOutput.put("limit", "5");

            action = new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.FETCH_ANNOUNCEMENT ,processFetchAnnouncement);
            asyncFetchAnnouncement.execute(action);

        }
        textDataOutput = null;
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
                String query = "select * from Announcement where published_date < ? and id != ? order by datetime(published_date) desc limit 3";
                //String query = "select * from News where id < ? order by datetime(published_date) desc limit 3";

                int position = index.size() - 1;

                String key = index.get(position);
                Announcement announcement = super.getEntryFromFeedList(key);

                String date = announcement.getPublishedDate();
                String idKey = Integer.toString(announcement.getId());

                String[] selectionArgs = {date, idKey};
                cursor = databaseRead.rawQuery(query, selectionArgs);

            }else{
                String query = "select * from Announcement order by datetime(published_date) desc limit 5";
                cursor = databaseRead.rawQuery(query, null);
            }

            while(cursor.moveToNext()){

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.News.ID.toString()));

                if(index.indexOf(id) > 0) continue;

                String content = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.CONTENT.toString()));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.TITLE.toString()));
                String imageFile = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.IMAGE.toString()));
                int authorId = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.AUTHOR.toString()));
                String createdDate = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.CREATED_DATE.toString()));
                String publishedDate = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.PUBLISHED_DATE.toString()));
                int signature = cursor.getInt(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.SIGNATURE.toString()));

                String authorFirstname = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.AUTHOR_FIRSTNAME.toString()));
                String authorLastname = cursor.getString(cursor.getColumnIndexOrThrow(UMnifyContract.UMnifyColumns.Announcement.AUTHOR_LASTNAME.toString()));

                Announcement announcement = new Announcement(id, title, content, imageFile, null, authorId, createdDate, publishedDate, signature, authorFirstname, authorLastname);

                addFeedEntry(announcement);
            }
        }
    }

    @Override
    public void addFeedEntry(String jsonData) throws JSONException {
        JSONObject newsData = new JSONObject(jsonData);
        Announcement announcement = AnnouncementHelper.createAnnouncementFromJSON(newsData);
        AnnouncementHelper.addToLocalDB(announcement, getActivity());

        String key = Integer.toString(announcement.getId());
        int position = index.size();

        super.addToFeedList(key, announcement);
        index.add(key);
        offset++;
        notifyItemInserted(position);

        if(announcement.getImageFile() != null){
            Bitmap image = GalleryHelper.loadImageFromInternal(announcement.getImageFile(), getActivity(), "feed/annoucement");
            if(image != null){
                announcement.setImage(image);
                notifyItemChanged(position);
            }else{
                WebServiceAction imageAction;
                WebServiceAsync async;

                PostAsyncImageAction postProcess =
                        new ProcessPostFetchImage(announcement, super.getActivity(), position);

                imageAction = new ImageActionWrapper(super.getActivity(), AuthenticationAddress.ANNOUNCEMENT_IMAGE_FOLDER + "/preview/" + announcement.getImageFile(), postProcess);
                async = new WebServiceAsync();
                async.execute(imageAction);
            }
        }
    }

    public void addFeedEntry(Announcement announcement){

        Bitmap newsImage = GalleryHelper.loadImageFromInternal(announcement.getImageFile(), super.getActivity(), "feed/announcement");
        if(newsImage != null){
            announcement.setImage(DataHelper.resizeImageAspectRatio(newsImage, 640, 360));
        }

        String key = Integer.toString(announcement.getId());
        int position = index.size();

        super.addToFeedList(key, announcement);
        index.add(key);
        offset++;
        notifyItemInserted(position);

    }

    @Override
    public void newFeedEntry(Intent data) {
        //dummy
    }

    @Override
    public void updateFeedContent(Intent data) {
        //dummy
    }

    @Override
    public void deleteFeedEntry(String key) {
        //dummy
    }

    @Override
    public int getItemCount() {
        return super.getFeedListSize();
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }


    private class ProcessPostFetchAnnouncement implements PostAsyncAction {

        @Override
        public void processResult(String jsonResponse) {
            try {

                if(jsonResponse == null){

                    setHasConnection(false);
                    addFeedEntries(jsonResponse);
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

    private class ProcessPostFetchImage implements PostAsyncImageAction{

        private Activity activity;
        private Announcement announcement;
        private int position;

        public ProcessPostFetchImage(Announcement announcement,Activity activity,  int position){
            this.activity = activity;
            this.announcement = announcement;
            this.position = position;
        }

        @Override
        public String getImageFile() {
            return announcement.getImageFile();
        }

        @Override
        public void processResult(Bitmap image) {
            if(image != null){
                if(image != null){
                    announcement.setImage(image);
                    GalleryHelper.saveImageToInternal(image,
                            announcement.getImageFile(), activity, "feed/announcement");
                    notifyItemChanged(position);
                }
                image = null;

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

    public List<String> getIndex() {
        return index;
    }
}
