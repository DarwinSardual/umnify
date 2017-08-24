package com.example.darwin.umnify.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.feed.blogs.BlogFeedFragment;
import com.example.darwin.umnify.feed.news.NewsFeedFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityControllerGuest {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private AppCompatActivity activity;
    public static final int LAYOUT_ID = R.layout.activity_home_guest;

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
        setUpTabLayout();
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
}
