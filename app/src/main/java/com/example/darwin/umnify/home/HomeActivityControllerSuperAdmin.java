package com.example.darwin.umnify.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.calendar.CalendarActivity;
import com.example.darwin.umnify.database.UMnifyDbHelper;
import com.example.darwin.umnify.groups.GroupsActivity;
import com.example.darwin.umnify.start.StartActivity;

import java.io.File;

/**
 * Created by darwin on 8/25/17.
 */

public class HomeActivityControllerSuperAdmin extends HomeActivityControllerAdmin{

    public HomeActivityControllerSuperAdmin(AppCompatActivity activity, Bundle userData){
        super(activity, userData);
    }

    public void setUpNavigationListener(){

        super.getNavigationView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                Intent intent = null;

                if(id == R.id.navigation_about){

                }else if(id == R.id.navigation_groups){
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), GroupsActivity.class);
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if (id == R.id.navigation_calendar) {
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), CalendarActivity.class);
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }else if(id == R.id.navigation_pending){

                }else if(id == R.id.navigation_logout){
                    //erase all the folders
                    File directory = HomeActivityControllerSuperAdmin.super.getActivity().getDir("umnify", Context.MODE_PRIVATE);
                    HomeActivity.deleteDirectoryRecursive(directory);
                    //delete the database
                    UMnifyDbHelper.getInstance(HomeActivityControllerSuperAdmin.super.getActivity()).close();
                    HomeActivityControllerSuperAdmin.super.getActivity().deleteDatabase(UMnifyDbHelper.DATABASE_NAME);

                    HomeActivityControllerSuperAdmin.super.getActivity().finish();
                    intent = new Intent(HomeActivityControllerSuperAdmin.super.getActivity(), StartActivity.class);
                    HomeActivityControllerSuperAdmin.super.getActivity().startActivity(intent);
                }

                HomeActivityControllerSuperAdmin.super.getDrawerLayout().closeDrawers();

                return true;
            }
        });
    }
}
