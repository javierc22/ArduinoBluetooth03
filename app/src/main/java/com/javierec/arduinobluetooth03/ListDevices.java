package com.javierec.arduinobluetooth03;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.Set;

public class ListDevices extends ListActivity{

    private BluetoothAdapter mBluetoothAdapter2 = null;
    static String ADDRESS_MAC = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Array que guardara los dispositivos pareados
        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        // Set Bluetooth adapter
        mBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter2.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for(BluetoothDevice device :pairedDevices) {
                String nameBT = device.getName();
                String macBT = device.getAddress();
                ArrayBluetooth.add(nameBT + "\n" + macBT);
            }
        }

        setListAdapter(ArrayBluetooth);
    }
}
