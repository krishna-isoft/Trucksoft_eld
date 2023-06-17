package com.trucksoft.isoft.isoft_elog.fuel_receipt;

import java.io.Serializable;

public class Receipt_model implements Serializable {
    String id;
    String driverid;
    String vin;
    String image;
    String bill_date;
    String address;
    String type;

    public Receipt_model(String type)
    {
        this.type=type;
    }
}
