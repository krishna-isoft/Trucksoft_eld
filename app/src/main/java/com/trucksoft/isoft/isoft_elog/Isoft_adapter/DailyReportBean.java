package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

/**
 * Created by isoft on 30/5/17.
 */

public class DailyReportBean {


    private String dstatus;
    private String dstart;
    private String dduration;

    private String address;

    private String slong;



    public void setstatus(String dstatus) {
        this.dstatus = dstatus;
    }
    public String getstatus() {
        return dstatus;
    }

    public void setstart(String dstart) {
        this.dstart = dstart;
    }
    public String getstart() {
        return dstart;
    }

    public void setduration(String on_line) {
        this.dduration = on_line;
    }
    public String getduration() {
        return dduration;
    }

    public void setaddress(String slat) {
        this.address = slat;
    }
    public String getaddress() {
        return address;
    }







}

