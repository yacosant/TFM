package com.es.ucm.yaco.loraConnect.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.es.ucm.yaco.loraConnect.utils.Constants;

public class ControllerConfig {
    private static Context context;
    private static String username = "";
    private boolean test = false;

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
        // 1...N configs aquí
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public static String  getUsername() {
        return username;
    }

    public static Context getContext() {
        return context;
    }

    public void setUsername(String username) {
        ControllerConfig.username = username;
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFS_KEY_USERNAME, username);
        editor.apply();
    }

}
