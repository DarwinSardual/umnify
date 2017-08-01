package com.example.darwin.umnify.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.calendar.CalendarActivity;
import com.example.darwin.umnify.feed.blogs.BlogFeedFragment;
import com.example.darwin.umnify.feed.news.NewsFeedFragment;
import com.example.darwin.umnify.feed.notifications.NotificationsFeedFragment;
import com.example.darwin.umnify.groups.GroupsActivity;
import com.example.darwin.umnify.preferences.PreferencesActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeActivityLayout homeActivityLayout = new HomeActivityLayout(HomeActivity.this);
        drawerLayout = homeActivityLayout.getDrawerLayout();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Hello Snackbar!",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private class HomeActivityLayout{

        private Toolbar toolbar;
        private ActionBar actionBar;
        private ViewPager viewPager;
        private TabLayout tabLayout;
        private AppCompatActivity activity;
        private DrawerLayout drawerLayout;
        private NavigationView navigationView;

        public HomeActivityLayout(AppCompatActivity activity){
            this.activity = activity;

            setUpToolbar();
            setUpViewPager();
            setUpTabLayout();
            setDrawerLayout();
            setUpSupportActionBar();
            setUpNavigationView();
        }

        private void setUpToolbar(){
            toolbar = (Toolbar) activity.findViewById(R.id.home_toolbar);
            setSupportActionBar(toolbar);
        }

        private void setUpViewPager(){
            viewPager = (ViewPager) activity.findViewById(R.id.home_viewpager);
            viewPager.setOffscreenPageLimit(3);
            setUpViewPagerAdapter();
        }

        private void setUpViewPagerAdapter(){
            Adapter adapter = new Adapter(getSupportFragmentManager());
            adapter.addFragment(new NewsFeedFragment(), "News Feed");
            adapter.addFragment(new BlogFeedFragment(), "Blogs");
            adapter.addFragment(new NotificationsFeedFragment(), "Notification");

            viewPager.setAdapter(adapter);
        }

        private void setUpTabLayout(){

            tabLayout = (TabLayout) activity.findViewById(R.id.home_tablayout);

            tabLayout.setupWithViewPager(viewPager);

            tabLayout.getTabAt(0).setIcon(R.drawable.newsfeed_icon);
            tabLayout.getTabAt(1).setIcon(R.drawable.blogfeed_icon);
            tabLayout.getTabAt(2).setIcon(R.drawable.notificationsfeed_icon);
        }

        private void setUpNavigationView(){

            navigationView = (NavigationView) activity.findViewById(R.id.home_navigation_view);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    int id = item.getItemId();
                    Intent intent = null;

                    if(id == R.id.navigation_groups)
                        intent = new Intent(activity, GroupsActivity.class);
                    else if (id == R.id.navigation_calendar)
                        intent = new Intent(activity, CalendarActivity.class);
                    else if (id == R.id.navigation_preferences)
                        intent = new Intent(activity, PreferencesActivity.class);

                    startActivity(intent);

                    drawerLayout.closeDrawers();

                    return true;
                }
            });
        }

        private void setUpSupportActionBar(){
            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(R.drawable.drawer_icon);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        private void setDrawerLayout(){
            drawerLayout = (DrawerLayout) activity.findViewById(R.id.home_drawer);
        }

        public DrawerLayout getDrawerLayout(){
            return drawerLayout;
        }

    }

    private class Adapter extends FragmentPagerAdapter {

        private final int size = 3;
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
            //size++;
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
