package com.example.darwin.umnify.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.example.darwin.umnify.feed.blogs.BlogFeedFragment;
import com.example.darwin.umnify.feed.news.NewsFeedFragment;
import com.example.darwin.umnify.feed.notifications.NotificationsFeedFragment;
import com.example.darwin.umnify.home.data_action_wrapper.FetchUserImageDataActionWrapper;

import java.util.HashMap;

public class HomeActivityControllerNormal extends HomeActivityControllerGuest{

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
        super.setUpTabLayout();
        setDrawerLayout();
        setUpTabLayout();
        setUpNavigationView();
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

    public void setUpNavigationUser(){

        HashMap<String, String> textData = new HashMap<>();
        textData.put("email", userData.getString("USER_EMAIL"));
        textData.put("name", userData.getString("USER_FIRSTNAME") +" " + userData.getString("USER_LASTNAME"));


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
