package com.trucksoft.isoft.isoft_elog.isoft_api;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Break_report.Breakreport_model;
import com.isoft.trucksoft_elog.Isoft_activity.Responsemodel;
import com.isoft.trucksoft_elog.Isoft_activity.Serviceticket_model;
import com.isoft.trucksoft_elog.Isoft_activity.reportz_model;
import com.isoft.trucksoft_elog.Isoft_adapter.state_model;
import com.isoft.trucksoft_elog.Model_class.Battery_model;
import com.isoft.trucksoft_elog.Model_class.Daily_reportmodel;
import com.isoft.trucksoft_elog.Model_class.Dvir_view_model;
import com.isoft.trucksoft_elog.Model_class.Faultcode_model;
import com.isoft.trucksoft_elog.Model_class.Fuel_model;
import com.isoft.trucksoft_elog.Model_class.Getvalue_model;
import com.isoft.trucksoft_elog.Model_class.MainException_model;
import com.isoft.trucksoft_elog.Model_class.Message_model;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Model_class.Res_model;
import com.isoft.trucksoft_elog.Model_class.Respp_model;
import com.isoft.trucksoft_elog.Model_class.ServerResponse;
import com.isoft.trucksoft_elog.Model_class.Summarymodel;
import com.isoft.trucksoft_elog.Model_class.Update_vehicle;
import com.isoft.trucksoft_elog.Model_class.Vehicle_model;
import com.isoft.trucksoft_elog.Model_class.ifta_model;
import com.isoft.trucksoft_elog.Model_class.newbrk_model;
import com.isoft.trucksoft_elog.Model_class.version_model;
import com.isoft.trucksoft_elog.Trips.Triplist_model;
import com.isoft.trucksoft_elog.Vehicle_notification.Notiflist_model;
import com.isoft.trucksoft_elog.breakrep.Breakrep_model;
import com.isoft.trucksoft_elog.company.root_model;
import com.isoft.trucksoft_elog.driverchecklist.Checklist_model;
import com.isoft.trucksoft_elog.driverchecklist.Chlitem_model;
import com.isoft.trucksoft_elog.driverchecklist.Item_model;
import com.isoft.trucksoft_elog.driverchecklist.Response_model;
import com.isoft.trucksoft_elog.driverchecklist.Trailor_model;
import com.isoft.trucksoft_elog.fuel_receipt.Receipt_model;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Eld_api {
    @GET("driver_checklist_home_recy.php")
    Call<List<Checklist_model>> getMovies(@Query("index") int index, @Query("page") String page, @Query("did") String did
            , @Query("cc") String cc, @Query("typefld") String typefld
            , @Query("lat") String lat, @Query("lon") String lon, @Query("address") String address, @Query("state") String state
            , @Query("feature") String feature);

    @GET("list_service_tickets.php")
    Call<List<Serviceticket_model>> getServicetickets( @Query("page") String page, @Query("driver") String did
            , @Query("cc") String cc,@Query("lat") String lat,@Query("lon") String lon,@Query("address") String address
            ,@Query("state") String state,@Query("feature") String feature);

    @FormUrlEncoded
    @POST("upload_fuelbill.php")
    Call<JsonObject> uploadfuelbill(@Field("did") String driver, @Field("fuelbill") String photos,
             @Field("vin") String vin, @Field("bill_date") String bill_date, @Field("address") String address);



    @GET("truck_items.php")
    Call<List<Chlitem_model>> gettruckitems(@Query("did") String did
            , @Query("cc") String cc, @Query("typefld") String typefld);

    @GET("trailer_items.php")
    Call<List<Chlitem_model>> gettraileritems(@Query("did") String did
            , @Query("cc") String cc, @Query("typefld") String typefld);

    @GET("get_itemscount.php")
    Call<Item_model> getitemcount();



    @Multipart
    @POST("trailer_details.php")
    Call<ResponseBody> gettrailerdetails(@Part("typefld") RequestBody typefld
    ,@Part("did") RequestBody did,@Part("license") RequestBody license,@Part("ccode") RequestBody ccode);

    @GET("get_mylocation.php")
    Call<List<Response_model>> getvehicle(@Query("vin") String ccode);
    @POST("trailer_insert.php")
    @FormUrlEncoded

    Call<Trailor_model> getUserInfoRequest(@FieldMap Map<String,String> params);


    @GET("driver_trips.php")
    Call<List<Triplist_model>> getTrips(@Query("driver") String driver, @Query("from") String from
            , @Query("to") String to, @Query("page") String page, @Query("cc") String cc, @Query("val") String val);

    @GET("list_fuel_bill.php")
    Call<List<Receipt_model>> getFuellist(@Query("driver") String driver, @Query("page") String page, @Query("cc") String cc
            , @Query("lat") String lat, @Query("lon") String lon, @Query("address") String address, @Query("state") String state,@Query("feature") String feature);

    @GET("list_notification.php")
    Call<List<Notiflist_model>> getNotificationlist(@Query("driver") String driver, @Query("from") String from
            , @Query("to") String to, @Query("page") String page, @Query("cc") String cc, @Query("vin") String vin
            , @Query("lat") String lat, @Query("lon") String lon, @Query("address") String address, @Query("state") String state
            ,@Query("feature") String feature);

    @GET("list_break.php")
    Call<List<Breakrep_model>> getBreakreport(@Query("did") String driver, @Query("page") String page, @Query("cc") String cc);

    @GET("eld_breaks_taken.php")
    Call<List<Breakreport_model>> getBreakreportlist(@Query("did") String driver, @Query("from") String from
            , @Query("to") String to, @Query("page") String page, @Query("cc") String cc, @Query("vin") String vin
            , @Query("lat") String lat, @Query("lon") String lon, @Query("address") String address, @Query("state") String state
            , @Query("feature") String feature);

    @GET("getDriverList.php")
    Call<root_model> getdriverlist(@Query("page") String page, @Query("ccode") String ccode);

    @GET("get_mylocation.php")
    Call<List<Location_model>> getvehicle(@Query("vin") String ccode,@Query("lat") String lat,@Query("lon") String lon,@Query("address") String address
            ,@Query("state") String state,@Query("feature") String feature);


    @GET("logout_elog.php")

    Call<List<Remark_model>> applogout(
            @Query("did") String did,@Query("vin") String vin,@Query("driverid") String driverid
            ,@Query("lat") String lat,@Query("lon") String lon);

    @Multipart
    @POST("upload_to_server.php")
    Call<JsonObject> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name
            , @Part("did") RequestBody did
            , @Part("brk_id") RequestBody brk_id
            , @Part("state") RequestBody state
            , @Part("address") RequestBody address
            , @Part("testbreak") RequestBody testbreak);


    @GET("eld_thirtyfour.php")

    Call<List<Responsemodel>> getsavethirtyreset_eldnew(@Query("vin") String vin, @Query("fname") String fname,
                                                        @Query("statusid") String statusid, @Query("pc_status") String pc_status,
                                                        @Query("lat") String lat, @Query("lon") String lon, @Query("did") String did, @Query("address") String address, @Query("state") String state
            , @Query("timezonename") String timezonename
            , @Query("timezoneid") String timezoneid
            , @Query("eld_active") String eld_active);

    //34 hr reset release optimized code
    @GET("eld_thirtyfourrelease.php")

    Call<List<Responsemodel>> getenablethirtyreset_eldnew(@Query("vin") String vin,@Query("fname") String fname,
                                                          @Query("statusid") String statusid,@Query("pc_status") String pc_status,
                                                          @Query("lat") String lat,@Query("lon") String lon,@Query("did") String did,
                                                          @Query("address") String address,@Query("state") String state
            ,@Query("eld_active") String eld_active);


    // 10 hr reset optimized
    @GET("eld_tenhour.php")

    Call<List<Responsemodel>> getsavetenhours_eld(@Query("vin") String vin,@Query("fname") String fname,
                                                  @Query("statusid") String statusid,@Query("pc_status") String pc_status,
                                                  @Query("lat") String lat,@Query("lon") String lon,@Query("did") String did,@Query("address") String address ,@Query("state") String state
            ,@Query("timezonename") String timezonename
            ,@Query("timezoneid") String timezoneid,@Query("eld_active") String eld_active);


