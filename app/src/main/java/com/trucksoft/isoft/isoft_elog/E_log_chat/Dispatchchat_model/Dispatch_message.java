package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Dispatch_message implements Serializable {
    String id, message, createdAt,online,chstatus,attachment,file;
    Dispatchuser user;

    public Dispatch_message() {
    }

    public Dispatch_message(String id, String message, String createdAt, String online, Dispatchuser user,String attachment,String file) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
        this.online=online;
        this.attachment=attachment;
        this.file=file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getOnline() {
        return online;
    }

    public void setonline(String uonline) {
        this.online = uonline;
    }
    public Dispatchuser getUser() {
        return user;
    }

    public void setUser(Dispatchuser user) {
        this.user = user;
    }



    public String getchstatus() {
        return chstatus;
    }

    public void setchstatus(String chstatus) {
        this.chstatus = chstatus;
    }

    public String getattachment() {
        return attachment;
    }

    public void setattachment(String attachment) {
        this.attachment = attachment;
    }
    public String getfile() {
        return file;
    }

    public void setfile(String file) {
        this.file = file;
    }

}
