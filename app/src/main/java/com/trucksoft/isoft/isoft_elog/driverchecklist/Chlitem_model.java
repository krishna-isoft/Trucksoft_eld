package com.trucksoft.isoft.isoft_elog.driverchecklist;

public class Chlitem_model {
    private String status;
    private String id;
    private String item_name;
    private String urimg="";
    private String strconvert="";
    private int selectstatus=0;

    public String getStatus() {
        return status;
    }
    public void setStatus(String id) {
        this.status = id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }
    public void setItem_name(String id) {
        this.item_name = id;
    }

    public String getUrimg()
    {
        return urimg;
    }
    public void setUrimg(String iu)
    {
        this.urimg=iu;
    }

    public int getSelectstatus()
    {
        return selectstatus;
    }
    public void setSelectstatus (int stat)
    {
        this.selectstatus=stat;
    }
    public String getStrconvert()
    {
        return strconvert;
    }
    public void setStrconvert(String iu)
    {
        this.strconvert=iu;
    }

}