// 10 hr reset releaseoptimized

    @GET("eld_tenhourrelease.php")

    Call<List<Responsemodel>> releasetenhour_eld(@Query("vin") String vin,@Query("fname") String fname,
                                                 @Query("statusid") String statusid,@Query("pc_status") String pc_status,
                                                 @Query("lat") String lat,@Query("lon") String lon,@Query("did") String did,@Query("address") String address ,@Query("state") String state
            ,@Query("timezonename") String timezonename
            ,@Query("timezoneid") String timezoneid,@Query("eld_active") String eld_active);




    // Auto break accept optimized--
    @GET("eld_acceptautobreak_new.php")
    Call<List<Responsemodel>> getautobreak_eld(@Query("did") String did,@Query("vin") String vin,
                                               @Query("fname") String fname,@Query("pc_status") String pc_status,
                                               @Query("statusid") String statusid,@Query("breakid") String breakid,
                                               @Query("lat") String lat,@Query("lon") String lon,@Query("address") String address, @Query("versiion") String versiion
            ,@Query("state") String state,@Query("eld_active") String eld_active);

    @FormUrlEncoded
    @POST("eld_acceptautobreak_new.php")
    Call<JsonObject> newbreakaccept(@Field("cc") String ccode, @Field("did") String did,
                                    @Field("brk_id") String brk_id,
                                    @Field("type") String type,@Field("rule") String rule
            ,@Field("state") String state,@Field("address") String address
            ,@Field("lat") String lat,@Field("lon") String lon
            ,@Field("trck") String trck,@Field("change_status") String change_status
            ,@Field("testbreak") String testbreak
            ,@Field("break_type") String break_type,@Field("eld_active") String eld_active
    );



    //pc enable optimized
    @GET("eld_pcenable.php")

    Call<List<Responsemodel>> getpcenable_eld(@Query("did") String did,@Query("vin") String vin,@Query("lat") String lat,@Query("lon") String lon,@Query("address") String address, @Query("versiion") String versiion,@Query("state") String state
            ,@Query("timezonename") String timezonename
            ,@Query("timezoneid") String timezoneid,@Query("eld_active") String eld_active);






    //select vehicle using recycler view
    @GET("updatedatausage.php")

    Call<JsonObject> updatedatausage( @Query("driver") String driver,@Query("dusage") String dusage,@Query("vname") String vname,@Query("cc") String cc
            ,@Query("cdate") String cdate);



    @FormUrlEncoded
    @POST("onduty_extend.php")
    Call<JsonObject> updateondutyalertstatus(@Field("driver") String driver, @Field("vin") String vin, @Field("cc") String cc
            , @Field("logid") String logid, @Field("value") String value);







    @FormUrlEncoded
    @POST("edit_serviceticket.php")
    Call<JsonObject> editserviceticket(@Field("did") String driver, @Field("stid") String stid, @Field("remark") String remark
            , @Field("statusstr") String statusstr, @Field("rating") String rating);




    @FormUrlEncoded
    @POST("reopen_serviceticket.php")
    Call<JsonObject> reopenticket(@Field("did") String driver, @Field("stid") String stid, @Field("license") String license
            , @Field("cc") String cc, @Field("dname") String dname, @Field("cname") String cname, @Field("uname") String uname);

    @GET("current_report_scrolling.php")
    Call<List<Daily_reportmodel>> getMovies(@Query("index") int index, @Query("vin")
            String vin, @Query("s_date") String s_date, @Query("page") String page, @Query("did") String did);

    @GET("current_report_scrolling.php")
    Call<List<Daily_reportmodel>> getdaylog(@Query("index") int index, @Query("vin")
            String vin, @Query("s_date") String s_date, @Query("page") String page,@Query("did") String did,@Query("testapi") String testapi);




    @GET("get_all_vehiclelist.php")

    Call<List<Vehicle_model>> getvehicle(@Query("ccode") String ccode, @Query("license") String license);


    @GET("update_vehicle_elog.php")

    Call<List<Update_vehicle>> updatevehicle(@Query("ccode") String ccode, @Query("license") String license, @Query("vin") String vin, @Query("nickname") String nickname, @Query("did") String did);


    @GET("get_remarksave_notify.php")

    Call<List<Remark_model>> saveremark(@Query("vin") String vin,@Query("fname") String fname,
                                        @Query("lat") String lat,@Query("lon") String lon,
                                        @Query("did") String did ,
                                        @Query("message") String message,
                                        @Query("breaktype") String breaktype);
    @GET("remarkupdate.php")

    Call<List<Remark_model>> updateremark(@Query("vin") String vin,@Query("lid") String lid,@Query("msg") String msg,@Query("did") String did);

    @GET("notification_status.php")

    Call<List<Remark_model>> updatenotification(
            @Query("did") String did,@Query("type") String fname);



    //  @GET("get_today_notifaccept_elogtestfgfgjh.php") //just caling purpose
    @GET("get_today_notifaccept_demo.php")
    Call<List<Responsemodel>> getValnotificationtest(@Query("did") String did,@Query("vin") String vin,@Query("lat") String lat,@Query("lon") String lon);

    @GET("get_today_notifaccept_demo.php")
    Call<List<Responsemodel>> getkd();


    @GET("cur_loc_drv.php")

    Call<List<com.isoft.trucksoft_elog.Model_class.Location_model>> updatelocation(
            @Query("driverid") String did,@Query("vin") String vin
            ,@Query("lat") String lat,@Query("long") String lon);


