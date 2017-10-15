package com.example.darwin.umnify.calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.CalendarView;

import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.calendar.data_action_wrapper.FetchCurrentDateDataActionWrapper;
import com.example.darwin.umnify.calendar.data_action_wrapper.FetchEventDataActionWrapper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by darwin on 9/7/17.
 */

public class CalendarManager {

    protected HashMap<Integer, Event> events;
    protected HashMap<String, List<Event>> cacheDayEvent;
    private Activity activity;

    private MaterialCalendarView calendarView;
    private CurrentDateDecorator currentDateDecorator;
    private EventDateDecorator eventDateDecorator;

    public CalendarManager(Activity activity, MaterialCalendarView calendarView){

        this.activity = activity;
        this.calendarView = calendarView;
        events = new HashMap<>();
        cacheDayEvent = new HashMap<>();
        List<CalendarDay> decoratorDays = new ArrayList<>();
        eventDateDecorator = new EventDateDecorator(activity, R.drawable.calendar_circle_decorator,
                decoratorDays);

        calendarView.addDecorator(eventDateDecorator);

        //fetch current date
        // after that, fetch event on that month


        WebServiceAsync asyncFetchCurrentDate = new WebServiceAsync();
        FetchCurrentDateDataActionWrapper fetchCurrentDateDataActionWrapper =
                new FetchCurrentDateDataActionWrapper(activity, this);

        asyncFetchCurrentDate.execute(fetchCurrentDateDataActionWrapper);
    }

    public void init(){

        setDateClick();
        setMonthChanged();
    }

    protected void setDateClick(){
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
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

                    ViewEventDialog viewEventDialog = new ViewEventDialog();
                    viewEventDialog.setArguments(bundle);
                    viewEventDialog.show(CalendarManager.this.activity.getFragmentManager(), "View event");

                }

            }
        });
    }

    protected void setMonthChanged(){

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                HashMap<String, String> textDataOutput = new HashMap<>();
                textDataOutput.put("month", (date.getMonth() + 1) + "");
                textDataOutput.put("year", date.getYear() + "");

                WebServiceAsync asyncFetchEvents =
                        new WebServiceAsync();
                FetchEventDataActionWrapper fetchEventDataActionWrapper =
                        new FetchEventDataActionWrapper(textDataOutput, activity, CalendarManager.this);

                asyncFetchEvents.execute(fetchEventDataActionWrapper);
            }
        });

    }

    public void setFirstCurrentDate(int year, int month, int day){

        CalendarDay date = new CalendarDay(year, month, day);

        setCurrentDate(date);

        // fetch all the events for this month

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("month", (month + 1) + "");
        textDataOutput.put("year", year + "");

        WebServiceAsync asyncFetchEvents =
                new WebServiceAsync();
        FetchEventDataActionWrapper fetchEventDataActionWrapper =
                new FetchEventDataActionWrapper(textDataOutput, activity, this);

        asyncFetchEvents.execute(fetchEventDataActionWrapper);
    }

    public void setCurrentDate(CalendarDay date){

        List<CalendarDay> currentDate = new ArrayList<>();
        currentDate.add(date);

        currentDateDecorator = new CurrentDateDecorator(Color.RED, currentDate);
        calendarView.setCurrentDate(date);
        calendarView.addDecorator(currentDateDecorator);
    }

    public void addEvent(String jsonData) throws JSONException{


        JSONObject data = new JSONObject(jsonData);

        if(events.containsKey(data.getInt("id")))
            return;

        int[] dateFromArray = DateHelper.convertDbDateToCalendarDate(data.getString("date_from"), "-");
        int[] dateToArray = DateHelper.convertDbDateToCalendarDate(data.getString("date_to"), "-");

        Calendar dateFrom = Calendar.getInstance();
        Calendar dateTo = Calendar.getInstance();

        dateFrom.set(dateFromArray[0], dateFromArray[1], dateFromArray[2]);
        dateTo.set(dateToArray[0], dateToArray[1], dateToArray[2]);

        Event event = new Event(data.getInt("id"), data.getString("name"), data.getString("description"),
                dateFrom, dateTo, 0);

        events.put(event.getId(), event);

        Calendar dateFromCopy = (Calendar) dateFrom.clone();;
        Calendar dateToCopy = (Calendar) dateTo.clone();

        // decorate events

        while(dateFromCopy.compareTo(dateToCopy) <= 0){

            CalendarDay eventDay = new CalendarDay(dateFromCopy.get(Calendar.YEAR),
                    dateFromCopy.get(Calendar.MONTH), dateFromCopy.get(Calendar.DATE));

            String key = dateFromCopy.get(Calendar.YEAR) + "-" +
            dateFromCopy.get(Calendar.MONTH) + "-" + dateFromCopy.get(Calendar.DATE);

            if(!cacheDayEvent.containsKey(key)){
                cacheDayEvent.put(key, new ArrayList<Event>());
            }

            cacheDayEvent.get(key).add(event);
            dateFromCopy.add(Calendar.DATE, 1);
            eventDateDecorator.addToDecorate(eventDay);

        }
    }

    public void addEvents(String jsonDataArray) throws JSONException{

        JSONArray dataArray = new JSONArray(jsonDataArray);
        for(int counter = 0; counter < dataArray.length(); counter++){

            String data = dataArray.getString(counter);
            addEvent(data);
        }

        calendarView.invalidateDecorators();
    }

    public MaterialCalendarView getCalendarView() {
        return calendarView;
    }

    public Activity getActivity() {
        return activity;
    }
}
