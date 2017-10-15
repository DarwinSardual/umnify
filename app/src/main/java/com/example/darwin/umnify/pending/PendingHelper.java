package com.example.darwin.umnify.pending;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darwin on 9/22/17.
 */

public class PendingHelper {

    public static Pending createPendingFromJSON(JSONObject data, int index) throws JSONException{

        int id = data.getInt("id");
        String content = data.getString("content");
        String createdDate = data.getString("created_date");
        String publishedDate = data.getString("published_date");
        String type = data.getString("type");
        int author = data.getInt("author");
        String status = data.getString("status");
        String authorFirstname = data.getString("author_firstname");
        String authorLastname = data.getString("author_lastname");

        Pending pending = new Pending(id, index, content, createdDate, publishedDate, type, author, authorFirstname, authorLastname);

        return pending;
    }
}
