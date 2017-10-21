package com.example.darwin.umnify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by darwin on 9/7/17.
 */

public class DateHelper {

    public static int[] convertDbDateToCalendarDate(String date, String seperator){

        int[] dateArray = splitAndConvertDate(date, seperator);
        dateArray[1]--;
        return dateArray;

    }

    public static int[] convertCalendarDateToDbDate(String date, String seperator){

        int[] dateArray = splitAndConvertDate(date, seperator);
        dateArray[1]++;
        return dateArray;

    }

    public static int [] splitAndConvertDate(String date, String seperator){

        int[] dateArray = new int[3];

        String [] str = date.split("-");
        dateArray[0] = Integer.parseInt(str[0]);
        dateArray[1] = Integer.parseInt(str[1]);
        dateArray[2] = Integer.parseInt(str[2]);

        return dateArray;
    }

    public static String parseDate(int[] dateArray, int length){

        StringBuilder builder = new StringBuilder("");

        for(int counter = 0; counter < length; counter++){
            builder.append(dateArray[counter]);

            if(counter < (length - 1))
                builder.append("-");
        }

        return builder.toString();
    }

    public static String convertDateToMDY(String date){

        SimpleDateFormat monthDateYearFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String monthDateYear = null;

        try{

            Date mdy = sdf.parse(date);
            monthDateYear = monthDateYearFormat.format(mdy);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return monthDateYear;
    }

    public static String convert24Hourto12Hour(String time){
        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
        String display = null;

        try{
            Date dtime = parseFormat.parse(time);
            display = displayFormat.format(dtime);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return display;
    }

}
