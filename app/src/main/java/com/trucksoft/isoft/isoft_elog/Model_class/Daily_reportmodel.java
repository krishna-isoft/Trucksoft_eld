package com.trucksoft.isoft.isoft_elog.Model_class;
import java.io.Serializable;

/**
 * Created by isoft on 26/1/18.
 */

public class Daily_reportmodel implements Serializable {
    String type;
    String address;
    String message;
    String ftime;
    String ttime;
    String dtime;
    String status;
    String remark;
    String lastindex;
    int tcount;
    String vin;
    String vid;
    String lid;
    String edited_status;
    String connection;
    String trip_id;
    String tripnum;
    String comm;
    public Daily_reportmodel (String type)
    {
        this.type=type;
    }
}