//    @GET("pchart_value.php")
//
//    Call<List<Graphmodel>> getcurrentdaytotal(
//            @Query("did") String did,@Query("s_date") String s_date
//            ,@Query("vin") String vin);



    @GET("eld_pchart_value.php")

    Call<List<Summarymodel>> getsummary(
            @Query("hstate") String hstate,@Query("cstate") String cstate
            ,@Query("did") String did,@Query("s_date") String s_date
            ,@Query("vin") String vin,@Query("cc") String cc,@Query("lat") String lat,@Query("lon") String lon,@Query("address") String address
            ,@Query("state") String state,@Query("feature") String feature);



    @GET("get_enginefaultcode.php")

    Call<List<Faultcode_model>> getenginefaultcode(
            @Query("did") String did,@Query("s_date") String s_date
            ,@Query("vin") String vin,@Query("lat") String lat,@Query("lon") String lon,@Query("address") String address
            ,@Query("state") String state,@Query("feature") String feature);

    @GET("get_batteryhealth.php")

    Call<List<Battery_model>> getbatteryhealth(
            @Query("did") String did,@Query("s_date") String s_date
            ,@Query("vin") String vin,@Query("lat") String lat,@Query("lon") String lon,@Query("address") String address
            ,@Query("state") String state,@Query("feature") String feature);




    //10 hr reset apply app eld
