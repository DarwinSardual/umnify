package com.example.darwin.umnify.feed.blogs;

import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;

public class BlogTile {

    private int id;
    private String heading;
    private Drawable image;

    public BlogTile(int id, String heading, Drawable image){
        this.id = id;
        this.heading = heading;
        this.image = image;

    }

    public BlogTile(JSONObject data) throws JSONException{

        this.id = data.getInt("id");
        this.heading = data.getString("heading");
        this.image = null; // edit this one
    }

    public int getId(){
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public Drawable getImage() {
        return image;
    }
}
