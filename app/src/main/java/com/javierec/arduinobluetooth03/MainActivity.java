package com.javierec.arduinobluetooth03;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private Button buttonConectar, buttonLed1, buttonLed2, buttonLed3;

    // Bluetooth
    BluetoothAdapter mBluetoothAdapter = null;
    private static final int ENABLE_BT = 1;
    private static final int ENABLE_CONECTION = 2;
    boolean conection = false;
    private static String MAC = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind UI Elements
        buttonConectar = findViewById(R.id.button_conectar);
        buttonLed1 = findViewById(R.id.button_led_1);
        buttonLed2 = findViewById(R.id.button_led_2);
        buttonLed3 = findViewById(R.id.button_led_3);

        // Set Bluetooth Adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Comprueba si dispositivo es compatible con Bluetooth
        if (mBluetoothAdapter == null) {
            //Dispositivo no posee Bluetooth
            Toast.makeText(this,
                    "Su dispositivo no posee Bluetooth",
                    Toast.LENGTH_SHORT).show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            // Habilita BlueTooth
            // Esto generará una solicitud para habilitar Bluetooth a través de los ajustes de sistema (sin detener tu aplicación)
            // en caso que no esté activado
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT);
        }

        // Events
        buttonConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conection) {
                    // Desconectar
                } else {
                    // Conectar
                    Intent intent = new Intent(MainActivity.this, ListDevices.class);
                    startActivityForResult(intent, ENABLE_CONECTION);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this,
                            "Bluetooth fue activado",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "Bluetooth NO fue activado, la aplicación se cerrará",
                            Toast.LENGTH_SHORT).show();

                    finish();
                }

                break;

            case ENABLE_CONECTION:
                if (resultCode == Activity.RESULT_OK) {
                    MAC = data.getExtras().getString(ListDevices.ADDRESS_MAC);

                    Toast.makeText(this,
                            "MAC: " + MAC,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "Error al obtener dirección MAC",
                            Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