//1 get values

    @GET("eld_getlogvalues.php")
    Call<List<Getvalue_model>> getValues_eld(@Query("did") String did, @Query("vin") String vin, @Query("address") String address, @Query("versiion") String versiion,
                                             @Query("lat") String lat, @Query("lon") String lon, @Query("version_name") String version_name, @Query("state") String state
            , @Query("timezonename") String timezonename
            , @Query("timezoneid") String timezoneid, @Query("screenwidth") String screenwidth, @Query("hval") String hval, @Query("testbreak") String testbreak);

    //2 save log

    @GET("eld_savedata.php")

    Call<List<Getvalue_model>> getsaveValues_eld(@Query("vin") String vin,@Query("fname") String fname,
                                                 @Query("statusid") String statusid,@Query("pc_status") String pc_status,
                                                 @Query("lat") String lat,@Query("lon") String lon,@Query("did") String did,@Query("address") String address, @Query("versiion") String versiion,@Query("state") String state
            ,@Query("timezonename") String timezonename
            ,@Query("timezoneid") String timezoneid
            , @Query("testbreak") String testbreak);
    @GET("eld_savedata.php")

    Call<List<Getvalue_model>> getsaveValues_elds(@Query("vin") String vin,@Query("fname") String fname,
                                                  @Query("statusid") String statusid,@Query("pc_status") String pc_status,
                                                  @Query("lat") String lat,@Query("lon") String lon,@Query("did") String did,@Query("address") String address, @Query("versiion") String versiion,@Query("state") String state
            ,@Query("timezonename") String timezonename
            ,@Query("timezoneid") String timezoneid
            , @Query("testbreak") String testbreak,@Query("remark") String remark);

    @GET("eld_savedata_bluetooth.php")

    Call<List<Getvalue_model>> getsaveValues_eldbluetooth(@Query("vin") String vin,@Query("fname") String fname,
                                                          @Query("statusid") String statusid,@Query("pc_status") String pc_status,
                                                          @Query("lat") String lat,@Query("lon") String lon,@Query("did") String did,@Query("address") String address, @Query("versiion") String versiion,@Query("state") String state
            ,@Query("timezonename") String timezonename
            ,@Query("timezoneid") String timezoneid,@Query("bvalues") String bvalues,@Query("remark") String remark
            ,@Query("trckstatus") String fnames,@Query("trckis") String trckis
            , @Query("testbreak") String testbreak, @Query("frservice") String frservice);

    @GET("eld_savedata_bluetooth.php")

    Call<List<Getvalue_model>> getsaveValues_eldbluetooth(@Query("vin") String vin,@Query("fname") String fname,
                                                          @Query("statusid") String statusid,@Query("pc_status") String pc_status,
                                                          @Query("lat") String lat,@Query("lon") String lon,@Query("did") String did,@Query("address") String address, @Query("versiion") String versiion,@Query("state") String state
            ,@Query("timezonename") String timezonename
            ,@Query("timezoneid") String timezoneid,@Query("bvalues") String bvalues,@Query("remark") String remark
            ,@Query("trckstatus") String fnames,@Query("trckis") String trckis
            , @Query("testbreak") String testbreak);

    @Headers({"Content-type: application/json",
            "Accept: */*"})
    @POST("BT_conn.php")
    Call<Responsemodel> saverecdd(@Body JSONObject driverid, @Query("type") String type);

    //3 auto break accept time


    @GET("get_fuellevel.php")
    Call<List<Fuel_model>> getValues_eld(@Query("did") String did, @Query("vin") String vin, @Query("ccode") String ccode
            , @Query("lat") String lat, @Query("lon") String lon, @Query("address") String address
            , @Query("state") String state, @Query("feature") String feature);



    @GET("driverstate.php")

    Call<List<Faultcode_model>> savestates(
            @Query("driver") String did,@Query("state") String s_date
            ,@Query("vin") String vin,@Query("fromstate") String fromstate,@Query("tostate") String tostate
            ,@Query("lat") String lat,@Query("lon") String lon,@Query("address") String address
            ,@Query("gpstatus") String gpstatus);


    @GET("admin_msgs.php")
    Call<List<Message_model>> getmessages(@Query("did") String driver, @Query("cc") String cc
            , @Query("lat") String lat, @Query("lon") String lon, @Query("address") String address
            , @Query("state") String state, @Query("feature") String feature);

    @GET("driverexceptions.php")
    Call<List<MainException_model>> getexception(@Query("driver") String driver
            , @Query("lat") String lat, @Query("lon") String lon, @Query("address") String address
            , @Query("state") String state, @Query("feature") String feature);
