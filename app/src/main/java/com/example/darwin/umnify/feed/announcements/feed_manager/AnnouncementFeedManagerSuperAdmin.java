package com.example.darwin.umnify.feed.announcements.feed_manager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.darwin.umnify.DataActionWrapper;
import com.example.darwin.umnify.DataImageActionWrapper;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.feed.OnDeleteFeed;
import com.example.darwin.umnify.feed.PostAsyncAction;
import com.example.darwin.umnify.feed.PostAsyncDeleteAction;
import com.example.darwin.umnify.feed.announcements.Announcement;
import com.example.darwin.umnify.feed.announcements.AnnouncementOptionViewListener;
import com.example.darwin.umnify.feed.announcements.data_action_wrapper.AddAnnouncementDataActionWrapper;
import com.example.darwin.umnify.feed.announcements.data_action_wrapper.DeleteAnnouncementDataActionWrapper;
import com.example.darwin.umnify.feed.announcements.data_action_wrapper.UpdateAnnouncementDataActionWrapper;
import com.example.darwin.umnify.feed.announcements.view_holder.AnnouncementViewHolderSuperAdmin;
import com.example.darwin.umnify.wrapper.DataHelper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by darwin on 9/29/17.
 */

public class AnnouncementFeedManagerSuperAdmin<E extends AnnouncementViewHolderSuperAdmin> extends AnnouncementFeedManager<E> {

    private Bundle userData;

    public AnnouncementFeedManagerSuperAdmin(Activity activity, SwipeRefreshLayout swipeRefreshLayout,
                                     Class<E> cls, int layoutId, Bundle userData) {
        super(activity, swipeRefreshLayout, cls, layoutId);
        this.userData = userData;
    }

    @Override
    public void onBindViewHolder(E holder, int position) {
        super.onBindViewHolder(holder, position);

        if(!(position < super.getIndex().size())) return;

        String id = super.getIndex().get(position);
        Announcement announcement = super.getEntryFromFeedList(id);
        if(announcement != null){
            int userId = userData.getInt("USER_ID");

            holder.getAnnouncementOptionView().setOnItemSelectedListener(new AnnouncementOptionViewListener(super.getActivity(), new ProcessDeleteFeed(Integer.parseInt(id)), announcement.getId()));
            if(userId != announcement.getAuthorId()) {
                holder.getAnnouncementOptionView().setVisibility(View.INVISIBLE);
                holder.getAnnouncementOptionView().setEnabled(false);
            }else{
                holder.getAnnouncementOptionView().setVisibility(View.VISIBLE);
                holder.getAnnouncementOptionView().setEnabled(true);

            }
        }
    }

