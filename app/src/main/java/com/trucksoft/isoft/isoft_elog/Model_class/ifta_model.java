package com.trucksoft.isoft.isoft_elog.Model_class;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;

public class ifta_model implements Serializable {
    public String nickname;
    public String vehicle;
    public String company;
    public JsonObject summaries;
    public JsonArray statelist;
    public JsonObject mileslist;
}
