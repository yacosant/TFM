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

public class TcpClient {

    private PrintWriter mBufferOut;
    private BufferedReader mBufferIn;
    private LoraConnect.OnMessageReceived mMessageListener = null;
    private boolean connected = false;
    private String msg_string;
    private Message msg;

    public TcpClient(LoraConnect.OnMessageReceived listener) {
        mMessageListener = listener;
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

                    if (msg_string != null && mMessageListener != null) {
                        msg = new Message();
                        msg.parse(msg_string);
                        //Se notifica a la Activity que se ha recibido un mensaje
                        mMessageListener.messageReceived(msg);
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

}
