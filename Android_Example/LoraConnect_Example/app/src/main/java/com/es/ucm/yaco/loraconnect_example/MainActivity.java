package com.es.ucm.yaco.loraconnect_example;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.es.ucm.yaco.loraConnect.data.Message;
import com.es.ucm.yaco.loraconnect_example.controller.ControllerChat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;


import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {
    public static MainActivity mA;
    private static ControllerChat controllerChat = new ControllerChat();
    private static boolean back = false;
    private static boolean back2 = false;
    private static Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LoraConnect.init(this);

        mA = this;
    }
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, ConfigActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_desconectar) {
            Intent i = new Intent(this, MainActivity.class);
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Desconectar ESP32")
                    .setMessage("¿Seguro que quieres desconectarte?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoraConnect.disconnect();
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed (){
        if (back) {
            super.onBackPressed();
            if(back2){
                back2=false;
                super.onBackPressed();
            }
        }
    }

    protected static MainActivity getMainAct(){
        return mA;
    }

    protected static ControllerChat getChatController(){
        return controllerChat;
    }

    protected static void setBack(boolean back) {
        MainActivity.back = back;
    }

    protected static void setBack2(boolean back2) {
        MainActivity.back2 = back2;
    }

    protected static void changeActiveItem(int pos, boolean value){
        if(menu!=null)
            menu.getItem(pos).setEnabled(value);
    }

    public static void pushNotification(Message msg){

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mA.getApplicationContext(), "notify_001");

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("");
        bigText.setBigContentTitle("Mensaje de "+msg.getSource());
        bigText.setSummaryText("Mensaje por Lora");

        mBuilder.setSmallIcon(R.drawable.back_msg_left);
        mBuilder.setContentTitle("Mensaje de "+msg.getSource());
        mBuilder.setContentText(msg.getMsg());
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) mA.getSystemService(Context.NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }*/

        mNotificationManager.notify(0, mBuilder.build());
    }
}