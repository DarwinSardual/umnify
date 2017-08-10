package com.example.darwin.umnify.authentication;

public final class AuthenticationAddress {

    public static final String DOMAIN_NAME = "https://139.59.108.167";
   // public static final String DOMAIN_NAME = "http://192.168.0.101/~darwin";
    public static final String ROOT_FOLDER = "/UMnifyMobileScripts";

    public static final String ROOT_LOGIN = "/login";
    public static final String AUTHENTICATE_LOGIN = DOMAIN_NAME + ROOT_FOLDER + ROOT_LOGIN + "/authenticate_login.php";
    public static final String CHECK_USER = DOMAIN_NAME + ROOT_FOLDER + ROOT_LOGIN + "/check_user.php";


    public static final String ROOT_FEED = "/feed";
    public static final String FETCH_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/fetch_news.php";
    public static final String FETCH_BLOGS = DOMAIN_NAME + ROOT_FOLDER + ROOT_FEED + "/blog/fetch_blogs.php";
}
