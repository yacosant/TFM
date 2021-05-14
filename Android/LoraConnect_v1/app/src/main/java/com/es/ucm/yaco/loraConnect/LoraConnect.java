package com.es.ucm.yaco.loraConnect;

import android.content.Context;

import com.es.ucm.yaco.loraConnect.controller.ControllerChat;
import com.es.ucm.yaco.loraConnect.controller.ControllerConfig;
import com.es.ucm.yaco.loraConnect.data.Chat;
import com.es.ucm.yaco.loraConnect.utils.TcpClient;

import java.util.ArrayList;

public class LoraConnect {
    private static ControllerConfig controllerConfig = null;
    private static ControllerChat controllerChat = null;
    private static TcpClient controllerTCP = null;

    public static void init(Context context){
        if(controllerConfig == null)
            controllerConfig = new ControllerConfig(context);
        if(controllerChat == null)
            controllerChat = new ControllerChat();
    }

    //Config
    public static String getUsername(){ return controllerConfig.getUsername();}
    public static String getWifiName(){ return controllerConfig.getWifi_name();}

    public static void setUsername(String user){ controllerConfig.setUsername(user);  }
    public static void setWifiName(String wifi){ controllerConfig.setWifi_name(wifi); }

    //Chats
    public static ArrayList<Chat> getListOfChats(){ return controllerChat.getChats();}

    //Mensajería
    //    enviar
//    recibir

}