//    @FormUrlEncoded
//    @POST("adddriverexceptions.php")
//    Call<List<Result_model>> addexception(@Field("driver") String driver, @Field("exps") String exps
//            , @Field("reason") String reason);



    @GET("Bth_conn.php")

    Call<Res_model> savebluetoothstatus(@Query("vin") String vin, @Query("bt_status") String bt_status
            , @Query("address") String address, @Query("did") String did
            , @Query("blu_address") String blu_address, @Query("blue_name") String blue_name
            , @Query("odometer") String odometer, @Query("val") String val);

    @GET("Bth_conn.php")

    Call<Res_model> savebluetoothstatusbck(@Query("vin") String vin,@Query("bt_status") String bt_status
            ,@Query("address") String address,@Query("did") String did
            ,@Query("blu_address") String blu_address,@Query("blue_name") String blue_name
            ,@Query("odometer") String odometer,@Query("val") String val,@Query("type") String type);

//    @GET("Bth_conn.php")
//
//    Call<Res_model> savebluetoothstatusback(@Query("vin") String vin,@Query("bt_status") String bt_status
//            ,@Query("address") String address,@Query("did") String did
//            ,@Query("blu_address") String blu_address,@Query("blue_name") String blue_name,@Query("type") String type);
//    @GET("Bth_conn.php")
//
//    Call<Res_model> savebluetoothstatus(@Query("vin") String vin,@Query("bt_status") String bt_status
//            ,@Query("address") String address,@Query("did") String did);
    @GET("change_trip_type.php")

    Call<Res_model> savetripstatus(@Query("type") String type,@Query("val") String val,@Query("trip_id") String trip_id,@Query("did") String did);

    @GET("certify_log.php")

    Call<Respp_model> savecertifystatus(@Query("driver") String driver, @Query("date") String date, @Query("status") String status
            , @Query("state") String state, @Query("cc") String cc);

