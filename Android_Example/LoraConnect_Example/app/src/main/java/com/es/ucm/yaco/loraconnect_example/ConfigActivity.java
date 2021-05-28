package com.es.ucm.yaco.loraconnect_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.es.ucm.yaco.loraConnect.LoraConnect;
import com.google.android.material.snackbar.Snackbar;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_config);

        TextView username = (TextView) findViewById(R.id.editTextconfigUsername);
        username.setText(LoraConnect.getUsername());

        Button button_ConfigGuardar = (Button) findViewById(R.id.button_ConfigGuardar);

        button_ConfigGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Snackbar.make(view, "Username cambiado a: '"+username.getText().toString()+"'", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                LoraConnect.setUsername(username.getText().toString());
            }
        });

    }
}