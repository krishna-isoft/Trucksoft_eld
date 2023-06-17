package com.trucksoft.isoft.isoft_elog.Model_class;

import java.io.Serializable;
import java.util.List;

public class newbrk_model implements Serializable {
    String breakduration;
    String rule;
    List<Break_info_model> break_info;
    public  List<Break_info_model> getbreakinfo()
    {
        return break_info;
    }

    public String getRule()
    {
        return rule;
    }
}
