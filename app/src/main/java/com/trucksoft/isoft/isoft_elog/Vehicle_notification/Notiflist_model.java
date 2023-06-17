package com.trucksoft.isoft.isoft_elog.Vehicle_notification;

import java.io.Serializable;

public class Notiflist_model implements Serializable {
    String vehicle_key;
    String truck;
    String slno;
    String type;
    String title;
    String message;
    String created_on;
    String note_type;



    public Notiflist_model(String note_type)
    {
        this.note_type=note_type;
    }
}
