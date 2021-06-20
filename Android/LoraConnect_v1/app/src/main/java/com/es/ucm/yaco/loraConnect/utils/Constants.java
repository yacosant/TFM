package com.es.ucm.yaco.loraConnect.utils;

public final class Constants {
    public static final String PREFS_NAME = "LoraConnect_prefs";
    public static final String PREFS_KEY_USERNAME = "username";

    public static String json_operation = "op";
    public static String json_source = "o";
    public static String json_destination = "d";
    public static String json_timestamp = "t";
    public static String json_message = "msg";

    public static final short TYPE_MSG_MSG = 0;
    public static final short TYPE_MSG_CONFIG = 1;
    public static final short TYPE_MSG_TEST = 1;
    public static final short TYPE_MSG_HELLO_ACK = 2;
    public static final short TYPE_MSG_HELLO = 3;
    public static final short TYPE_MSG_BYE = 4;

    public static final String TCP_SERVER_IP = "192.168.4.1";
    public static final int TCP_SERVER_PORT = 80;

    public static final String MSG_CONTENT_TEST = "Msg de prueba;{t};{f};{n};{i}";
    public static final String MSG_CONTENT_TEST_FIN = "FIN";

}
