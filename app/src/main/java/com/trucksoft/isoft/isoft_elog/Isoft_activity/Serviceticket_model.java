package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import java.io.Serializable;

public class Serviceticket_model implements Serializable {
    String id;
    String drvid;
    String stid;
    String company;
    String device;
    String vehicle;
    String nickname;
    String truckno;
    String date_reported;
    String phone;
    String email;
    String attachments;
    String img;
    String message;
    String status;

    String description;
    String remark;

    String service_rate;
    String date_fixed;
    String parent_id;
    String type;




    public Serviceticket_model(String type)
    {
        this.type=type;
    }
}
