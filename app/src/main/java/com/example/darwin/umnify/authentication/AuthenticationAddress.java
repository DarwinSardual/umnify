package com.example.darwin.umnify.authentication;

public final class AuthenticationAddress {

    public static final String DOMAIN_NAME = "http://192.168.122.1/~darwin";
    //public static final String DOMAIN_NAME = "http://192.168.0.100/~darwin";
    public static final String ROOT_FOLDER = "/UMnifyMobileScripts";

    public static final String ROOT_LOGIN = "/login";
    public static final String AUTHENTICATE_LOGIN = DOMAIN_NAME + ROOT_FOLDER + ROOT_LOGIN + "/authenticate_login.php";
    public static final String CHECK_USER = DOMAIN_NAME + ROOT_FOLDER + ROOT_LOGIN + "/check_user.php";

    public static final String ROOT_FEED = "/feed";

    public static final String FETCH_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/fetch_news.php";
    public static final String ADD_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/add_news.php";
    public static final String DELETE_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/delete_news.php";
    public static final String STAR_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/star_news.php";
    public static final String NEWS_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER + "/uploads/images/feed/news";

    public static final String ADD_BLOG = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/blog/add_blog.php";
    public static final String DELETE_BLOG = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/blog/delete_blog.php";
    public static final String FETCH_BLOGS = DOMAIN_NAME + ROOT_FOLDER + ROOT_FEED + "/blog/fetch_blogs.php";
    public static final String BLOG_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER + "/uploads/images/feed/blog";

    public static final String AVATAR_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER + "/uploads/images/avatar";

    public static final String FETCH_PERSONAL_INFO = DOMAIN_NAME + ROOT_FOLDER + "/personal/fetch_personal_info.php";
    public static final String FETCH_EVALUATION = DOMAIN_NAME + ROOT_FOLDER + "/personal/fetch_evaluation.php";
    public static final String FETCH_STUDENT_PERMANENT_RECORD = DOMAIN_NAME + ROOT_FOLDER + "/personal/fetch_student_permanent_record.php";
    public static final String FETCH_SUBJECTS_ENROLLED = DOMAIN_NAME + ROOT_FOLDER + "/personal/fetch_subjects_enrolled.php";

    public static final String FETCH_NEWS_IMAGE_FILE = DOMAIN_NAME + ROOT_FOLDER + "/gallery/news/fetch_news_image_file.php";
    public static final String FETCH_BLOG_IMAGE_FILE = DOMAIN_NAME + ROOT_FOLDER + "/gallery/blog/fetch_blog_image_file.php";

    public static final String FETCH_CURRENT_DATE = DOMAIN_NAME + ROOT_FOLDER + "/calendar/fetch_current_date.php";
    public static final String FETCH_EVENTS = DOMAIN_NAME + ROOT_FOLDER + "/calendar/fetch_events.php";
    public static final String ADD_EVENT= DOMAIN_NAME + ROOT_FOLDER + "/calendar/add_event.php";
    public static final String DELETE_EVENT = DOMAIN_NAME + ROOT_FOLDER + "/calendar/delete_event.php";
}
