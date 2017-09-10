package com.example.darwin.umnify.calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.authentication.AuthenticationCodes;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private Bundle userData;
    CalendarManager manager;
    public static int ADD_EVENT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userData = getIntent().getExtras();

        if(userData != null){

            if(userData.getInt("USER_TYPE") == AuthenticationCodes.ADMIN_USER ||
                    userData.getInt("USER_TYPE") == AuthenticationCodes.SUPER_ADMIN_USER){

                setContentView(R.layout.activity_calendar_admin);
                MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
                manager = new CalendarManagerAdmin(this, calendarView, userData);
                manager.init();

            }else{
                setContentView(R.layout.activity_calendar);
                MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
                manager = new CalendarManager(this, calendarView);
                manager.init();

            }
        }else{
            setContentView(R.layout.activity_calendar);
            MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
            manager = new CalendarManager(this, calendarView);
            manager.init();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CalendarActivity.ADD_EVENT_CODE){
            if(resultCode == RESULT_OK){

                addEvent(data);
            }
        }
    }

    public void addEvent(Intent data){

        ((CalendarManagerAdmin)manager).newEvent(data);
    }

}
