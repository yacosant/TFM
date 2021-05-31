package com.es.ucm.yaco.loraConnect.utils;

import android.util.Log;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.es.ucm.yaco.loraConnect.data.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class TcpClient {

    private PrintWriter mBufferOut;
    private BufferedReader mBufferIn;
    private LoraConnect.OnMessageReceived mMessageListener = null;
    private boolean connected = false;
    private String msg_string;
    private Message msg;
    private ArrayList<String> usersConnected;

    public TcpClient(LoraConnect.OnMessageReceived listener) {
        mMessageListener = listener;
        usersConnected = new ArrayList<String>();
    }

    /**
     * Envía el mensaje (String) en formato json por TCP a la ESP32
     */
    public void send(String msg) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(msg);
            mBufferOut.flush();
        }
    }

    /**
     * Cierra la conección TCP con la ESP32
     */
    public void disconnect() {

       Message m = new Message();
       m.setType(Constants.TYPE_MSG_BYE);
       send(m.toJson());

        connected = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
    }

    /**
     * Proceso que se inicia para realizar y gestionar la conexión con la ESP32
     */
    public void run() {

        connected = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(Constants.TCP_SERVER_IP);


            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, Constants.TCP_SERVER_PORT);

            try {

                //Mensajes de salida
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //Mensajes de entrada
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Envia mensaje de aparición
                msg = new Message();
                msg.setType(Constants.TYPE_MSG_HELLO);
                send(msg.toJson());

                //in this while the client listens for the messages sent by the server
                while (connected) {
                    msg_string = mBufferIn.readLine();
                    try{
                        if (msg_string != null && mMessageListener != null) {
                            Log.d("TCP", "MSG recibido: " + msg_string);
                            msg = new Message();
                            msg.parse(msg_string);

                            if (msg.getType() == Constants.TYPE_MSG_HELLO || msg.getType() == Constants.TYPE_MSG_HELLO_ACK) {
                                if (!usersConnected.contains(msg.getSource())) {
                                    usersConnected.add(msg.getSource());
                                    Log.d("TCP", "Usuario añadido " + msg.getSource());
                                }
                                Log.d("TCP", "Lista: " + usersConnected.toString());
                            }

                            if (msg.getType() == Constants.TYPE_MSG_HELLO) { //Se responde al usuario que estamos en linea
                                Message msgAck = new Message();
                                msgAck.setType(Constants.TYPE_MSG_HELLO_ACK);
                                msgAck.setDestination(msg.getSource());
                                send(msgAck.toJson());
                            } else if (msg.getType() == Constants.TYPE_MSG_BYE) {
                                if (usersConnected.remove(msg.getSource()))
                                    Log.d("TCP", "Usuario borrado " + msg.getSource());
                                else
                                    Log.d("TCP", "Usuario " + msg.getSource() + " NO borrado");
                                Log.d("TCP", "Lista: " + usersConnected.toString());
                            }

                            //if(msg.getType()== Constants.TYPE_MSG_MSG)

                            //Se notifica a la Activity que se ha recibido un mensaje
                            mMessageListener.messageReceived(msg);
                        }
                    }catch (Exception e){
                        Log.d("TCP", "MSG basura descartado: " + msg_string);
                    }
                }

            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }

    }

    /**
     * Recupera lista de usuarios conectados
     * @return ArrayList<String> lista de usuarios
     */
    public ArrayList<String> getUsersConnected(){
        return usersConnected;
    }

}
