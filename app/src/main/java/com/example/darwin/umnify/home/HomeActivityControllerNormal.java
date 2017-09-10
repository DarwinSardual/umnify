package com.example.darwin.umnify.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.calendar.CalendarActivity;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.feed.notifications.NotificationsFeedFragment;
import com.example.darwin.umnify.gallery.GalleryActivity;
import com.example.darwin.umnify.groups.GroupsActivity;
import com.example.darwin.umnify.home.data_action_wrapper.FetchUserImageDataActionWrapper;
import com.example.darwin.umnify.maps.CampusMapActivity;
import com.example.darwin.umnify.personal.EvaluationActivity;
import com.example.darwin.umnify.personal.StudentPermanentRecordActivity;
import com.example.darwin.umnify.personal.SubjectsEnrolledActivity;
import com.example.darwin.umnify.start.StartActivity;

import java.io.File;
import java.util.HashMap;

public class HomeActivityControllerNormal extends HomeActivityControllerGuest {

    private Bundle userData;
    private NotificationsFeedFragment notificationsFeedFragment;

    private NavigationView navigationView;
    private ImageView userIconView;
    private TextView userNameView;
    private TextView userEmailView;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;

    public HomeActivityControllerNormal(AppCompatActivity activity, Bundle userData){
        super(activity);
        this.userData = userData;
    }

    @Override
    public void init(){

        super.setSupportActionBar();
        setUpSupportActionBar();
        super.setUpViewPager(3);
        setUpViewPagerAdapter();
        super.bindViewPagerToAdapter();
        setDrawerLayout();
        setUpTabLayout();
        setUpNavigationView();
        setUpNavigationListener();
        setUpNavigationUser();
    }

    public void setUpSupportActionBar(){
        actionBar = getActivity().getSupportActionBar();
        if (actionBar != null ) {
            actionBar.setHomeAsUpIndicator(R.drawable.drawer_icon);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setUpViewPagerAdapter(){

        super.setUpViewPagerAdapter(userData);

        notificationsFeedFragment = new NotificationsFeedFragment();
        notificationsFeedFragment.setArguments(userData);
        super.getAdapter().addFragment(notificationsFeedFragment, "Updates");
    }

    @Override
    public void setUpTabLayout(){
        super.setUpTabLayout();

        super.getTabLayout().getTabAt(2).setIcon(R.drawable.notificationsfeed_icon);
    }

    public void setUpNavigationView(){
        navigationView = (NavigationView) getActivity().findViewById(R.id.home_navigation_view);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout = navigationView.getHeaderView(0);

        userIconView = (ImageView) headerLayout.findViewById(R.id.drawer_navigation_user_image);
        userNameView = (TextView) headerLayout.findViewById(R.id.drawer_navigation_name);
        userEmailView = (TextView) headerLayout.findViewById(R.id.drawer_navigation_email);

    }

    public void setUpNavigationListener(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                Intent intent = null;

                if(id == R.id.navigation_about){

                }else if (id == R.id.navigation_calendar) {
                    intent = new Intent(HomeActivityControllerNormal.super.getActivity(), CalendarActivity.class);
                    intent.putExtras(getUserData());
                    HomeActivityControllerNormal.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_gallery) {
                    intent = new Intent(HomeActivityControllerNormal.super.getActivity(), GalleryActivity.class);
                    HomeActivityControllerNormal.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_maps) {
                    intent = new Intent(HomeActivityControllerNormal.super.getActivity(), CampusMapActivity.class);
                    HomeActivityControllerNormal.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_subjects_enrolled) {
                    intent = new Intent(HomeActivityControllerNormal.super.getActivity(), SubjectsEnrolledActivity.class);
                    intent.putExtras(userData);
                    HomeActivityControllerNormal.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_spr) {
                    intent = new Intent(HomeActivityControllerNormal.super.getActivity(), StudentPermanentRecordActivity.class);
                    intent.putExtras(userData);
                    HomeActivityControllerNormal.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_evalution) {
                    intent = new Intent(HomeActivityControllerNormal.super.getActivity(), EvaluationActivity.class);
                    intent.putExtras(userData);
                    HomeActivityControllerNormal.super.getActivity().startActivity(intent);
                }else if(id == R.id.navigation_logout){
                    //erase all the folders
                    File directory = HomeActivityControllerNormal.super.getActivity().getDir("umnify", Context.MODE_PRIVATE);
                    HomeActivity.deleteDirectoryRecursive(directory);
                    //delete the database
                    UMnifyDbHelper.getInstance(HomeActivityControllerNormal.super.getActivity()).close();
                    HomeActivityControllerNormal.super.getActivity().deleteDatabase(UMnifyDbHelper.DATABASE_NAME);

                    HomeActivityControllerNormal.super.getActivity().finish();
                    intent = new Intent(HomeActivityControllerNormal.super.getActivity(), StartActivity.class);
                    HomeActivityControllerNormal.super.getActivity().startActivity(intent);
                }

                HomeActivityControllerNormal.this.drawerLayout.closeDrawers();

                return true;
            }
        });
    }

    public void setUpNavigationUser(){

        HashMap<String, String> textData = new HashMap<>();
        textData.put("email", userData.getString("USER_EMAIL"));
        textData.put("name", userData.getString("USER_FIRSTNAME") +" " + userData.getString("USER_LASTNAME"));
        textData.put("image_file", userData.getString("USER_IMAGE_FILE"));

        FetchUserImageDataActionWrapper fetchUserImageDataActionWrapper = new FetchUserImageDataActionWrapper(
                textData, null, userNameView, userEmailView,
                userIconView);

        WebServiceAsync async = new WebServiceAsync();
        async.execute(fetchUserImageDataActionWrapper);

        textData = null;
        fetchUserImageDataActionWrapper = null;
        async = null;
    }

    public void setDrawerLayout(){
        drawerLayout = (DrawerLayout) super.getActivity().findViewById(R.id.home_drawer);
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NotificationsFeedFragment getNotificationsFeedFragment() {
        return notificationsFeedFragment;
    }

    public Bundle getUserData() {
        return userData;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
}
