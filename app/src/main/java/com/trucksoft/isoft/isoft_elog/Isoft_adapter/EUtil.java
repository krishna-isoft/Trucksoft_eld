package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

import java.util.HashMap;
import java.util.Map;

public class EUtil {
    private static final String TAG = "BenchMarkUtil";
    private static boolean enable;
    private static Map<Object, Long> startTimes;

    static {
        startTimes = new HashMap();
        enable = false;
    }

    public static void enable(boolean enable) {
       // DebugLog.m920v(TAG, "BenchMarkUtil enable: " + enable);
        enable = enable;
    }

    public static void start(Object id) {
        if (enable) {
          //  DebugLog.m920v(TAG, "start id: " + id);
            startTimes.put(id, Long.valueOf(System.currentTimeMillis()));
        }
    }

    public static int stop(Object id) {
        if (!enable) {
            return 0;
        }
        int elapsed = (int) (System.currentTimeMillis() - ((Long) startTimes.get(id)).longValue());
      //  DebugLog.m920v(TAG, "end id: " + id + " elapsed: " + elapsed);
        return elapsed;
    }
}
