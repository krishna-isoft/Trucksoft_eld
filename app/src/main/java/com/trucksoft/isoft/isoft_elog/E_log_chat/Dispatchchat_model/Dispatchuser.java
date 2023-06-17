package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Dispatchuser implements Serializable {
    String id, name, email;

    public Dispatchuser() {
    }

    public Dispatchuser(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
