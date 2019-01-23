package com.example.aluno.appandroidbluetooth;

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

public class ListaDispositivos extends ListActivity {

    BluetoothAdapter mBluetoothAdapter;
    static String ADRESS_MAC = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> dispositivosPareados = mBluetoothAdapter.getBondedDevices();

        if(dispositivosPareados.size() > 0){
            for (BluetoothDevice dispositivo : dispositivosPareados){
                String nomeBt = dispositivo.getName();
                String macBt = dispositivo.getAddress();
                ArrayBluetooth.add(nomeBt + "\n" + macBt);
            }
        }
        setListAdapter(ArrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String infomation = ((TextView) v).getText().toString();
        String mac = infomation.substring(infomation.length()-17);

        Intent returnMac = new Intent();
        returnMac.putExtra(ADRESS_MAC, mac);
        setResult(RESULT_OK, returnMac);
        finish();
    }
}
