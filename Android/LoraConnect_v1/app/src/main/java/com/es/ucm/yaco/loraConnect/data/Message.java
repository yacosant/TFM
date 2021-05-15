package com.es.ucm.yaco.loraConnect.data;

import com.es.ucm.yaco.loraConnect.controller.ControllerConfig;
import com.es.ucm.yaco.loraConnect.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
/**
 * Tipo de datos que empaqueta toda la información enviada y recibida por la ESP32
 */
public class Message {
    private short type;
    private String source;
    private String destination;
    private String msg;
    private Date timestamp;

    /**
     * Crear mensaje
     */
    public Message(){
        this.timestamp = new Date();
        this.source = ControllerConfig.getUsername();
    }

    /**
     * Se crea un mensaje de tipo configuración apra notificar a la ESP32
     * @param config configuracion (texto en compo msg) que se va a enviar
     */
    public void configMessage(String config) {
        Message m = new Message();
        m.setMsg(config);
        m.setType(Constants.TYPE_MSG_CONFIG);
    }

    /**
     * Indica el tipo del mensaje. Puede ser:
     * TYPE_MSG_MSG = 0;
     * TYPE_MSG_CONFIG = 1;
     * TYPE_MSG_HELLO_ACK = 2;
     * TYPE_MSG_HELLO = 3;
     * TYPE_MSG_BYE = 4;
     * @return  tipo del mensaje
     */
    public short getType() {
        return type;
    }

    /**
     * Setea el tipo de mensaje
     * @param type Indica el tipo del mensaje a setear.
     *  Puede ser Constant.*:
     * TYPE_MSG_MSG = 0;
     * TYPE_MSG_CONFIG = 1;
     * TYPE_MSG_HELLO_ACK = 2;
     * TYPE_MSG_HELLO = 3;
     * TYPE_MSG_BYE = 4;
     */
    public void setType(short type) {
        this.type = type;
    }

    /**
     * Recupera el origen del mensaje
     * @return nombre de usuario originario del mensaje
     */
    public String getSource() {
        return source;
    }

    /**
     * Setea el origen del mensaje
     * @param source nombre de usuario originario del mensaje
     */
    private void setSource(String source) {
        this.source = source;
    }

    /**
     * Recupera el destinatario del mensaje
     * @return nombre de usuario destinatario del mensaje
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Setea el destinatario del mensaje
     * @param destination  nombre de usuario destinatario del mensaje
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Recupera el contenido del mensaje
     * @return contenido del mensaje
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Setea el contenido del mensaje
     * @param msg  contenido del mensaje
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Recupera la hora de creación del mensaje
     * @return timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Parsea el String json recibido por TCP para disponer de la información en el objeto Message
     * @param json de entrada
     */
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

    /**
     * Convierte a String json el mensaje para poder enviarlo por TCP
     * @return String del json
     */
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
