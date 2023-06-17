package com.trucksoft.isoft.isoft_elog.Model_class;

import java.io.Serializable;
import java.util.List;

public class Dvir_view_model implements Serializable {
    public String status;
    public String id;
    public String savedate;
    public String carrier;
    public String carrier_address;
    public String Date_Time;
    public String editmode;
    public String Truck_No;
    public String Odometer_Reading;
    public String Trailer;
    public String Remark;
    public String CONDITION_OF_THE;
    public String Driver_Name;
    public String Driver_Signature;
    public String Driver_date;
    public String Driver_address;
    public String ABOVE_DEFECTS_CORRECTED;
    public String ABOVE_DEFECTS_NEED_NOT_BE_CORRECTED;
    public String Mechanics_Name;
    public String Mechanics_Signature;
    public String Mechanics_date;
    public String Mechanics_address;
    public List<Truckitem_model> truck_item_details;
    public String truck_other_status;
    public String truck_other;
    //public String trailer_item_details;
    public List<Traileritem_model> trailer_item_details;
    public String trailer_other_status;
    public String trailer_other;
    public String image_photo;
    public String imgaddress;
    public String imgdate;
    public String tripopt;
    //public String truck_img;


    public String question;
    public String answer;
}
