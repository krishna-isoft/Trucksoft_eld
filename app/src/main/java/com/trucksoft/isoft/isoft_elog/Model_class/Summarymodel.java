package com.trucksoft.isoft.isoft_elog.Model_class;

import java.io.Serializable;
import java.util.List;

public class Summarymodel implements Serializable {

    public List<todaymodel> today;
    public List<weeklymodel> week;
//    public todaymodel today;
//    public weeklymodel week;
    public String status;
    public String rule;
    public String current_status;
    public String certify;
    public String certifydate;
    public String sevenDaysCertify;
    public String trip_num;
    public String imgurl;
    public String today_count;

}