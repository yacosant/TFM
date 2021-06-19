package com.es.ucm.yaco.loraconnect_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.google.android.material.snackbar.Snackbar;

public class ConfigActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_config);
        Switch switchTest = (Switch)findViewById(R.id.switch_modeTest);
        TextView username = (TextView) findViewById(R.id.editTextconfigUsername);
        username.setText(LoraConnect.getUsername());
        //switchGps.setChecked(LoraConnect.isSendGps());
        Button button_ConfigGuardar = (Button) findViewById(R.id.button_ConfigGuardar);

        switchTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchTest.setChecked(isChecked);
            }
        });


        button_ConfigGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Snackbar.make(view, "Cambios guardados", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                LoraConnect.setUsername(username.getText().toString());
                //LoraConnect.setSendGps(switchGps.isChecked());
            }
        });

    }
}