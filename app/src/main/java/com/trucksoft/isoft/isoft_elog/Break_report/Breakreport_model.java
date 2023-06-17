package com.trucksoft.isoft.isoft_elog.Break_report;

import java.io.Serializable;

public class Breakreport_model implements Serializable {
    String date;
    String break_id;
    String break_method;
    String rule;
    String address;
    String state;
    String type;
    String note_type;

    String taken_time;
    String release_time;
    String duration;
    String status;
    String file;
    String reason;



    public Breakreport_model(String note_type)
    {
        this.note_type=note_type;
    }
}
