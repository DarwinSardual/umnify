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
    private String imageFile;

    private int authorId;
    private String authorFirstname;
    private String authorLastname;
    private String authorImage;


    public Blog(int id, String heading, String content, int authorId, String publishedDate, String imageFile, Bitmap image, int signature){

        this.id = id;
        this.content = content;
        this.heading = heading;
        this.authorId = authorId;
        this.publishedDate = publishedDate;
        this.image = image;
        this.signature = signature;
        this.imageFile = imageFile;

    }

    public String getImageFile() {
        return imageFile;
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