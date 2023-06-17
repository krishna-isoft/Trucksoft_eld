package com.trucksoft.isoft.isoft_elog.Model_class;

import com.isoft.trucksoft_elog.Isoft_activity.Graphweekmodel;

import java.io.Serializable;
import java.util.List;

public class Graphmodel implements Serializable {
    public String date;
    public String sdriving;
    public String soffdutty;
    public String ssleep;
    public String sonduty;
    public String imgurl;
    public String freehrs;
    public String dailymeter;
    public String weeklymeter;
   // public String fweek;
    public List<Graphweekmodel> fweek;
    public int weekoff;
    public int weeksleep;
    public int weekon;
    public int weekdrive;
}