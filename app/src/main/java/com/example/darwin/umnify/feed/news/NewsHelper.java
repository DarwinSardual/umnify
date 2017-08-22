package com.example.darwin.umnify.feed.news;

import android.app.Activity;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import org.json.JSONException;
import org.json.JSONObject;


public class NewsHelper {

    public static News createNewsFromJSON(JSONObject data, int index) throws JSONException{

        int id = data.getInt("id");
        String content = data.getString("content");
        String imageFile = data.getString("image");
        int authorId = data.getInt("author");
        String publishedDate = data.getString("published_date");
        int signature = data.getInt("signature");

        String authorFirstname = data.getString("firstname");
        String authorLastname = data.getString("lastname");
        String authorImageFile = data.getString("author_image");
        int stars = data.getInt("stars");
        boolean isStarred = data.getBoolean("starred");

        News news = new News(id, content, imageFile, authorId, publishedDate, signature, stars, isStarred,
                index, authorFirstname, authorLastname, authorImageFile);

        return news;

    }
}
