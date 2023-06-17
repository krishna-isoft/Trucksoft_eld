package com.trucksoft.isoft.isoft_elog.breakrep;

import java.io.Serializable;

public class Breakrep_model implements Serializable {
    String date;
    String driver;
    String type;
    String status;
    String time;
    String time_taken;
    String file;
    String loadtype;



    public Breakrep_model(String type)
    {
        this.loadtype=type;
    }
}
