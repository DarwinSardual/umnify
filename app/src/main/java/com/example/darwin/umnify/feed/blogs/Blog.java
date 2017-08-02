package com.example.darwin.umnify.feed.blogs;

import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;

public class Blog {

    private int id;
    private String heading;
    private String content;
    private int author;
    private String publishedDate;
    private int signature;
    private Drawable image;

    public Blog(int id, String heading, String content, int author, String publishedDate, Drawable image, int signaure){

        this.id = id;
        this.content = content;
        this.heading = heading;
        this.author = author;
        this.publishedDate = publishedDate;
        this.image = image;
        this.signature = signature;

    }

    public Blog(JSONObject data)throws JSONException{

        id = data.getInt("id");
        heading = data.getString("heading");
        content = data.getString("content");
        author = data.getInt("author");
        publishedDate = data.getString("published_date");
        this.image = null;
        this.signature = data.getInt("signature");

    }

    public Blog(BlogTile blogTile, JSONObject restOfData) throws JSONException{

        this.id = blogTile.getId();
        this.heading = blogTile.getHeading();
        this.image = blogTile.getImage();

        content = restOfData.getString("content");
        author = restOfData.getInt("author");
        publishedDate = restOfData.getString("published_date");
        this.signature = restOfData.getInt("signature");



    }

    public int getId(){ return id;}

    public String content(){
        return content;
    }

    public String getHeading(){
        return heading;
    }

    public int getAuthor(){return author;}

    public String getPublishedDate(){ return publishedDate; }

    public Drawable getImage(){
        return image;
    }

    public int getSignature(){ return signature; }
}