//edit https://eld.e-logbook.info/api/logHandler_mobile.php
    @FormUrlEncoded
    @POST("logHandler_mobile.php")
    Call<JsonObject> uploadeditlog(@Field("ccode") String ccode, @Field("userid") String userid,
                                   @Field("driver") String driver, @Field("date") String date, @Field("stime") String stime
            , @Field("etime") String etime
            , @Field("tottime") String tottime
            , @Field("status") String status
            , @Field("remark") String remark);



//    @FormUrlEncoded
//    @POST("manage_break.php")
//    Call<JsonObject> breaktaken(@Field("ccode") String ccode, @Field("did") String did,
//                                @Field("date") String date, @Field("type") String type, @Field("time") String time
//            , @Field("time_taken") String time_taken
//            , @Field("status") String tottime
//            , @Field("lastid") String lastid);

//    @FormUrlEncoded
//    @POST("msg_read.php")
//    Call<JsonObject> megread(@Field("cc") String ccode, @Field("did") String did,
//                             @Field("msgid") String msgid);

    @FormUrlEncoded
    @POST("save_driver_loc.php")
    Call<JsonObject> savedriverlocation(@Field("driver") String driver, @Field("lat") String lat,
                                        @Field("lng") String lng,@Field("address") String address
            ,@Field("state") String state
            ,@Field("gpstatus") String gpstatus,@Field("cc") String cc,@Field("type") String type);





    //1
    @FormUrlEncoded
    @POST("eld_breakrelease.php")
    Call<JsonObject> newbreakrelease(@Field("cc") String ccode, @Field("did") String did,
                                     @Field("brk_tk_id") String brk_tk_id
            ,@Field("state") String state,@Field("address") String address
            ,@Field("testbreak") String testbreak ,@Field("break_id") String break_id
            ,@Field("reason") String reason);
    @FormUrlEncoded
    @POST("eld_breakrelease.php")
    Call<JsonObject> newbreakrelease(@Field("cc") String ccode, @Field("did") String did,
                                     @Field("brk_tk_id") String brk_tk_id
            ,@Field("state") String state,@Field("address") String address
            ,@Field("testbreak") String testbreak ,@Field("break_id") String break_id
    );
//    @GET("eld_breakinfo.php")
//
//    Call<List<newbrk_model>> getbreakrefresh(@Query("vin") String vin,@Query("did") String did,
//                                                          @Query("rule") String rule,@Query("ccode") String ccode);

    @GET("eld_breakinfo_t.php")

    Call<List<newbrk_model>> getbreakrefreshnew(@Query("vin") String vin, @Query("did") String did,
                                                @Query("ccode") String ccode);

    @FormUrlEncoded
    @POST("fmcsa_output_app_upload.php")
    Call<JsonObject>sendfmcsa(@Field("drvid") String drvid, @Field("date_from") String date_from,
                              @Field("date_to") String date_to, @Field("inv_code") String inv_code);



