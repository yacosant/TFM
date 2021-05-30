package com.es.ucm.yaco.loraConnect.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.es.ucm.yaco.loraConnect.utils.Constants;

public class ControllerConfig {
    private static Context context;
    private static String username = "";
    private static boolean sendGps = false;

    /*
    For an Activity:    new ControllerConfig(this);
    For a Fragment:     new ControllerConfig(getActivity());
    For a Service:      new ControllerConfig(getApplicationContext());
    */
    public ControllerConfig(Context context){
        this.context = context;
        getconfig();
    }
    private void getconfig(){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        username = prefs.getString(Constants.PREFS_KEY_USERNAME, "");
        sendGps = prefs.getBoolean(Constants.PREFS_KEY_GPS, false);
        // 1...N configs aquí
    }


    public static String getUsername() {
        return username;
    }

    public static boolean isSendGps() {return sendGps;}

    public void setUsername(String username) {
        this.username = username;
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFS_KEY_USERNAME, username);
        editor.apply();
    }

    public void setSendGps(boolean value) {
        this.username = username;
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.PREFS_KEY_GPS, value);
        editor.apply();
    }

}
