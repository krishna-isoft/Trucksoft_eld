package com.trucksoft.isoft.isoft_elog.Model_class;

import java.io.Serializable;

public class Recycle_vehiclemodel implements Serializable {
    public String respstatus;
    public String model_name;
    public String vehicle_image;
    public String comp_id;
    public String vin;
    public String vid;
    public String status;
    public String driver_name;
    public String type;
    public Recycle_vehiclemodel (String type)
    {
        this.type=type;
    }
}