//    @GET("certify_log_last_seven.php")
//    Call<JsonObject> updatesevencertify(@Query("cc") String ccode, @Query("driver") String driver,
//                                        @Query("date") String date,@Query("status") String status);
//    @GET("certify_log_last_seven.php")
//    Call<JsonObject> updatesevencertifyaddr(@Query("cc") String ccode, @Query("driver") String driver,
//                                            @Query("date") String date,@Query("status") String status
//            ,@Query("state") String state
//            ,@Query("address") String address);


//    @GET("saveTripNo.php")
//
//    Call<JsonObject> updatetripno(@Query("vin") String vin,@Query("lid") String lid,@Query("did") String did,@Query("num") String num,@Query("trip") String trip
//            ,@Query("action") String action);

    @GET("saveTripNo_new.php")

    Call<JsonObject> updatetripno(@Query("vin") String vin,@Query("lid") String lid,@Query("did") String did,@Query("num") String num,@Query("trip") String trip
            ,@Query("action") String action,@Query("date") String date);

    @GET("saveCommodity.php")

    Call<JsonObject> savecommodity(@Query("vin") String vin,@Query("lid") String lid,@Query("did") String did,@Query("comm") String num);





    @GET("updateLoadDetails.php")

    Call<JsonObject> savelogdetails(@Query("vin") String vin,@Query("did") String did,@Query("date") String date,
                                    @Query("truck") String truck,@Query("trailer") String trailer
            ,@Query("comm") String comm,@Query("tripnum") String tripnum);


    @GET("getLoadDetails.php")

    Call<JsonObject> getlogdetails(@Query("vin") String vin,@Query("did") String did,@Query("date") String date);

    @GET("state_details.php")
    Call<List<state_model>> getstatedetails();


    @FormUrlEncoded
    @POST("demo_driver_registration.php")
    Call<JsonObject> savedemodrivers(@Field("f_name") String f_name, @Field("l_name") String l_name, @Field("e_mail") String e_mail
            , @Field("mob") String mob, @Field("d_license") String d_license, @Field("state") String state
            , @Field("city") String city);



    @FormUrlEncoded
    @POST("addCompany.php")
    Call<JsonObject> saveregcompany(@Field("compname") String compname, @Field("first_name") String first_name,
                                    @Field("last_name") String last_name,@Field("phone") String phone
            ,@Field("email") String email
            ,@Field("password") String password,@Field("state") String state
            ,@Field("dotno") String dotno

            ,@Field("paddress1") String paddress1
            ,@Field("paddress2") String paddress2
            ,@Field("pcity") String pcity
            ,@Field("pstate") String pstate
            ,@Field("pcountry") String pcountry
            ,@Field("pzip") String pzip
            ,@Field("pphone") String pphone
            ,@Field("baddress1") String baddress1
            ,@Field("baddress2") String baddress2
            ,@Field("bcity") String bcity
            ,@Field("bstate") String bstate
            ,@Field("bcountry") String bcountry
            ,@Field("bzip") String bzip
            ,@Field("bphone") String bphone

            ,@Field("saddress1") String saddress1
            ,@Field("saddress2") String saddress2
            ,@Field("scity") String scity
            ,@Field("sstate") String sstate
            ,@Field("scountry") String scountry
            ,@Field("szip") String szip
            ,@Field("sphone") String sphone);



    @FormUrlEncoded
    @POST("companyLogin.php")
    Call<JsonObject> getcompanylogin(@Field("email") String email, @Field("password") String password,
                                     @Field("address") String address
            , @Field("lat") String lat
            ,@Field("lon") String lon);



    @FormUrlEncoded
    @POST("addDriver.php")
    Call<JsonObject> saveregdriver(@Field("firstname") String firstname, @Field("lastname") String lastname
            ,@Field("email") String email
            ,@Field("phone") String phone,@Field("address") String address
            ,@Field("city") String city

            ,@Field("state") String state
            ,@Field("zip") String zip
            ,@Field("license") String license
            ,@Field("dob") String dob
            ,@Field("note") String note
            ,@Field("ccode") String ccode
            ,@Field("interval") String interval);


    @FormUrlEncoded
    @POST("editDriver.php")
    Call<JsonObject> editregdriver(@Field("driverID") String driverID,@Field("firstname") String firstname, @Field("lastname") String lastname
            ,@Field("email") String email
            ,@Field("phone") String phone,@Field("address") String address
            ,@Field("city") String city

            ,@Field("state") String state
            ,@Field("zip") String zip
            ,@Field("license") String license
            ,@Field("dob") String dob
            ,@Field("note") String note
            ,@Field("ccode") String ccode
            ,@Field("interval") String interval);

    @Multipart
    @POST("save_reportpdf.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file,
                                    @Part("file") RequestBody name);



    @GET("login_elog.php")

    Call<JsonObject> getlogindetail(@Query("driverid") String driverid,
                                    @Query("lat") String lat,
                                    @Query("long") String lng,
                                    @Query("uname") String uname,
                                    @Query("imenumber") String imenumber,
                                    @Query("imenumber1") String imenumber1,
                                    @Query("mac") String mac,
                                    @Query("ky") String ky,
                                    @Query("device_type") String device_type,
                                    @Query("device_version") String device_version,
                                    @Query("address") String address,
                                    @Query("state") String state,
                                    @Query("android_id") String android_id,@Query("unique_id") String unique_id);

