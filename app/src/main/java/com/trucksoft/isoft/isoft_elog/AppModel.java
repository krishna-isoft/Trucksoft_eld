package com.trucksoft.isoft.isoft_elog;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.geometris.wqlib.BaseRequest;

/**
 * Created by bipin_2 on 2/7/2018.
 */

public class AppModel {
    public static final String TAG = "Geometris";
    private static AppModel instance = new AppModel();

    public static AppModel getInstance() {
        return instance;
    }

    public BaseRequest mLastEvent= null;

    public static BluetoothDevice mDevice = null;
    public BluetoothAdapter mBtAdapter = null;

    //public DeviceInfo mDeviceInfo = null;

    public long mConnectTime = 0;

    private AppModel() {

    }
}
