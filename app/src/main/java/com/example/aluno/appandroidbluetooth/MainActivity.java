package com.example.aluno.appandroidbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mDevice;
    BluetoothSocket mSocket;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CONECTION_BT = 2;
    private static String MAC;
    Button btnConect, btnLed1, btnLed2, btnLed3;
    boolean conexao = false;

    UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConect = (Button) findViewById(R.id.btnConect);
        btnLed1 = (Button) findViewById(R.id.btnLed1);
        btnLed2 = (Button) findViewById(R.id.btnLed2);
        btnLed3 = (Button) findViewById(R.id.btnLed3);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Sem Bluetooth!", Toast.LENGTH_SHORT);
        } else if (mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        btnConect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conexao){
                    try {
                        mSocket.close();
                        conexao = false;
                        btnConect.setText("Conectar");
                        Toast.makeText(getApplicationContext(), "Bluetooth desconectado!", Toast.LENGTH_SHORT);

                    } catch (IOException error){
                        Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_SHORT);
                    }
                }else{
                    Intent abrirLista = new Intent(MainActivity.this, ListaDispositivos.class);
                    startActivityForResult(abrirLista, REQUEST_CONECTION_BT);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "Bluetooth Ativado!", Toast.LENGTH_SHORT);

                }else{
                    Toast.makeText(this, "Bluetooth não ativo, Byebye!", Toast.LENGTH_SHORT);
                    finish();
                }
            case REQUEST_CONECTION_BT:
                if(resultCode == Activity.RESULT_OK){
                    MAC = data.getExtras().getString(ListaDispositivos.ADRESS_MAC);
                    mDevice = mBluetoothAdapter.getRemoteDevice(MAC);
                    try {
                        mSocket = mDevice.createRfcommSocketToServiceRecord(mUUID);
                        mSocket.connect();
                        conexao = true;
                        btnConect.setText("Desconectar");
                        Toast.makeText(this, "Conectado" + MAC, Toast.LENGTH_SHORT);

                    }catch (IOException error){
                        conexao = false;
                        Toast.makeText(this, "Erro:" + error, Toast.LENGTH_SHORT);
                    }
                }else{
                    Toast.makeText(this, "Falha ao obter o MAC!", Toast.LENGTH_SHORT);

                }

        }


    }
}
