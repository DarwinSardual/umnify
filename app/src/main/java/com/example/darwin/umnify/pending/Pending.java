package com.example.darwin.umnify.pending;

/**
 * Created by darwin on 9/21/17.
 */

public class Pending {

    private int id;
    private int index;
    private String content;
    private String createdDate;
    private String publishedDate;
    private String type;
    private int author;

    private String authorFirstname;
    private String authorLastname;


    public Pending(int id, int index, String content, String createdDate, String publishedDate, String type, int author,
                   String authorFirstname, String authorLastname){

        this.id = id;
        this.index = index;
        this.content = content;
        this.createdDate = createdDate;
        this.publishedDate = publishedDate;
        this.type = type;
        this.author = author;

        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;

    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public String getContent() {
        return content;
    }

    public int getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }
}
