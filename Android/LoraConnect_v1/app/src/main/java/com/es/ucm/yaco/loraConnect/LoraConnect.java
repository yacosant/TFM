package com.es.ucm.yaco.loraConnect;

import android.content.Context;

import com.es.ucm.yaco.loraConnect.controller.ControllerChat;
import com.es.ucm.yaco.loraConnect.controller.ControllerConfig;
import com.es.ucm.yaco.loraConnect.data.Chat;
import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraConnect.utils.Constants;
import com.es.ucm.yaco.loraConnect.utils.TcpClient;

import java.util.ArrayList;

/**
 * Clase para gestionar LoraConnect desde la aplicación.
 * Github @yacosant
 */
public class LoraConnect {
    private static ControllerConfig controllerConfig = null;
    private static ControllerChat controllerChat = null;
    private static TcpClient controllerTCP = null;

    /**
     * Inicia LoraConnect library.
     * @param context contexto de la aplicación donde se isntancia
     */
    public static void init(Context context){
        if(controllerConfig == null)
            controllerConfig = new ControllerConfig(context);
        if(controllerChat == null)
            controllerChat = new ControllerChat();
    }

    public static void connectToESP32(OnMessageReceived listener){
        if(controllerTCP == null)
            controllerTCP = new TcpClient(listener);
    }

    //Config ---------------------------------------------------------------------------------------
    /**
     * Recupera el nombre del usuario
     * @return String nombre
     */
    public static String getUsername(){ return controllerConfig.getUsername();}

    /**
     * Setea el nombre del usuario para almacenarlo
     * @param user nombre a guardar
     */
    public static void setUsername(String user){
        controllerConfig.setUsername(user);
        Message m = new Message();
        m.configMessage(user);

        controllerTCP.send(m.toJson());
    }

    //Chats ----------------------------------------------------------------------------------------

    /**
     * Recupera la lista ordenada de chats
     * @return ArrayList<Chat> lista de chats
     */
    public static ArrayList<Chat> getListOfChats(){ return controllerChat.getChats();}

    //Mensajería -----------------------------------------------------------------------------------

    /**
     * Envia un mensaje al destinatario indicado.
     * Empaqueta, almacena y envia formateado el paquete
     * @param destination nombre del destino
     * @param msg mensaje a enviar
     */
    public static void sendMessage(String destination, String msg){
        Message m = new Message();
        m.setType(Constants.TYPE_MSG_MSG);
        m.setDestination(destination);
        m.setMsg(msg);

        controllerTCP.send(m.toJson());
        controllerChat.addMsg(m);
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(Message message);
    }
}
