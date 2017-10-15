package com.example.darwin.umnify.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.about.HistoryActivity;
import com.example.darwin.umnify.about.qoute_of_the_day.QouteOfTheDayActivity;
import com.example.darwin.umnify.about.quick_fact.QuickFactActivity;
import com.example.darwin.umnify.about.trivia.TriviaActivity;
import com.example.darwin.umnify.about.VisionMissionGoalsActivity;
import com.example.darwin.umnify.calendar.CalendarActivity;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.feed.announcements.AddAnnouncementActivity;
import com.example.darwin.umnify.feed.announcements.AnnouncementCode;
import com.example.darwin.umnify.feed.blogs.AddBlogActivity;
import com.example.darwin.umnify.feed.blogs.BlogCode;
import com.example.darwin.umnify.feed.news.AddNewsActivity;
import com.example.darwin.umnify.feed.news.NewsCode;
import com.example.darwin.umnify.gallery.GalleryActivity;
import com.example.darwin.umnify.maps.CampusMapActivity;
import com.example.darwin.umnify.pending.PendingActivity;
import com.example.darwin.umnify.personal.EvaluationActivity;
import com.example.darwin.umnify.personal.ProfileActivity;
import com.example.darwin.umnify.personal.StudentPermanentRecordActivity;
import com.example.darwin.umnify.personal.SubjectsEnrolledActivity;
import com.example.darwin.umnify.poseidon.PoseidonAlertActivity;
import com.example.darwin.umnify.start.StartActivity;

import java.io.File;

/**
 * Created by darwin on 8/25/17.
 */

public class HomeActivityControllerSuperAdmin extends HomeActivityControllerAdmin{

    public HomeActivityControllerSuperAdmin(AppCompatActivity activity, Bundle userData){
        super(activity, userData);
    }

    public void init(){
        super.setSupportActionBar();
        super.setUpSupportActionBar();
        super.setUpViewPager(3);
        super.setUpViewPagerAdapter();
        super.bindViewPagerToAdapter();
        super.setDrawerLayout();
        super.setUpTabLayout();
        super.setUpNavigationView();
        setUpNavigationListener();
        super.setUpNavigationUser();

        setFloatingActionButton();
        setUpViewPagerOnPageChangeListener();
    }

    public void setUpViewPagerOnPageChangeListener(){

        final FabActionAdmin addNewsAction = new FabActionAdmin(super.getActivity(),
                AddNewsActivity.class, null, NewsCode.ADD_NEWS);

        final FabActionAdmin addBlogAction = new FabActionAdmin(super.getActivity(),
                AddBlogActivity.class, null, BlogCode.ADD_BLOG);

        final FabActionAdmin addAnnouncementAction = new FabActionAdmin(super.getActivity(),
                AddAnnouncementActivity.class, null, AnnouncementCode.ADD_ANNOUNCEMENT);

        getFloatingActionButton().setOnClickListener(addNewsAction);

        super.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    getFloatingActionButton().setOnClickListener(addNewsAction);
                    getFloatingActionButton().show();
                }else if(position == 1){
                    getFloatingActionButton().setOnClickListener(addBlogAction);
                    getFloatingActionButton().show();
                }else if(position == 2){
                    getFloatingActionButton().setOnClickListener(addAnnouncementAction);
                    getFloatingActionButton().show();
                }else if(position == 3){
                    getFloatingActionButton().hide();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void setUpNavigationListener(){

        super.getNavigationView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                Intent intent = null;

                if(id == R.id.navigation_history){
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), HistoryActivity.class);
                    intent.putExtras(getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_vmg) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), VisionMissionGoalsActivity.class);
                    intent.putExtras(getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_qoute) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), QouteOfTheDayActivity.class);
                    intent.putExtras(getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_trivia) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), TriviaActivity.class);
                    intent.putExtras(getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }
                else if (id == R.id.navigation_quick_facts) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), QuickFactActivity.class);
                    intent.putExtras(getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }
                else if (id == R.id.navigation_calendar) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), CalendarActivity.class);
                    intent.putExtras(HomeActivityControllerSuperAdmin.super.getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_gallery) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), GalleryActivity.class);
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_maps) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), CampusMapActivity.class);
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_subjects_enrolled) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), SubjectsEnrolledActivity.class);
                    intent.putExtras(HomeActivityControllerSuperAdmin.super.getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_spr) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), StudentPermanentRecordActivity.class);
                    intent.putExtras(HomeActivityControllerSuperAdmin.super.getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_evalution) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), EvaluationActivity.class);
                    intent.putExtras(HomeActivityControllerSuperAdmin.super.getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_poseidon) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), PoseidonAlertActivity.class);
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if(id == R.id.navigation_profile){
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), ProfileActivity.class);
                    intent.putExtras(HomeActivityControllerSuperAdmin.super.getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }
                else if(id == R.id.navigation_pending){
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), PendingActivity.class);
                    intent.putExtras(HomeActivityControllerSuperAdmin.super.getUserData());
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if(id == R.id.navigation_logout){
                    //erase all the folders
                    File directory = HomeActivityControllerSuperAdmin.super.getActivity().getDir("umnify", Context.MODE_PRIVATE);
                    HomeActivity.deleteDirectoryRecursive(directory);
                    //delete the database
                    UMnifyDbHelper.getInstance(HomeActivityControllerSuperAdmin.super.getActivity()).close();
                    HomeActivityControllerSuperAdmin.super.getActivity().deleteDatabase(UMnifyDbHelper.DATABASE_NAME);

                    HomeActivityControllerSuperAdmin.super.getActivity().finish();
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), StartActivity.class);
                    //intent.putExtra("dummy", "value");
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }

                HomeActivityControllerSuperAdmin.super.getDrawerLayout().closeDrawers();

                return true;
            }
        });
    }
}
