package com.example.darwin.umnify.feed.news;

class NewsWrapper{

    News news;
    NewsFeedManager manager;

    public NewsWrapper(News news, NewsFeedManager manager){
        this.news = news;
        this.manager = manager;
    }
}