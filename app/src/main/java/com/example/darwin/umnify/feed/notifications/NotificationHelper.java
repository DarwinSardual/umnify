package com.example.darwin.umnify.feed.notifications;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darwin on 9/16/17.
 */

public class NotificationHelper {

    public static Notification createNotificationFromJSON(JSONObject data, int index) throws JSONException{

        int id = data.getInt("notification_id");
        int refId = data.getInt("id");
        String content = data.getString("content");
        String publishedDate = data.getString("published_date");
        String type = data.getString("type");
        int author = data.getInt("author");

        String authorFirstname = data.getString("author_firstname");
        String authorLastname = data.getString("author_lastname");
        String authorImageFile = data.getString("author_image");

        Notification notification = new Notification(id, refId, index, content, publishedDate, type, author,
                authorFirstname, authorLastname, authorImageFile, null);

        return notification;
    }
}
