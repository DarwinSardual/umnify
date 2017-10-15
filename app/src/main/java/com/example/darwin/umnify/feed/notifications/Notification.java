package com.example.darwin.umnify.feed.notifications;

import android.graphics.Bitmap;

/**
 * Created by darwin on 9/16/17.
 */

public class Notification {

    private int id;
    private int refId;
    private int index;
    private String content;
    private String publishedDate;
    private String type;
    private int author;

    private String authorFirstname;
    private String authorLastname;
    private String authorImageFile;


    private Bitmap authorImage;


    public Notification(int id, int refId, int index, String content, String publishedDate, String type, int author,
                        String authorFirstname, String authorLastname, String authorImageFile, Bitmap authorImage){

        this.id = id;
        this.refId = refId;
        this.index = index;
        this.content = content;
        this.publishedDate = publishedDate;
        this.type = type;
        this.author = author;

        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;
        this.authorImageFile = authorImageFile;
        this.authorImage = authorImage;
    }

    public void setAuthorImage(Bitmap image) {
        this.authorImage = image;
    }

    public Bitmap getImage() {
        return authorImage;
    }

    public int getId() {
        return id;
    }

    public int getRefId() {
        return refId;
    }

    public String getContent() {
        return content;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getType() {
        return type;
    }

    public int getAuthor() {
        return author;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }

    public String getAuthorImageFile() {
        return authorImageFile;
    }

    public int getIndex() {
        return index;
    }
}
