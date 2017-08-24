package com.example.darwin.umnify.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.calendar.CalendarActivity;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.groups.GroupsActivity;
import com.example.darwin.umnify.start.StartActivity;

import java.io.File;

public class HomeActivityControllerAdmin extends HomeActivityControllerNormal{

    private FloatingActionButton floatingActionButton;

    public HomeActivityControllerAdmin(AppCompatActivity activity, Bundle userData){
        super(activity, userData);

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

        setFloatingActionButton();
    }

    public void setFloatingActionButton(){

        floatingActionButton = (FloatingActionButton) super.getActivity().findViewById(R.id.home_fab);

    }

    public void setUpNavigationView(){
        super.setUpNavigationView();

        super.getNavigationView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                Intent intent = null;

                if(id == R.id.navigation_groups){
                    intent = new Intent(HomeActivityControllerAdmin.super.getActivity(), GroupsActivity.class);
                    HomeActivityControllerAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_calendar) {
                    intent = new Intent(HomeActivityControllerAdmin.super.getActivity(), CalendarActivity.class);
                    HomeActivityControllerAdmin.super.getActivity().startActivity(intent);
                }else if(id == R.id.navigation_logout){
                    //erase all the folders
                    File directory = HomeActivityControllerAdmin.super.getActivity().getDir("umnify", Context.MODE_PRIVATE);
                    HomeActivity.deleteDirectoryRecursive(directory);
                    //delete the database
                    UMnifyDbHelper.getInstance(HomeActivityControllerAdmin.super.getActivity()).close();
                    HomeActivityControllerAdmin.super.getActivity().deleteDatabase(UMnifyDbHelper.DATABASE_NAME);

                    HomeActivityControllerAdmin.super.getActivity().finish();
                    intent = new Intent(HomeActivityControllerAdmin.super.getActivity(), StartActivity.class);
                    HomeActivityControllerAdmin.super.getActivity().startActivity(intent);
                }

                 HomeActivityControllerAdmin.super.getDrawerLayout().closeDrawers();

                return true;
            }
        });
    }
}
