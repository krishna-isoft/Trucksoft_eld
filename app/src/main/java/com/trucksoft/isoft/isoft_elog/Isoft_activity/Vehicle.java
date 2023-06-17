package com.trucksoft.isoft.isoft_elog.Isoft_activity;

public class Vehicle {
    public String respstatus;
    public String model_name;
    public String vehicle_image;
    public String comp_id;
    public String vin;
    public String vid;
    public String status;
    public String driver_name;

    public Vehicle(String model_name,String vehicle_image,String comp_id,
                   String vin,String vid,String status,String driver_name) {
        this.model_name = model_name;
        this.vehicle_image = vehicle_image;
        this.comp_id = comp_id;
        this.vin=vin;
        this.vid=vid;
        this.status=status;
        this.driver_name = driver_name;

    }

    @Override
    public String toString() {
        return model_name;
    }

    public String toVin() {
        return vin;
    }
}
