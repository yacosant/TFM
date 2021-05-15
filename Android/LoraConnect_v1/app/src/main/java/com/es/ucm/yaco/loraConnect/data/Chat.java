package com.es.ucm.yaco.loraConnect.data;

import java.util.ArrayList;
import java.util.Date;

public class Chat {
    private String destination;
    private ArrayList<Message> msgs;

    public Chat(String dest){
        destination = dest;
        msgs = new ArrayList<Message>();
    }

    public String getDestination() {
        return destination;
    }

    public ArrayList<Message> getMsgs() {
        return msgs;
    }

    public Message getLastMsg(){
       return msgs.get(msgs.size()-1);
    }

    public void addMsg(Message msg){
        msg.setDestination(destination);
        msgs.add(msg);
    }
}
