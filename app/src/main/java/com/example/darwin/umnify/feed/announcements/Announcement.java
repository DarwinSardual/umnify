package com.example.darwin.umnify.feed.announcements;

import android.graphics.Bitmap;

/**
 * Created by darwin on 9/29/17.
 */

public class Announcement {

    private int id;
    private String title;
    private String content;
    private Bitmap image;
    private String imageFile;
    private String publishedDate;
    private String createdDate;
    private int signature;

    private int authorId;
    private String authorFirstname;
    private String authorLastname;

    public Announcement(int id, String title, String content, String imageFile, Bitmap image, int authorId,
                        String createdDate, String publishedDate, int signature,
                        String authorFirstname, String authorLastname){

        this.id = id;
        this.title = title;
        this.content = content;
        this.imageFile = imageFile;
        this.image = image;
        this.authorId = authorId;
        this.publishedDate = publishedDate;
        this.createdDate = createdDate;
        this.signature = signature;
        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getSignature() {
        return signature;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }
}
