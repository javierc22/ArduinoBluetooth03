package com.javierec.arduinobluetooth03;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter = null;
    int ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
