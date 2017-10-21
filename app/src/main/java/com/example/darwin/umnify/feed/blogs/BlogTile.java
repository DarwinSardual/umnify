package com.example.darwin.umnify.feed.blogs;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;

public class BlogTile {

    private int id;
    private String heading;
    private String imageFile;
    private Bitmap image;
    private int index;
    int author;

    public BlogTile(int id, String heading, String imageFile, int author, int index){
        this.id = id;
        this.heading = heading;
        this.imageFile = imageFile;
        this.author = author;

        this.index = index;
    }

    /*public BlogTile(JSONObject data) throws JSONException{

        this.id = data.getInt("id");
        this.heading = data.getString("heading");
        this.image = null; // edit this one
    }*/

    public int getIndex() {
        return index;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getId(){
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public String getImageFile() {
        return imageFile;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getAuthor() {
        return author;
    }
}
