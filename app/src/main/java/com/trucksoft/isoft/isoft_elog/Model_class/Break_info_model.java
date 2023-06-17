package com.trucksoft.isoft.isoft_elog.Model_class;

import java.io.Serializable;

public class Break_info_model implements Serializable {
    String brk_id;
    String rule;
    String type;
    String how_many_minutes;
    String remainder;
    String remainderto;
    String message;
    String alert_interval;
    String exptime;
    String estatus;
    String first_status;
    String first_status_time;
    String till_now;
    String break_status;
    String break_taken_id;
    public String getBrk_id() {
        return brk_id;
    }
    public String getRule() {
        return rule;
    }
    public String getType() {
        return type;
    }
    public String getHow_many_minutes() {
        return how_many_minutes;
    }
    public String getRemainder() {
        return remainder;
    }public String getRemainderto() {
        return remainderto;
    }
    public String getMessage() {
        return message;
    }
    public String getAlert_interval() {
        return alert_interval;
    }
    public String getExptime() {
        return exptime;
    }
    public String getEstatus() {
        return estatus;
    }
    public String getFirst_status() {
        return first_status;
    }
    public String getFirst_status_time() {
        return first_status_time;
    }
    public String getTill_now() {
        return till_now;
    }
    public String getBreak_status() {
        return break_status;
    }
    public String getBreak_taken_id() {
        return break_taken_id;
    }
}
