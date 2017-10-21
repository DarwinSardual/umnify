package com.example.darwin.umnify.home;


import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.announcements.Announcement;
import com.example.darwin.umnify.feed.announcements.AnnouncementCode;
import com.example.darwin.umnify.feed.announcements.AnnouncementFeedFragment;
import com.example.darwin.umnify.feed.blogs.BlogFeedFragment;
import com.example.darwin.umnify.feed.blogs.BlogCode;
import com.example.darwin.umnify.feed.news.NewsCode;
import com.example.darwin.umnify.feed.news.NewsFeedFragment;
import com.example.darwin.umnify.feed.notifications.Notification;
import com.example.darwin.umnify.feed.notifications.NotificationsCode;
import com.example.darwin.umnify.feed.notifications.NotificationsFeedFragment;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    public static final int ADD_NEWS_CODE = 1;
    public static final int ADD_BLOG_CODE = 2;

    private NewsFeedFragment newsFragment;
    private BlogFeedFragment blogFragment;
    private AnnouncementFeedFragment announcementFragment;
    private NotificationsFeedFragment notificationsFragment;

    private Bundle userData;

    private int USER_TYPE;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         userData = getIntent().getExtras();

        if(userData != null){

            USER_TYPE = userData.getInt("USER_TYPE");
            if(USER_TYPE == AuthenticationCodes.GUEST_USER){

                setContentView(R.layout.activity_home_guest);
                HomeActivityControllerGuest homeActivityManagerGuest = new HomeActivityControllerGuest(this);
                homeActivityManagerGuest.init();
                drawerLayout = homeActivityManagerGuest.getDrawerLayout();

            }else if(USER_TYPE == AuthenticationCodes.NORMAL_USER ||
                    USER_TYPE == AuthenticationCodes.ADMIN_USER ||
                    USER_TYPE == AuthenticationCodes.SUPER_ADMIN_USER){

                if(USER_TYPE == AuthenticationCodes.NORMAL_USER){

                    setContentView(R.layout.activity_home_normal);
                    HomeActivityControllerNormal homeActivityControllerNormal = new HomeActivityControllerNormal(this, userData);
                    homeActivityControllerNormal.init();
                    drawerLayout = homeActivityControllerNormal.getDrawerLayout();
                }else if(USER_TYPE == AuthenticationCodes.ADMIN_USER){

                    setContentView(R.layout.activity_home_admin);
                    HomeActivityControllerAdmin homeActivityControllerAdmin = new HomeActivityControllerAdmin(this, userData);
                    homeActivityControllerAdmin.init();
                    drawerLayout = homeActivityControllerAdmin.getDrawerLayout();

                    newsFragment = homeActivityControllerAdmin.getNewsFeedFragment();
                    blogFragment = homeActivityControllerAdmin.getBlogFeedFragment();
                    notificationsFragment = homeActivityControllerAdmin.getNotificationsFeedFragment();
                }else if(USER_TYPE == AuthenticationCodes.SUPER_ADMIN_USER){
                    setContentView(R.layout.activity_home_super_admin);
                    HomeActivityControllerSuperAdmin homeActivityControllerSuperAdmin = new HomeActivityControllerSuperAdmin(this, userData);
                    homeActivityControllerSuperAdmin.init();
                    drawerLayout = homeActivityControllerSuperAdmin.getDrawerLayout();

                    newsFragment = homeActivityControllerSuperAdmin.getNewsFeedFragment();
                    blogFragment = homeActivityControllerSuperAdmin.getBlogFeedFragment();
                    notificationsFragment = homeActivityControllerSuperAdmin.getNotificationsFeedFragment();
                    announcementFragment = homeActivityControllerSuperAdmin.getAnnouncementFeedFragment();
                }

            }else{
                Toast.makeText(this, "Fatal error: Unknown user type.", Toast.LENGTH_SHORT).show();
                finish();
            }


        }else{
            //try to restore from saved instance
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    public static void deleteDirectoryRecursive(File directory){
        if (directory.isDirectory()) {
            for (File child : directory.listFiles()) {
                deleteDirectoryRecursive(child);
            }
        }
        directory.delete();
    }



    private boolean backTapOnce = false;

    @Override
    public void onBackPressed() {
        if(backTapOnce){
            super.onBackPressed();
            return;
        }

        backTapOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                backTapOnce=false;
            }
        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == NewsCode.ADD_NEWS){

            if(resultCode == RESULT_OK){

                newsFragment.addNews(data);
            }else{

            }

        }else if(requestCode == BlogCode.ADD_BLOG){

            if(resultCode == RESULT_OK){

                blogFragment.addBlog(data);
            }else{

            }
        }else if(requestCode == BlogCode.VIEW_BLOG){
            if (resultCode == RESULT_OK) {
                if(data != null){
                    int action = data.getIntExtra("ACTION", -1);

                    if(action == BlogCode.DELETE_BLOG){
                        blogFragment.deleteBlog(data);
                    }else if(action == BlogCode.EDIT_BLOG){
                        blogFragment.updateBlog(data);
                    }
                }
            }

        }else if(requestCode == NewsCode.EDIT_NEWS){
            if(resultCode == RESULT_OK){
                if(data != null){
                    newsFragment.updateNews(data);
                }
            }
        }
        else if(requestCode == AnnouncementCode.ADD_ANNOUNCEMENT){
            if(resultCode == RESULT_OK){
                if(data != null){
                    announcementFragment.addAnnouncement(data);
                }
            }
        }else if(requestCode == AnnouncementCode.EDIT_ANNOUNCEMENT){
            if(resultCode == RESULT_OK){
                if(data != null){
                    announcementFragment.updateAnnouncement(data);
                }
            }
        }
        /*else if(requestCode == NewsCode.VIEW_NEWS){
            if(data != null){

                int action = data.getIntExtra("ACTION", -1);

                if(action == NewsCode.DELETE_NEWS){
                    newsFragment.deleteNews(data);
                }else if(action == NewsCode.EDIT_NEWS){

                }
            }
        }*/else if(requestCode == NotificationsCode.VIEW_NEWS){

            if(resultCode == RESULT_OK){
                if(data != null){

                    int action = data.getIntExtra("ACTION", -1);
                    if(action == NewsCode.DELETE_NEWS){
                        //Log.e("notification id", data.getIntExtra("NOTIFICATION_ID", -1) + "");
                        notificationsFragment.deleteNotification(data);
                    }else if(action == NewsCode.EDIT_NEWS){
                        data.putExtra("TYPE", "news");
                        notificationsFragment.updateNewsBlog(data);
                    }
                }
            }
        }else if(requestCode == NotificationsCode.VIEW_BLOG){

            if(resultCode == RESULT_OK){
                if(data != null) {

                    int action = data.getIntExtra("ACTION", -1);
                    if (action == BlogCode.DELETE_BLOG) {
                        notificationsFragment.deleteNotification(data);

                    } else if (action == BlogCode.EDIT_BLOG) {
                        data.putExtra("TYPE", "blog");
                        notificationsFragment.updateNewsBlog(data);
                    }
                }
            }
        }
    }
}
