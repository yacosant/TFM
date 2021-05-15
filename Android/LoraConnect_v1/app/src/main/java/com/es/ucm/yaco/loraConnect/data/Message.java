package com.es.ucm.yaco.loraConnect.data;

import com.es.ucm.yaco.loraConnect.controller.ControllerConfig;
import com.es.ucm.yaco.loraConnect.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Message {
    private short type;
    private String source;
    private String destination;
    private String msg;
    private boolean check;
    private Date timestamp;

    public Message(){
        check = false;
        this.timestamp = new Date();
        this.source = ControllerConfig.getUsername();
    }

    public void configMessage(String config) {
        Message m = new Message();
        m.setMsg(config);
        m.setType(Constants.TYPE_MSG_CONFIG);
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    private void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isCheck() {
        return check;
    }

    public void setChecked() {
        this.check = true;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void parse(String json){
        JSONObject obj;
        try {
            obj = new JSONObject(json);
            setType((short)obj.getInt(Constants.json_operation));
            setDestination(obj.getString(Constants.json_destination));
            setSource(obj.getString(Constants.json_source));
            setMsg(obj.getString(Constants.json_message));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toJson(){
        String jsonString="";
        try {
            jsonString = new JSONObject()
            .put(Constants.json_operation, type)
            .put(Constants.json_source, source)
            .put(Constants.json_destination, destination)
            .put(Constants.json_message, msg)
            .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
