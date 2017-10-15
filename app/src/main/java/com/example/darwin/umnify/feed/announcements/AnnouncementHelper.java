package com.example.darwin.umnify.feed.announcements;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darwin on 9/29/17.
 */

public class AnnouncementHelper {

    public static Announcement createAnnouncementFromJSON(JSONObject data) throws JSONException{

        int id = data.getInt("id");
        String title = data.getString("title");
        String content = data.getString("content");
        String imageFile = data.isNull("image") ? null : data.getString("image");
        String publishedDate = data.isNull("published_date")? null: data.getString("published_date");
        String createdDate = data.isNull("created_date")? null: data.getString("created_date");
        int signature = data.getInt("signature");

        int authorId = data.getInt("author_id");
        String authorFirstname = data.getString("author_firstname");
        String authorLastname = data.getString("author_lastname");

        Announcement announcement = new Announcement(id, title, content, imageFile, null, authorId, createdDate, publishedDate, signature, authorFirstname, authorLastname);

        return announcement;
    }
}
