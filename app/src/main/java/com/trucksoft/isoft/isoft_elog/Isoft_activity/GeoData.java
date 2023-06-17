package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import java.io.Serializable;

public class GeoData implements Serializable {

    private Double vehicleSpeed; //vehicle Speed

    /**
     * Constructor.
     * By default, all properties begin with null values,
     * and the unidentified events are empty.
     */
    public GeoData() {

        vehicleSpeed = 0.0; //vehicle Speed

    }


    /**
     *
     * @return gets the vehicle speed.
     */
    public Double getVehicleSpeed(){
        return this.vehicleSpeed;
    }

    /**
     * {@link com.geometris.wqlib.GeoData#getVehicleSpeed()}
     * @param vehicleSpeed value to set vehicle speed
     * @return returns this object.
     */
    public void setVehicleSpeed(Double vehicleSpeed){
        this.vehicleSpeed=vehicleSpeed;


    }







}