    @Override
    public void newFeedEntry(Intent data) {

        Uri uri = data.getData();
        Cursor cursor;

        HashMap<String, String> textData = new HashMap<>();
        HashMap<String, byte[]> fileData = new HashMap<>();

        if(uri != null){

            String mimeType = DataHelper.getMimeType(uri, super.getActivity());

            cursor = super.getActivity().getContentResolver().query(uri,
                    null, null,
                    null, null);

            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();

            String imageFile = cursor.getString(nameIndex);
            Bitmap image = null;
            int orientation = 0;
            byte[] byteArray = null;
            Bitmap rescaledImage = null;

            try{
                image = MediaStore.Images.Media.getBitmap(super.getActivity().getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }

            byteArray = DataHelper.bitmapToByteArray(image, mimeType);

            textData.put("title", data.getStringExtra("ADD_ANNOUNCEMENT_TITLE"));
            textData.put("content", data.getStringExtra("ADD_ANNOUNCEMENT_CONTENT"));
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");


            fileData.put(imageFile, byteArray);

        }else{
            textData.put("title", data.getStringExtra("ADD_ANNOUNCEMENT_TITLE"));
            textData.put("content", data.getStringExtra("ADD_ANNOUNCEMENT_CONTENT"));
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");
        }

        WebServiceAction action = new DataImageActionWrapper(textData,
                fileData, super.getActivity(),AuthenticationAddress.ADD_ANNOUNCEMENT, new ProcessPostAddAnnouncement());

        WebServiceAsync async = new WebServiceAsync();
        async.execute(action);
    }

    @Override
    public void updateFeedContent(Intent data) {

        Uri uri = data.getData();
        Cursor cursor;

        HashMap<String, String> textData = new HashMap<>();
        HashMap<String, byte[]> fileData = new HashMap<>();

        if(uri != null){

            String mimeType = DataHelper.getMimeType(uri, super.getActivity());

            cursor = super.getActivity().getContentResolver().query(uri,
                    null, null,
                    null, null);

            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();

            String imageFile = cursor.getString(nameIndex);
            Bitmap image = null;
            int orientation = 0;
            byte[] byteArray = null;
            Bitmap rescaledImage = null;

            try{
                image = MediaStore.Images.Media.getBitmap(super.getActivity().getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }

            byteArray = DataHelper.bitmapToByteArray(image, mimeType);


            textData.put("id", data.getIntExtra("ANNOUNCEMENT_ID", -1) +"");
            textData.put("remove", data.getIntExtra("EDIT_ANNOUNCEMENT_IMAGE_REMOVE", 0) + "");
            textData.put("title", data.getStringExtra("EDIT_ANNOUNCEMENT_TITLE"));
            textData.put("content", data.getStringExtra("EDIT_ANNOUNCEMENT_CONTENT"));
            textData.put("image_file", imageFile);
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");


            fileData.put(imageFile, byteArray);

        }else{

            textData.put("id", data.getIntExtra("ANNOUNCEMENT_ID", -1) +"");
            textData.put("remove", data.getIntExtra("EDIT_ANNOUNCEMENT_IMAGE_REMOVE", 0) + "");
            textData.put("title", data.getStringExtra("EDIT_ANNOUNCEMENT_TITLE"));
            textData.put("content", data.getStringExtra("EDIT_ANNOUNCEMENT_CONTENT"));
            textData.put("user_type", userData.getInt("USER_TYPE") + "");
            textData.put("author", userData.getInt("USER_ID") + "");
        }

        WebServiceAction action = new DataImageActionWrapper(textData,
                fileData, super.getActivity(),AuthenticationAddress.UPDATE_ANNOUNCEMENT, new ProcessPostUpdateAnnouncement());

        WebServiceAsync async = new WebServiceAsync();
        async.execute(action);

    }

    @Override
    public void deleteFeedEntry(String key) {

        WebServiceAsync async = new WebServiceAsync();

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", key);

        PostAsyncAction postProcess =
                new ProcessPostDeleteAnnouncement(key);
        WebServiceAction action =
                new DataActionWrapper(textDataOutput, super.getActivity(), AuthenticationAddress.DELETE_ANNOUNCEMENT, postProcess);

        async.execute(action);
        textDataOutput = null;
    }

    private class ProcessDeleteFeed implements OnDeleteFeed {

        private int pos;

        public ProcessDeleteFeed(int pos){
            this.pos = pos;
        }

        public int getIndex() {
            return pos;
        }

        @Override
        public void onDeleteFeed() {
            deleteFeedEntry(getIndex() + "");
        }
    }

    private class ProcessPostUpdateAnnouncement implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {

        }
    }

    private class ProcessPostAddAnnouncement implements PostAsyncAction{

        @Override
        public void processResult(String jsonResponse) {
            Log.e("response", jsonResponse);
        }
    }

    private class ProcessPostDeleteAnnouncement implements PostAsyncAction {

        private String key;

        public ProcessPostDeleteAnnouncement(String key){
            this.key = key;
        }

        @Override
        public void processResult(String jsonResponse) {

            if(jsonResponse != null){
                int position = getIndex().indexOf(key);
                getIndex().remove(position);
                removeFromFeedList(key);
                notifyItemRemoved(position);
            }else{
                Toast.makeText(getActivity(), "Failed to delete.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
