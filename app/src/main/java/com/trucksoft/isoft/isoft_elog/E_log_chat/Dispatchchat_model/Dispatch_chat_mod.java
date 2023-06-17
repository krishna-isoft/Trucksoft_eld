package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Dispatch_chat_mod implements Serializable {
    String id, name, lastMessage, timestamp,stronline,img_src;
    int unreadCount;

    public Dispatch_chat_mod() {
    }

    public Dispatch_chat_mod(String id, String name, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOnline() {
        return stronline;
    }

    public void setOnline(String stronline) {
        this.stronline = stronline;
    }

    public String getImage() {
        return img_src;
    }

    public void setImage(String src_img) {
        this.img_src = src_img;
    }


}
