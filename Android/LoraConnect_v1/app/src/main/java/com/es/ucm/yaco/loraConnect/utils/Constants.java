package com.es.ucm.yaco.loraConnect.utils;

public final class Constants {
    public static final String PREFS_NAME = "LoraConnect_prefs";
    public static final String PREFS_KEY_WIFI = "wifi";
    public static final String PREFS_KEY_USERNAME = "username";

    public static String json_operation = "op";
    public static String json_source = "o";
    public static String json_destination = "d";
    public static String json_message = "msg";

    public static final short TYPE_MSG_MSG = 0;
    public static final short TYPE_MSG_CONFIG = 1;
    public static final short TYPE_MSG_ACK = 2;
    public static final short TYPE_MSG_HELLO = 3;
    public static final short TYPE_MSG_BYE = 4;
    public static final short TYPE_MSG_GET_CONNECTED = 5;

}
