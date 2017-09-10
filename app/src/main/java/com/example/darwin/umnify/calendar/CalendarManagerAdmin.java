package com.example.darwin.umnify.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.calendar.data_action_wrapper.AddEventDataActionWrapper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by darwin on 9/8/17.
 */

public class CalendarManagerAdmin extends CalendarManager {

    private FloatingActionButton fab;
    private Bundle userData;

    public CalendarManagerAdmin(Activity activity, MaterialCalendarView calendarView,
                                Bundle userData){
        super(activity, calendarView);
        fab = (FloatingActionButton) super.getActivity().findViewById(R.id.calendar_fab);

        fab.setOnClickListener(new FabActionAdmin(CalendarManagerAdmin.super.getActivity(),
                AddEventActivity.class, null ,CalendarActivity.ADD_EVENT_CODE));
    }

    public void newEvent(Intent data){

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("name", data.getStringExtra("name"));
        textDataOutput.put("description", data.getStringExtra("description"));

        String tempStartDate = DateHelper.parseDate(DateHelper.convertCalendarDateToDbDate(data.getStringExtra("start_date"), "-"), 3);
        String tempEndDate = DateHelper.parseDate(DateHelper.convertCalendarDateToDbDate(data.getStringExtra("end_date"), "-"), 3);
        textDataOutput.put("start_date", tempStartDate);
        textDataOutput.put("end_date", tempEndDate);

        WebServiceAsync async = new WebServiceAsync();
        AddEventDataActionWrapper addEventDataActionWrapper =
                new AddEventDataActionWrapper(textDataOutput, super.getActivity());

        async.execute(addEventDataActionWrapper);

    }

    public void init(){
        setDateClick();
        super.setMonthChanged();
    }

    protected void setDateClick(){
        getCalendarView().setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                String key = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if(cacheDayEvent.containsKey(key)){

                    List<Event> tempEventList = cacheDayEvent.get(key);
                    Bundle bundle = new Bundle();

                    for(Event e : tempEventList){

                        Bundle data = new Bundle();
                        data.putInt("id", e.getId());
                        data.putString("name", e.getName());
                        data.putString("description", e.getDescription());
                        data.putString("start_date", sdf.format(e.getDateFrom().getTime()));
                        data.putString("end_date", sdf.format(e.getDateTo().getTime()));

                        bundle.putBundle(e.getId() + "", data);

                    }

                    ViewEventDialogAdmin viewEventDialog = new ViewEventDialogAdmin();
                    viewEventDialog.setArguments(bundle);
                    viewEventDialog.show(CalendarManagerAdmin.super.getActivity().getFragmentManager(), "View event");

                }
            }
        });
    }
}
