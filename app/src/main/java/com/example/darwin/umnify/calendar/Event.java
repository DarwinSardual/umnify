package com.example.darwin.umnify.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by darwin on 9/7/17.
 */

public class Event {

    private int id;
    private String name;
    private String description;
    private Calendar dateFrom;
    private Calendar dateTo;
    private int poseidon;

    public Event(int id, String name, String description, Calendar dateFrom,
                 Calendar dateTo, int poseidon){
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.poseidon = poseidon;
    }

    public Calendar getDateFrom() {
        return dateFrom;
    }

    public Calendar getDateTo() {
        return dateTo;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getPoseidon() {
        return poseidon;
    }
}