//JSONArray
@GET("checkversion_new.php")

Call<List<version_model>> getcheckversion(@Query("vcode") String vcode,
                                    @Query("regnum") String regnum,
                                    @Query("did") String did);



    @GET("logout_elog.php")

    Call<JsonObject> getlogout(@Query("did") String did,
                                    @Query("driverid") String driverid,
                                    @Query("vin") String vin,
                                    @Query("lasttime") String lasttime,
                                    @Query("lat") String lat,
                                    @Query("lon") String lon,
                                    @Query("imenumber") String imenumber,
                                    @Query("imenumber1") String imenumber1,
                                    @Query("state") String state);


    @GET("monthly_report.php")

    Call<List<reportz_model>> getmonthreport(
            @Query("did") String did
            ,@Query("vin") String vin);

    @GET("weekly_report.php")

    Call<List<reportz_model>> getweeklyreport(
            @Query("did") String did
            ,@Query("vin") String vin,@Query("st_date") String st_date);



    @GET("upload_profileimage.php")

    Call<JsonObject> uploadprofileimg(@Query("did") String did,
                               @Query("photos") String photos, @Query("lat") String lat,
                                      @Query("lon") String lon);


    @GET("send_serviceticket.php")

    Call<JsonObject> sendserviceticketmail(@Query("did") String did,
                                      @Query("vin") String vin,
                                    @Query("license") String license,
                                      @Query("dname") String dname,
                                    @Query("cc") String cc,
                                    @Query("device") String device,
                                    @Query("cname") String cname,
                                    @Query("uname") String uname,
                                    @Query("cnumber") String cnumber,
                                    @Query("email") String email,
                                    @Query("message") String message,
                                    @Query("dtime") String dtime,
                                    @Query("photos") String photos);


    @GET("send_mail.php")

    Call<JsonObject> sendemail(@Query("did") String did,
                                           @Query("vin") String vin,
                                           @Query("to_email") String to_email);


    @GET("update_notstatus.php")

    Call<JsonObject> updatenotify(@Query("did") String did,
                               @Query("vin") String vin,
                               @Query("nid") String to_email);

    @POST("driver_checklist_save.php")
    @FormUrlEncoded
    Call<Response_model> savedriverchecklist(@FieldMap Map<String,String> params ,@Field("android_id") String android_id
            , @Field("device_token") String device_token
            , @Field("os") String os);


    @GET("view_driverchecklist_driver.php")

    Call<List<Dvir_view_model>> viewdriverchecklist(
            @Query("did") String did
            ,@Query("cc") String cc,@Query("id") String id
            ,@Query("typefld") String typefld);

    @GET("c")
    Call<List<ifta_model>> getifta(@Query("driver") String driver, @Query("from") String from
            , @Query("to") String to, @Query("cc") String cc, @Query("vin") String vin);


    @GET("send_ifta_report.php")

    Call<JsonObject> sendemailifta( @Query("from") String driver,@Query("to") String dusage,@Query("email") String vname,@Query("cc") String cc
            ,@Query("vin") String vin);



}
