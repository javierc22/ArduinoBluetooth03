package com.javierec.arduinobluetooth03;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        // Consulta de dispositivos sincronizados (pareados)
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter2.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for(BluetoothDevice device :pairedDevices) {
                String nameBT = device.getName();
                String macBT = device.getAddress();
                ArrayBluetooth.add(nameBT + "\n" + macBT);
            }
        }

        // Setear Array de dispositivos obtenidos en lista
        setListAdapter(ArrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        String generalInformation = ((TextView) view).getText().toString();
       // Toast.makeText(this, "Info: " + generalInformation, Toast.LENGTH_SHORT).show();

        String addressMAC = generalInformation.substring(generalInformation.length() - 17);
        // Toast.makeText(this, "MAC: " + addressMAC, Toast.LENGTH_SHORT).show();

        Intent intentReturnMac = new Intent();
        intentReturnMac.putExtra(ADDRESS_MAC, addressMAC);
        setResult(RESULT_OK, intentReturnMac);
        finish();
    }
}
