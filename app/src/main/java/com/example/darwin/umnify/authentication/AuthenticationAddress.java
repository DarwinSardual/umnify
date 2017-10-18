package com.example.darwin.umnify.authentication;

public final class AuthenticationAddress {

    public static final String DOMAIN_NAME = "https://174.138.20.165";
    //public static final String DOMAIN_NAME = "http://192.168.122.1/~darwin";
    //public static final String DOMAIN_NAME = "http://192.168.122.1/~darwin";
    public static final String ROOT_FOLDER = "/UMnifyMobileScripts";

    public static final String ROOT_LOGIN = "/login";
    public static final String AUTHENTICATE_LOGIN = DOMAIN_NAME + ROOT_FOLDER + ROOT_LOGIN + "/authenticate_login.php";
    public static final String CHECK_USER = DOMAIN_NAME + ROOT_FOLDER + ROOT_LOGIN + "/check_user.php";

    public static final String ROOT_FEED = "/feed";

    public static final String FETCH_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/fetch_news.php";
    public static final String ADD_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/add_news.php";
    public static final String UPDATE_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/update_news.php";
    public static final String DELETE_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/delete_news.php";
    public static final String STAR_NEWS = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/news/star_news.php";
    public static final String NEWS_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER + "/uploads/images/feed/news";

    public static final String UPDATE_BLOG = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/blog/update_blog.php";
    public static final String ADD_BLOG = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/blog/add_blog.php";
    public static final String DELETE_BLOG = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/blog/delete_blog.php";
    public static final String FETCH_BLOG = DOMAIN_NAME + ROOT_FOLDER + ROOT_FEED + "/blog/fetch_blog.php";
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

    public static final String FETCH_NOTIFICATION = DOMAIN_NAME + ROOT_FOLDER + "/notification/fetch_notification.php";
    public static final String FETCH_PENDING = DOMAIN_NAME + ROOT_FOLDER + "/pending/fetch_pending.php";
    public static final String ALLOW_PENDING = DOMAIN_NAME + ROOT_FOLDER + "/pending/allow_pending.php";

    public static final String FETCH_ANNOUNCEMENT = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/announcement/fetch_announcement.php";
    public static final String ADD_ANNOUNCEMENT = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/announcement/add_announcement.php";
    public static final String DELETE_ANNOUNCEMENT = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/announcement/delete_announcement.php";
    public static final String UPDATE_ANNOUNCEMENT = DOMAIN_NAME + ROOT_FOLDER +  ROOT_FEED + "/announcement/update_announcement.php";
    public static final String ANNOUNCEMENT_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER + "/uploads/images/feed/announcement";

    public static final String FETCH_QOUTEOFTHEDAY = DOMAIN_NAME + ROOT_FOLDER +  "/about" + "/qouteoftheday/fetch_qoute_of_the_day.php";
    public static final String ADD_QOUTEOFTHEDAY = DOMAIN_NAME + ROOT_FOLDER +  "/about" + "/qouteoftheday/add_qoute_of_the_day.php";

    public static final String FETCH_TRIVIA = DOMAIN_NAME + ROOT_FOLDER +  "/about" + "/trivia/fetch_trivia.php";
    public static final String TRIVIA_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER +  "/uploads" + "/images/about/trivia";
    public static final String ADD_TRIVIA = DOMAIN_NAME + ROOT_FOLDER +  "/about" + "/trivia/add_trivia.php";

    public static final String FETCH_QUICK_FACT = DOMAIN_NAME + ROOT_FOLDER +  "/about" + "/quickfact/fetch_quick_fact.php";
    public static final String ADD_QUICK_FACT = DOMAIN_NAME + ROOT_FOLDER +  "/about" + "/quickfact/add_quick_fact.php";
    public static final String QUICK_FACT_IMAGE_FOLDER = DOMAIN_NAME + ROOT_FOLDER +  "/uploads" + "/images/about/quickfact";
}
