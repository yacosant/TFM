package com.es.ucm.yaco.loraConnect;

import android.content.Context;

import com.es.ucm.yaco.loraConnect.controller.ControllerConfig;
import com.es.ucm.yaco.loraConnect.controller.ControllerGps;
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
    private static TcpClient controllerTCP = null;
    private static ControllerGps controllerGps = null;

    /**
     * Inicia LoraConnect library.
     * @param context contexto de la aplicación donde se isntancia
     */
    public static void init(Context context){
        if(controllerConfig == null)
            controllerConfig = new ControllerConfig(context);
        if(controllerGps == null)
            controllerGps = new ControllerGps(context);

    }
    /**
     * Proceso que se inicia para realizar y gestionar la conexión con la ESP32
     * @param listener del tipo OnMessageReceived que hay que definir en el Activity para que se le notifiquen las llegadas de mensajes.
     */
    public static void connectToESP32(OnMessageReceived listener){
        if(controllerTCP == null)
            controllerTCP = new TcpClient(listener);
        controllerTCP.run();
    }

    //Config ---------------------------------------------------------------------------------------
    /**
     * Recupera el nombre del usuario
     * @return String nombre
     */
    public static String getUsername(){ return controllerConfig.getUsername();}

    /**
     * Recupera el nombre del usuario
     * @return String nombre
     */
    public static boolean isSendGps(){ return controllerConfig.isSendGps();}

    /**
     * Setea el nombre del usuario para almacenarlo
     * @param user nombre a guardar
     */
    public static void setUsername(String user){
        controllerConfig.setUsername(user);
        /*Message m = new Message();
        m.configMessage(user);

        controllerTCP.send(m.toJson());*/
    }

    /**
     * Setea la configuración gps
     * @param value valor a guardar
     */
    public static void setSendGps(boolean value){
        controllerConfig.setSendGps(value);
    }

    //Mensajería -----------------------------------------------------------------------------------

    /**
     * Envia un mensaje al destinatario indicado.
     * Empaqueta, almacena y envia formateado el paquete
     * @param destination nombre del destino
     * @param msg mensaje a enviar
     */
    public static Message sendMessage(String destination, String msg){
        Message m = new Message();
        m.setType(Constants.TYPE_MSG_MSG);
        m.setDestination(destination);
        m.setMsg(msg);
        m.setSource(getUsername());
        if(controllerConfig.isSendGps())
            m.setCoordenadas();
        controllerTCP.send(m.toJson());
        return m;
    }

    /**
     * Recupera lista de usuarios conectados
     * @return ArrayList<String> lista de usuarios
     */
    public static ArrayList<String> getConnected(){
        return controllerTCP.getUsersConnected();
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(Message message);
    }

    /**
     * Desconecta de la ESP32
     */
    public static void disconnect(){
        if(controllerTCP != null)
            controllerTCP.disconnect();
    }

}
