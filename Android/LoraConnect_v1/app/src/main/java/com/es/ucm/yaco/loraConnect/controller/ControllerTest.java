package com.es.ucm.yaco.loraConnect.controller;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraConnect.utils.Constants;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

public class ControllerTest {
    private ControllerTcp controllerTcp;
    private static ArrayList<Long> latencias = new ArrayList<Long>();

    private int time=0, frec=0, numEjec=0;
    private static int recTime=0, recFrec=0, recNumEjec=0;

    public ControllerTest(ControllerTcp controllerTcp){
        this.controllerTcp = controllerTcp;
    }

    public Message makeTest(String destination, String params) {


        String prms[] = params.split(";");
        time = Integer.parseInt(prms[0]);
        frec = Integer.parseInt(prms[1]);
        numEjec = time/frec;
        String contenido_msg_org = Constants.MSG_CONTENT_TEST
                .replace("{t}",String.valueOf(time))
                .replace("{f}",String.valueOf(frec))
                .replace("{n}",String.valueOf(numEjec));

        Message msg;

        for(int i=0; i<numEjec; i++){
            msg = new Message();
            msg.setDestination(destination);
            msg.setType(Constants.TYPE_MSG_TEST);
            msg.setMsg(contenido_msg_org.replace("{i}", String.valueOf(i)));
            controllerTcp.send(msg.toJson());
            SystemClock.sleep(frec*1000);
        }
        //Se notifica el fin del test
        msg = new Message();
        msg.setDestination(destination);
        msg.setType(Constants.TYPE_MSG_TEST);
        msg.setMsg(Constants.MSG_CONTENT_TEST_FIN);

        controllerTcp.send(msg.toJson());
        msg.setMsg(contenido_msg_org.replace("{i}", String.valueOf(numEjec)));
        time=frec=numEjec=0;

        return msg;
    }

    public static void reciveMsgTest(Message msg, long time){
        long dif = System.currentTimeMillis();
        time = dif-time;
        Log.i("TIME_PROCE", "es: " + time);
        dif-= msg.getTimestamp();
        latencias.add( new Long(dif) );

        if(recTime==0){
            String prms[] = msg.getMsg().split(";");
            recTime = Integer.parseInt(prms[1]);
            recFrec = Integer.parseInt(prms[2]);
            recNumEjec = Integer.parseInt(prms[3]);
        }
        else if(Constants.MSG_CONTENT_TEST_FIN.equals(msg.getMsg()))
            writeToFile();

    }

    private static void writeToFile() {
        int i=0;
        long cont = 0;
        String date = new Date().toString();
        StringBuilder sb = new StringBuilder();

        sb.append("Test Latencias LoraConnect - ").append(date).append(" - ").append(" Duración:").append(recTime)
                .append(" - Frecuencia: ").append(recFrec).append(" - Ejecuciones: ").append(recNumEjec).append("\n\n");

        for(Long l : latencias){
            sb.append(l.toString()).append("\n");
            cont+=l;
            i++;
        }

        sb.append("\n").append("MEDIA: ").append(cont/i).append("\n");
        Log.i("Ubicacion_Log", "File write to: " + ControllerConfig.getContext().getFilesDir());
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ControllerConfig.getContext().openFileOutput("Test-"+date+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(sb.toString());
            outputStreamWriter.close();

            latencias.clear();
            recTime = recFrec = recNumEjec = 0;

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
