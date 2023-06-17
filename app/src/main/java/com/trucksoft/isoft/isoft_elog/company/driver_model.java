package com.trucksoft.isoft.isoft_elog.company;

import java.io.Serializable;

public class driver_model implements Serializable {
    String staff_id;
    String fname;
    String lname;
    String loc_interval;
    String e_mail;
    String is_admin;
    String license;
    String staff_name;
    String phone;
    String address;
    String city;
    String state;
    String zip;
    String note;
    String dob;
    String regnumber;
    String ccode;
    String app_version;
    String type;




    public driver_model(String type)
    {
        this.type=type;
    }
}
