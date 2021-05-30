package com.es.ucm.yaco.loraconnect_example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
        Switch switchGps = (Switch)findViewById(R.id.switch_gpsConfig);
        TextView username = (TextView) findViewById(R.id.editTextconfigUsername);
        username.setText(LoraConnect.getUsername());
        switchGps.setChecked(LoraConnect.isSendGps());
        Button button_ConfigGuardar = (Button) findViewById(R.id.button_ConfigGuardar);

        switchGps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ActivityCompat.requestPermissions(MainActivity.getMainAct(),
                            new String []{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            100);
                    // The toggle is enabled
                    switchGps.setChecked(true);
                    if(ActivityCompat.checkSelfPermission(MainActivity.getMainAct(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.getMainAct(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        Log.d("Config", "Permisos GPS OK");
                    }
                    else{
                        //switchGps.setChecked(false);
                        Log.d("Config", "Permisos GPS KO");
                    }
                }
            }
        });

        /*@Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode){
                case REQUEST_CODE_ASK_PERMISSIONS:
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        // Permission Granted
                        // Rutina que se ejecuta al aceptar
                    }else{
                        // Permission Denied
                        Toast.makeText(PrincipalActivity.this, "No se aceptó permisos", Toast.LENGTH_SHORT).show();
                    }
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }*/

        button_ConfigGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Snackbar.make(view, "Cambios guardados", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                LoraConnect.setUsername(username.getText().toString());
                LoraConnect.setSendGps(switchGps.isChecked());
            }
        });

    }
}