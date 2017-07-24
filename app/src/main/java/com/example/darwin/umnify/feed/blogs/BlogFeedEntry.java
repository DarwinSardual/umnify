package com.example.darwin.umnify.feed.blogs;

import android.graphics.drawable.Drawable;

public class BlogFeedEntry {

    private String subTitle;
    private String title;
    private Drawable image;

    public BlogFeedEntry(String title, String subTitle, Drawable image){
        this.subTitle = subTitle;
        this.title = title;
        this.image = image;

    }

    public String subTitle(){
        return subTitle;
    }

    public String getTitle(){
        return title;
    }

    public Drawable getImage(){
        return image;
    }
}