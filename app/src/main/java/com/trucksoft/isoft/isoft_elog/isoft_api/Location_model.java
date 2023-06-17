package com.trucksoft.isoft.isoft_elog.isoft_api;

import java.io.Serializable;

public class Location_model implements Serializable {
    String endlat;
    String endlong;
    String model_name;
    String address;
    public void setendlat(String idz) {
        this.endlat = idz;
    }
    public String getendlat() {
        return endlat;
    }

    public void setendlong(String idz) {
        this.endlong = idz;
    }
    public String getendlong() {
        return endlong;
    }

    public String getmodel_name() {
        return model_name;
    }
    public void setmodel_name(String idz) {
        this.model_name = idz;
    }

    public String getaddress() {
        return address;
    }
    public void setaddress(String idz) {
        this.address = idz;
    }

}

