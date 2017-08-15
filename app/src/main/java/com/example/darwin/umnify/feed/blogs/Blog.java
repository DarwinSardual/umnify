package com.example.darwin.umnify.feed.blogs;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;

public class Blog {

    private int id;
    private String heading;
    private String content;
    private String publishedDate;
    private int signature;
    private Bitmap image;

    private int authorId;
    private String authorFirstname;
    private String authorLastname;
    private String authorImage;


    public Blog(int id, String heading, String content, int authorId, String publishedDate, Bitmap image, int signaure){

        this.id = id;
        this.content = content;
        this.heading = heading;
        this.authorId = authorId;
        this.publishedDate = publishedDate;
        this.image = image;
        this.signature = signature;

    }

    public Blog(JSONObject data)throws JSONException{

        id = data.getInt("id");
        heading = data.getString("heading");
        content = data.getString("content");
        authorId = data.getInt("author");
        publishedDate = data.getString("published_date");
        this.image = null;
        this.signature = data.getInt("signature");

    }

    public Blog(BlogTile blogTile, JSONObject restOfData) throws JSONException{

        this.id = blogTile.getId();
        this.heading = blogTile.getHeading();
        this.image = blogTile.getImage();

        content = restOfData.getString("content");
        authorId = restOfData.getInt("author");
        publishedDate = restOfData.getString("published_date");
        this.signature = restOfData.getInt("signature");

        authorFirstname = restOfData.getString("firstname");
        authorLastname = restOfData.getString("lastname");
        authorImage = restOfData.getString("author_image");

    }

    public int getId(){ return id;}

    public String getContent(){
        return content;
    }

    public String getHeading(){
        return heading;
    }

    public int getAuthorId(){return authorId;}

    public String getPublishedDate(){ return publishedDate; }

    public Bitmap getImage(){
        return image;
    }

    public int getSignature(){ return signature; }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }

    public String getAuthorImage() {
        return authorImage;
    }
}