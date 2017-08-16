package com.example.darwin.umnify.authentication;

public final class AuthenticationAddress {

    //public static final String DOMAIN_NAME = "http://192.168.137.1";
    public static final String DOMAIN_NAME = "http://192.168.0.105/~darwin";
    public static final String ROOT_FOLDER = "/UMnifyMobileScripts";

    public static final String ROOT_LOGIN = "/login";
    public static final String AUTHENTICATE_LOGIN = DOMAIN_NAME + ROOT_FOLDER + ROOT_LOGIN + "/authenticate_login.php";
    public static final String CHECK_USER = DOMAIN_NAME + ROOT_FOLDER + ROOT_LOGIN + "/check_user.php";

    public static final String ROOT_FEED = "/feed";

    public static final String FETCH_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/fetch_news.php";
    public static final String ADD_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/add_news.php";
    public static final String STAR_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/star_news.php";
    public static final String NEWS_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER + "/uploads/images/feed/news";

    public static final String ADD_BLOG = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/blog/add_blog.php";
    public static final String FETCH_BLOGS = DOMAIN_NAME + ROOT_FOLDER + ROOT_FEED + "/blog/fetch_blogs.php";
    public static final String BLOG_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER + "/uploads/images/feed/blog";
}
