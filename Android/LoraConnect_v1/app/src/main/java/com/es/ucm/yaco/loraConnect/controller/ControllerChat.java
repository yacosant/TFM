package com.es.ucm.yaco.loraConnect.controller;

import com.es.ucm.yaco.loraConnect.data.Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ControllerChat {
    private HashMap<String,Chat> chats;
    private Comparator comparator;

    public ControllerChat(){
        chats = new HashMap<String,Chat>();
        comparator = new Comparator<Chat>(){
            @Override
            public int compare(Chat o1, Chat o2) {
                return o1.getLastMsg().getTimestamp().compareTo(o2.getLastMsg().getTimestamp());
            }
        };
    }

    public ArrayList<Chat> getChats(){
        ArrayList<Chat> list= new ArrayList<Chat>(chats.values());
        Collections.sort(list,comparator);
        return list;
    }

}
