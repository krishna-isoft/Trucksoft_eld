package com.trucksoft.isoft.isoft_elog.Model_class;

import java.io.Serializable;
import java.util.List;

public class Getvalue_model implements Serializable {
    String date;
    String sdriving;

    String soffdutty;
    String ssleep;
    String sonduty;

    String onftime;
    String onttime;
    String onid;
    String onstatus;

    String drftime;
    String drttime;
    String drid;
    String drstatus;


    String offtime;
    String ofttime;
    String ofid;
    String ofstatus;

    String slftime;
    String slttime;
    String slid;
    String slstatus;

    String status;
    String pcstatus;
    String remark;
    String soldsleep;
    String resethr;

    String breakduration;
    String breaklive;
    String tenresethr;
    String eldstatus;
    String voiceoff;
    String voiceon;
    String voicesleep;
    String voicedrive;
    String rule;

    String versioncode;
    String versionname;
    String d_type;
//String comp_notification;
//    String app_notification;


//    String comp_not_title;
//    String app_not_title;
    String blu_address;
    String blu_name;

//    String comp_not_id;
//    String app_not_id;
    String blu_time;
    String blu_speedtime;
    String blu_scanning;
    String lastlogstatus;
    String loc_interval;
    String total_unread;
    String msgval;
    String login_status="";
    List<Break_info_model> break_info;

    private List<Getvalue_model> sponsors = null;
    public List<Getvalue_model> getSponsors() {
        return sponsors;
    }

    public String getdate() {
        return date;
    }
    public void setdate(String id) {
        this.date = id;
    }

    public void setsdriving(String idz) {
        this.sdriving = idz;
    }
    public String getsdriving() {
        return sdriving;
    }

    public void setsoffdutty(String idz) {
        this.soffdutty = idz;
    }
    public String getsoffdutty() {
        return soffdutty;
    }

    public String getssleep() {
        return ssleep;
    }
    public void setssleep(String idz) {
        this.ssleep = idz;
    }

    public String getsonduty() {
        return sonduty;
    }
    public void setsonduty(String idz) {
        this.sonduty = idz;
    }

    //1
    public void setonftime(String idz) {
        this.onftime = idz;
    }
    public String getonftime() {
        return onftime;
    }

    public void setonttime(String idz) {
        this.onttime = idz;
    }
    public String getonttime() {
        return onttime;
    }

    public String getonid() {
        return onid;
    }
    public void setonid(String idz) {
        this.onid = idz;
    }

    public String getonstatus() {
        return onstatus;
    }
    public void setonstatus(String idz) {
        this.onstatus = idz;
    }

    //2

    public void setdrftime(String idz) {
        this.drftime = idz;
    }
    public String getdrftime() {
        return drftime;
    }

    public void setdrttime(String idz) {
        this.drttime = idz;
    }
    public String getdrttime() {
        return drttime;
    }

    public String getdrid() {
        return drid;
    }
    public void setdrid(String idz) {
        this.drid = idz;
    }

    public String getdrstatus() {
        return drstatus;
    }
    public void setdrstatus(String idz) {
        this.drstatus = idz;
    }
    //3

    public void setofftime(String idz) {
        this.offtime = idz;
    }
    public String getofftime() {
        return offtime;
    }

    public void setofttime(String idz) {
        this.ofttime = idz;
    }
    public String getofttime() {
        return ofttime;
    }

    public String getofid() {
        return ofid;
    }
    public void setofid(String idz) {
        this.ofid = idz;
    }

    public String getofstatus() {
        return ofstatus;
    }
    public void setofstatus(String idz) {
        this.ofstatus = idz;
    }
    //4
    public void setslftime(String idz) {
        this.slftime = idz;
    }
    public String getslftime() {
        return slftime;
    }

    public void setslttime(String idz) {
        this.slttime = idz;
    }
    public String getslttime() {
        return slttime;
    }

    public String getslid() {
        return slid;
    }
    public void setslid(String idz) {
        this.slid = idz;
    }

    public String getslstatus() {
        return slstatus;
    }
    public void setslstatus(String idz) {
        this.slstatus = idz;
    }

    public String getstatus() {
        return status;
    }
    public String getpreviousstatus() {
        return login_status;
    }
    public void setstatus(String idz) {
        this.pcstatus = idz;
    }
    public String getpcstatus() {
        return pcstatus;
    }
    public void setpcstatus(String idz) {
        this.pcstatus = idz;
    }


    public String getremark() {
        return remark;
    }
    public void setRemark(String idz) {
        this.remark = idz;
    }


    public String getsoldsleep() {
        return soldsleep;
    }
    public void setsoldsleep(String idz) {
        this.soldsleep = idz;
    }
    public String getResethr() {
        return resethr;
    }
    public void setResethr(String idz) {
        this.resethr = idz;
    }

    public String getBreakduration() {
        return breakduration;
    }
    public void setBreakduration(String idz) {
        this.breakduration = idz;
    }

    public String getBreaklive() {
        return breaklive;
    }
    public void setBreaklive(String idz) {
        this.breaklive = idz;
    }
    public String getTenresethr() {
        return tenresethr;
    }
    public void setTenresethr(String idz) {
        this.tenresethr = idz;
    }

    public String getEldstatus() {
        return eldstatus;
    }
    public void setEldstatus(String idz) {
        this.eldstatus = idz;
    }


    public String getVoiceoff() {
        return voiceoff;
    }
    public void setVoiceoff(String idz) {
        this.voiceoff = idz;
    }

    public String getVoiceon() {
        return voiceon;
    }
    public void setVoiceon(String idz) {
        this.voiceon = idz;
    }

    public String getVoicesleep() {
        return voicesleep;
    }
    public void setVoicesleep(String idz) {
        this.voicesleep = idz;
    }

    public String getVoicedrive() {
        return voicedrive;
    }
    public void setVoicedrive(String idz) {
        this.voicedrive = idz;
    }
    public String getRule() {
        return rule;
    }
    public void setRule(String idz) {
        this.rule = idz;
    }

    public String getVersioncode() {
        return versioncode;
    }
    public String getVersionname() {
        return versionname;
    }

    public String getD_type() {
        return d_type;
    }
//    public String getComp_notification() {
//        return comp_notification;
//    }
//    public String getApp_notification() {
//        return app_notification;
//    }
//    public String getComp_not_title() {
//        return comp_not_title;
//    }
//    public String getApp_not_title() {
//        return app_not_title;
//    }

    public String getBlu_address() {
        return blu_address;
    }

    public String getBlu_name() {
        return blu_name;
    }
//    public String getComp_not_id() {
//        return comp_not_id;
//    }
//    public String getApp_not_id() {
//        return app_not_id;
//    }

    public String getBlu_time() {
        return blu_time;
    }

    public String getBlu_speedtime() {
        return blu_speedtime;
    }
    public String getBlu_scanning() {
        return blu_scanning;
    }
    public String getLastlogstatus() {
        return lastlogstatus;
    }
    public String getLoc_interval() {
        return loc_interval;
    }
    public String getUnread_app() {
        return total_unread;
    }

    public  List<Break_info_model> getbreakinfo()
    {
        return break_info;
    }

    public String getMsgval() {
        return msgval;
    }
}
