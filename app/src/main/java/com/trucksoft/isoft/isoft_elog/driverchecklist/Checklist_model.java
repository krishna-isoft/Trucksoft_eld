package com.trucksoft.isoft.isoft_elog.driverchecklist;

import java.io.Serializable;

public class Checklist_model implements Serializable {
    String id;
    String type;
    String carrier;
    String tractor;
    String truck_no;
    String odometer_reading;
    String date;
    String trashmode;
    String tripopt;
    String editmode;
    String msg;
    String lastindex;
    String savedate;
    String datedd;
    String address;
    int tcount;



    public Checklist_model (String type)
    {
        this.type=type;
    }
}
