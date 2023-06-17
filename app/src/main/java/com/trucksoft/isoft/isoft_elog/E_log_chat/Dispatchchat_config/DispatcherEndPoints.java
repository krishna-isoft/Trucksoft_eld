package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_config;

/**
 * Created by Lincoln on 06/01/16.
 */
public class DispatcherEndPoints {

    // localhost url
    // public static final String BASE_URL = "http://192.168.0.101/gcm_chat/v1";
    //public static final String BASE_URL = "http://trucksoft.net/wweapp/chat_new/v1";
   public static final String BASE_URL = "http://eld.e-logbook.info/elog_app/chat/v1";
   // public static final String LOGIN = BASE_URL + "/user/login";
    public static final String LOGIN = BASE_URL + "/user/login_new";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";
    public static final String CHAT_ROOM_MESSAGE_UPDATE = BASE_URL + "/chat_roomsok/_ID_";

   // public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/driversend_message";
}
