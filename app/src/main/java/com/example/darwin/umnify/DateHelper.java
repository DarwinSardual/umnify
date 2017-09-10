package com.example.darwin.umnify;

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

}
