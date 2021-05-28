package com.es.ucm.yaco.loraconnect_example;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.es.ucm.yaco.loraconnect_example.controller.ControllerChat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {
    public static MainActivity mA;
    private static ControllerChat controllerChat = new ControllerChat();
    private static boolean back = false;
    private static Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LoraConnect.init(this);
       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        }
    }

    protected void refreshChatList(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.chatList);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(currentFragment);
        ft.attach(currentFragment);
        ft.commit();
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

    protected static void changeActiveItem(int pos, boolean value){
        if(menu!=null)
            menu.getItem(pos).setEnabled(value);
    }
}