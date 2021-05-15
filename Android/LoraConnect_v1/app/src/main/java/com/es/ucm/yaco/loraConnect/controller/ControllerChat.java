package com.es.ucm.yaco.loraConnect.controller;

import com.es.ucm.yaco.loraConnect.data.Chat;
import com.es.ucm.yaco.loraConnect.data.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Clase controladora que gestiona los chats y los mensajes
 */
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

    /**
     * Recupera la lista de chats ordenados por el timestamp de los ultimos mensajes de cada chat
     * @return  ArrayList<Chat> lista de chats
     */
    public ArrayList<Chat> getChats(){
        ArrayList<Chat> list= new ArrayList<Chat>(chats.values());
        Collections.sort(list,comparator);
        return list;
    }

    /**
    * Añade un mensaje a un chat. Si no existe el chat lo crea
     * @param  msg Objeto Menssage con la información necesaria
     */
    public void addMsg(Message msg){
        Chat c = chats.get(msg.getDestination());
        if(c == null) //Si no existe el chat
            c = new Chat(msg.getDestination());
        c.addMsg(msg);
        chats.put(msg.getDestination(),c);
    }

}
