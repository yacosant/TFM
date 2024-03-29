package com.es.ucm.yaco.loraconnect_example.controller;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraconnect_example.ChatListAdapter;
import com.es.ucm.yaco.loraconnect_example.MainActivity;
import com.es.ucm.yaco.loraconnect_example.data.Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Clase controladora que gestiona los chats y los mensajes
 */
public class ControllerChat {
    private HashMap<String, Chat> chats;
    private Comparator comparator;

    public ControllerChat(){
        chats = new HashMap<String, Chat>();
        comparator = new Comparator<Chat>(){
            @Override
            public int compare(Chat o1, Chat o2) {
                return o2.getLastMsg().getTimestamp() < o1.getLastMsg().getTimestamp() ? 1 : 0;
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
        String destino= msg.getDestination();
        if(LoraConnect.getUsername().equals(msg.getDestination()))
            destino = msg.getSource();

        Chat c = chats.get(destino);
        if(c == null) //Si no existe el chat
            c = new Chat(destino);
        c.addMsg(msg);
        if(LoraConnect.getUsername().equals(msg.getDestination())) {
            c.setNewMsg(true);
            MainActivity.pushNotification(msg);//Crea notificación push
        }
        chats.put(destino,c);
    }

    /**
     * Añade un chat nuevo si no existe el chat y si ya existe, lo recupera
     * @param  dest String con el nombre del destinatario
     */
    public void addChat(String dest){
        Chat c = chats.get(dest);
        if(c == null) { //Si no existe el chat
            c = new Chat(dest);
            chats.put(dest, c);
        }
        c.setOnline(true);
    }

    /**
     * Recupera el chat indicado por el destino
     * @param dest Nombre del destinatario
     * @return  Chat chat
     */
    public Chat getChat(String dest) {
        return chats.get(dest);
    }

}
