package com.example.darwin.umnify.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.about.HistoryActivity;
import com.example.darwin.umnify.about.VisionMissionGoalsActivity;
import com.example.darwin.umnify.about.quick_fact.QuickFactActivity;
import com.example.darwin.umnify.about.trivia.TriviaActivity;
import com.example.darwin.umnify.calendar.CalendarActivity;
import com.example.darwin.umnify.feed.announcements.AnnouncementFeedFragment;
import com.example.darwin.umnify.feed.blogs.BlogFeedFragment;
import com.example.darwin.umnify.feed.news.NewsFeedFragment;
import com.example.darwin.umnify.gallery.GalleryActivity;
import com.example.darwin.umnify.maps.CampusMapActivity;
import com.example.darwin.umnify.personal.EvaluationActivity;
import com.example.darwin.umnify.personal.ProfileActivity;
import com.example.darwin.umnify.personal.StudentPermanentRecordActivity;
import com.example.darwin.umnify.personal.SubjectsEnrolledActivity;
import com.example.darwin.umnify.poseidon.PoseidonAlertActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityControllerGuest {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private AppCompatActivity activity;

    private NewsFeedFragment newsFeedFragment;
    private BlogFeedFragment blogFeedFragment;
    private AnnouncementFeedFragment announcementFeedFragment;

    private Adapter adapter;

    public HomeActivityControllerGuest(AppCompatActivity activity){

        toolbar = (Toolbar) activity.findViewById(R.id.home_toolbar);
        viewPager = (ViewPager) activity.findViewById(R.id.home_viewpager);
        tabLayout = (TabLayout) activity.findViewById(R.id.home_tablayout);
        this.activity = activity;


    }

    public void init(){

        this.setSupportActionBar();
        setUpViewPager(4);
        setUpViewPagerAdapter(null);
        bindViewPagerToAdapter();
        setUpTabLayout();
        setDrawerLayout();
        setUpNavigationView();
        setUpNavigationListener();
    }

    public void setSupportActionBar(){
        activity.setSupportActionBar(toolbar);
        actionBar = getActivity().getSupportActionBar();
        if (actionBar != null ) {
            actionBar.setHomeAsUpIndicator(R.drawable.drawer_icon);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setUpViewPager(int offScreenLimit){
        viewPager.setOffscreenPageLimit(offScreenLimit);
    }

    public void setUpViewPagerAdapter(Bundle bundle){

        newsFeedFragment = new NewsFeedFragment();
        newsFeedFragment.setArguments(bundle);

        blogFeedFragment = new BlogFeedFragment();
        blogFeedFragment.setArguments(bundle);

        announcementFeedFragment = new AnnouncementFeedFragment();
        announcementFeedFragment.setArguments(bundle);

        adapter = new Adapter(activity.getSupportFragmentManager());

        adapter.addFragment(newsFeedFragment, "News");
        adapter.addFragment(blogFeedFragment, "Blog");
        adapter.addFragment(announcementFeedFragment, "Announcement");
    }

    public void bindViewPagerToAdapter(){
        viewPager.setAdapter(adapter);
    }

    public void setUpTabLayout(){

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.newsfeed_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.blogfeed_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.announcementfeed_icon);
        tabLayout.getTabAt(0).select();
    }

    public BlogFeedFragment getBlogFeedFragment() {
        return blogFeedFragment;
    }

    public NewsFeedFragment getNewsFeedFragment() {
        return newsFeedFragment;
    }

    public AnnouncementFeedFragment getAnnouncementFeedFragment() {
        return announcementFeedFragment;
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

    public class Adapter extends FragmentPagerAdapter {

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

    public void setUpNavigationView(){
        navigationView = (NavigationView) getActivity().findViewById(R.id.home_navigation_view);
    }

    public void setUpNavigationListener(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                Intent intent = null;

                if(id == R.id.navigation_poseidon){
                    intent = new Intent(activity, PoseidonAlertActivity.class);
                    //intent.putExtras(getUserData());
                    activity.startActivity(intent);
                }else if (id == R.id.navigation_history) {
                    intent = new Intent(activity, HistoryActivity.class);
                    //intent.putExtras(getUserData());
                    activity.startActivity(intent);
                }else if (id == R.id.navigation_vmg) {
                    intent = new Intent(activity, VisionMissionGoalsActivity.class);
                    //intent.putExtras(getUserData());
                    activity.startActivity(intent);
                }else if (id == R.id.navigation_trivia) {
                    intent = new Intent(activity, TriviaActivity.class);
                    //intent.putExtras(getUserData());
                    activity.startActivity(intent);
                }
                else if (id == R.id.navigation_quick_facts) {
                    intent = new Intent(activity, QuickFactActivity.class);
                    //intent.putExtras(getUserData());
                    activity.startActivity(intent);
                }
                else if (id == R.id.navigation_calendar) {
                    intent = new Intent(activity, CalendarActivity.class);
                    //intent.putExtras(getUserData());
                    activity.startActivity(intent);
                }else if (id == R.id.navigation_gallery) {
                    intent = new Intent(activity, GalleryActivity.class);
                    activity.startActivity(intent);
                }else if (id == R.id.navigation_maps) {
                    intent = new Intent(activity, CampusMapActivity.class);
                    activity.startActivity(intent);
                }else if (id == R.id.navigation_subjects_enrolled) {
                    intent = new Intent(activity, SubjectsEnrolledActivity.class);
                    //intent.putExtras(userData);
                    activity.startActivity(intent);
                }else if (id == R.id.navigation_spr) {
                    intent = new Intent(activity, StudentPermanentRecordActivity.class);
                    //intent.putExtras(userData);
                    activity.startActivity(intent);
                }else if(id == R.id.navigation_profile){
                    intent = new Intent(activity, ProfileActivity.class);
                    //intent.putExtras(userData);
                    activity.startActivity(intent);
                }else if (id == R.id.navigation_evalution) {
                    intent = new Intent(activity, EvaluationActivity.class);
                    //intent.putExtras(userData);
                    activity.startActivity(intent);
                }

                getDrawerLayout().closeDrawers();

                return true;
            }
        });
    }

    public void setDrawerLayout(){
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.home_drawer);
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }
}
