package com.trucksoft.isoft.isoft_elog.driverchecklist;

import java.io.Serializable;

public class Response_model implements Serializable {
    String status;
    String message;


    public String getStatus() {
        return status;
    }



    public String getMessage() {
        return message;
    }
}