package com.example.darwin.umnify.about.qoute_of_the_day;

/**
 * Created by darwin on 10/1/17.
 */

public class QouteOfTheDay {

    private int id;
    private String content;
    private int authorId;
    private String publishedDate;

    private String authorFirstname;
    private String authorLastname;

    public QouteOfTheDay(int id, String content, int authorId, String publishedDate,
                         String authorFirstname, String authorLastname){

        this.id =id;
        this.content = content;
        this.authorId = authorId;
        this.publishedDate = publishedDate;

        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getAuthorId() {
        return authorId;
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
