package com.example.darwin.umnify.home;


import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.blogs.BlogFeedFragment;
import com.example.darwin.umnify.feed.news.NewsFeedFragment;
import com.example.darwin.umnify.feed.notifications.NotificationsFeedFragment;
import com.example.darwin.umnify.home.data_action_wrapper.HomeActivityControllerSuperAdmin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static final int ADD_NEWS_CODE = 1;
    public static final int ADD_BLOG_CODE = 2;

    private NewsFeedFragment newsFragment;
    private BlogFeedFragment blogFragment;
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

            }/*else if(USER_TYPE == AuthenticationCodes.NORMAL_USER ||
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
                }else if(USER_TYPE == AuthenticationCodes.SUPER_ADMIN_USER){

                    setContentView(R.layout.activity_home_super_admin);
                    HomeActivityControllerSuperAdmin homeActivityControllerSuperAdmin = new HomeActivityControllerSuperAdmin(this, userData);
                    homeActivityControllerSuperAdmin.init();
                    drawerLayout = homeActivityControllerSuperAdmin.getDrawerLayout();
                }

            }*/else{
                Toast.makeText(this, "Fatal error: Unknown user type.", Toast.LENGTH_SHORT).show();
                finish();
            }


        }else{
            //try to restore from saved instance
        }

    }

    private class HomeActivityControllerGuest {

        private Toolbar toolbar;
        private ViewPager viewPager;
        private TabLayout tabLayout;

        private AppCompatActivity activity;

        private NewsFeedFragment newsFeedFragment;
        private BlogFeedFragment blogFeedFragment;

        private Adapter adapter;

        public HomeActivityControllerGuest(AppCompatActivity activity){

            toolbar = (Toolbar) activity.findViewById(R.id.home_toolbar);
            viewPager = (ViewPager) activity.findViewById(R.id.home_viewpager);
            tabLayout = (TabLayout) activity.findViewById(R.id.home_tablayout);
            this.activity = activity;


        }

        public void init(){

            this.setSupportActionBar();
            setUpViewPager(3);
            setUpViewPagerAdapter(null);
            bindViewPagerToAdapter();
            //setUpTabLayout();
        }

        public void setSupportActionBar(){
            activity.setSupportActionBar(toolbar);
        }

        public void setUpViewPager(int offScreenLimit){
            viewPager.setOffscreenPageLimit(offScreenLimit);
        }

        public void setUpViewPagerAdapter(Bundle bundle){

            newsFeedFragment = new NewsFeedFragment();
            newsFeedFragment.setArguments(bundle);

            blogFeedFragment = new BlogFeedFragment();
            blogFeedFragment.setArguments(bundle);

            adapter = new Adapter(activity.getSupportFragmentManager());

            adapter.addFragment(newsFeedFragment, "News Feed");
            adapter.addFragment(blogFeedFragment, "Blogs");
        }

        public void bindViewPagerToAdapter(){
            viewPager.setAdapter(adapter);
        }

        public void setUpTabLayout(){

            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(R.drawable.newsfeed_icon);
            tabLayout.getTabAt(1).setIcon(R.drawable.blogfeed_icon);
            tabLayout.getTabAt(0).select();
        }

        public BlogFeedFragment getBlogFeedFragment() {
            return blogFeedFragment;
        }

        public NewsFeedFragment getNewsFeedFragment() {
            return newsFeedFragment;
        }

        public ViewPager getViewPager() {
            return viewPager;
        }

        public AppCompatActivity getActivity() {
            return activity;
        }

        public Adapter getAdapter() {
            return adapter;
        }

        public TabLayout getTabLayout() {
            return tabLayout;
        }

        private class Adapter extends FragmentPagerAdapter {

            private int size = 0;
            private final List<Fragment> tabFragments = new ArrayList<Fragment>();
            private final List<String> tabFragmentTitles = new ArrayList<String>();

            public Adapter(FragmentManager manager){
                super(manager);
            }

            @Override
            public Fragment getItem(int position){

                return tabFragments.get(position);
            }

            public void addFragment(Fragment fragment, String title){

                tabFragments.add(fragment);
                tabFragmentTitles.add(title);
                size++;
            }

            @Override
            public CharSequence getPageTitle(int position){
                return tabFragmentTitles.get(position);
            }

            @Override
            public int getCount(){
                return size;
            }
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

        Log.e("code", requestCode + "");

        if(requestCode == HomeActivity.ADD_NEWS_CODE){

            if(resultCode == RESULT_OK){
                // add user data
                // news fragment and trigger news manager to perform adding news

                newsFragment.addNews(data);
            }else{
                Log.e("There is a error", resultCode + "");
            }

        }else if(requestCode == HomeActivity.ADD_BLOG_CODE){

            if(resultCode == RESULT_OK){
                // add user data
                // news fragment and trigger news manager to perform adding news

                blogFragment.addBlog(data);
            }else{
                Log.e("There is a error", resultCode + "");
            }
        }
    }


}
