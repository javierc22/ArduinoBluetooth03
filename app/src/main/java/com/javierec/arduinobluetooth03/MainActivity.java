package com.javierec.arduinobluetooth03;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private Button buttonConectar, buttonLed1, buttonLed2, buttonLed3;

    // Bluetooth
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothDevice mDevice = null;
    BluetoothSocket mSocket = null;

    private static final int ENABLE_BT = 1;
    private static final int ENABLE_CONECTION = 2;
    private static final int MESSAGE_READ = 3;

    boolean myConection = false;
    private static String MAC = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ConnectedThread connectedThread;
    Handler mHandler;
    StringBuilder dataBluetooth = new StringBuilder();


    @SuppressLint("HandlerLeak")
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
                if (myConection) {
                    // Desconectar
                    try {
                        mSocket.close();
                        myConection = false;
                        buttonConectar.setText("Conectar");
                        Toast.makeText(getApplicationContext(), "Bluetooth fue desconectado", Toast.LENGTH_SHORT).show();
                    } catch (IOException error) {
                        Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Conectar
                    Intent intent = new Intent(MainActivity.this, ListDevices.class);
                    startActivityForResult(intent, ENABLE_CONECTION);
                }
            }
        });

        buttonLed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myConection) {
                    connectedThread.write("led1");
                } else {
                    Toast.makeText(MainActivity.this, "Sin conexión Bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myConection) {
                    connectedThread.write("led2");
                } else {
                    Toast.makeText(MainActivity.this, "Sin conexión Bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myConection) {
                    connectedThread.write("led3");
                } else {
                    Toast.makeText(MainActivity.this, "Sin conexión Bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String dataReceived = (String) msg.obj;
                    dataBluetooth.append(dataReceived);
                    int endInformation = dataBluetooth.indexOf("}");

                    if (endInformation > 0) {
                        String dataComplete = dataBluetooth.substring(0, endInformation);
                        int informationSize = dataComplete.length();

                        if (dataBluetooth.charAt(0) == '{') {
                            String dataFinal = dataBluetooth.substring(1, informationSize);
                            Log.d("Recibidos", dataFinal);

                            // Set Text Button Led 1
                            if (dataFinal.contains("l1on")) {
                                buttonLed1.setText("LED1 ON");
                                Log.d("LED1","ON");
                            } else if (dataFinal.contains("l1off")) {
                                buttonLed1.setText("LED1 OFF");
                                Log.d("LED1","OFF");
                            }

                            // Set Text Button Led 2
                            if (dataFinal.contains("l2on")) {
                                buttonLed2.setText("LED2 ON");
                                Log.d("LED2","ON");
                            } else if (dataFinal.contains("l2off")) {
                                buttonLed2.setText("LED2 OFF");
                                Log.d("LED2","OFF");
                            }

                            // Set Text Button Led 3
                            if (dataFinal.contains("l3on")) {
                                buttonLed3.setText("LED3 ON");
                                Log.d("LED3","ON");
                            } else if (dataFinal.contains("l3off")) {
                                buttonLed3.setText("LED3 OFF");
                                Log.d("LED3","OFF");
                            }

                        }

                        dataBluetooth.delete(0, dataBluetooth.length());

                    }
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ENABLE_BT: // Comprueba si Bluetooth stá activado
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

            case ENABLE_CONECTION: //
                if (resultCode == Activity.RESULT_OK) {

                    MAC = data.getStringExtra(ListDevices.ADDRESS_MAC);
                    // Toast.makeText(this, "MAC: " + MAC, Toast.LENGTH_SHORT).show();

                    // Obtenemos el dispositivo conectado junto con su dirección MAC y lo guardamos
                    // para ejecutar la conexión Bluetooth
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mDevice = mBluetoothAdapter.getRemoteDevice(MAC);

                    try {
                        mSocket = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
                        mSocket.connect();
                        myConection = true;
                        connectedThread = new ConnectedThread(mSocket);
                        connectedThread.start();
                        buttonConectar.setText("Desconectar");
                        Toast.makeText(this, "Estás conectado con: " + MAC, Toast.LENGTH_SHORT).show();

                    } catch (IOException error) {
                        myConection = false;
                        Toast.makeText(this, "Ocurrió un error: " + error, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this,
                            "Error al obtener dirección MAC",
                            Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String dataBt = new String(buffer, 0, bytes);

                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, dataBt).sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String enviarDatos) {
            byte[] mBuffer = enviarDatos.getBytes();

            try {
                mmOutStream.write(mBuffer);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) { }
        }
    }
}
