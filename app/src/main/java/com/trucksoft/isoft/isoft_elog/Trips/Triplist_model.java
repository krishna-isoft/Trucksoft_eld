package com.trucksoft.isoft.isoft_elog.Trips;

import java.io.Serializable;

public class Triplist_model implements Serializable {
    String nickname;
    String vehicle;
    String date;
    String timefrom;
    String timeto;
    String start_from;
    String end_to;
    String type;
    String triptype;
    String trip_id;
    String d_type;
    String miles;



    public Triplist_model(String type)
    {
        this.triptype=type;
    }
}
