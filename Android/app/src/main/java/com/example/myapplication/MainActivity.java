package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ListView view;
    Button refreshButton;
    BluetoothAdapter adapter = null;
    private Set<BluetoothDevice> pairedDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view  = findViewById(R.id.listview);
        refreshButton = findViewById(R.id.button2);
        adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            Toast.makeText(getApplicationContext(),"Bluetooth unavailable", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(adapter.isEnabled()){
            Intent turnBTOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTOn,1);
        }
        refreshButton.setOnClickListener(v -> ListDevices());
    }
    private void ListDevices(){
        pairedDevices = adapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevices.size() >0){
            for(BluetoothDevice bt : pairedDevices){
                list.add(bt.getName()+ "\n" + bt.getAddress());
            }
        }
        else
            Toast.makeText(getApplicationContext(),"No Paired Devices found", Toast.LENGTH_LONG).show();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        view.setAdapter(adapter);
        view.setOnItemClickListener(clickListener);
    }
    private AdapterView.OnItemClickListener clickListener = (parent, view, position, id) -> {
        String info = ((TextView) view).getText().toString();
        String address = info.substring(info.length()-17);

        Intent i = new Intent(MainActivity.this, SendBluetooth.class);
        i.putExtra("address", address);
        startActivity(i);
    };
}