package com.es.ucm.yaco.loraconnect_example.data;

import com.es.ucm.yaco.loraConnect.data.Message;

import java.util.ArrayList;
import java.util.Date;

public class Chat {
    private String destination;
    private ArrayList<Message> msgs;
    private boolean online;
    private boolean newMsg;

    public Chat(String dest){
        destination = dest;
        msgs = new ArrayList<Message>();
        online = false;
    }

    public String getDestination() {
        return destination;
    }

    public ArrayList<Message> getMsgs() {
        return msgs;
    }

    public Message getLastMsg(){
       if(msgs.size()-1>=0)
            return msgs.get(msgs.size()-1);
       else
           return new Message();
    }

    public void addMsg(Message msg){
        msgs.add(msg);
        online = true;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isNewMsg() {
        return newMsg;
    }

    public void setNewMsg(boolean newMsg) {
        this.newMsg = newMsg;
    }
}
