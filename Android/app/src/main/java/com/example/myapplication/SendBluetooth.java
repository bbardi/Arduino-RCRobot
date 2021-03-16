package com.example.myapplication;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class SendBluetooth extends AppCompatActivity {
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String address = null;
    Button up,down,left,right,dc,clear,go;
    CustomCanvas canvas;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.bluetooth_mode);
        address = intent.getStringExtra("address");

        new ConnectBT().execute();
        up = findViewById(R.id.fwd);
        down = findViewById(R.id.back);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        dc = findViewById(R.id.dc);
        clear = findViewById(R.id.clear);
        go = findViewById(R.id.go);
        canvas = findViewById(R.id.canv);

        clear.setOnClickListener(v -> canvas.clearCanvas());
        dc.setOnClickListener(v-> Disconnect());
        up.setOnClickListener(v -> sendSignal("u"));
        down.setOnClickListener(v -> sendSignal("d"));
        left.setOnClickListener(v -> sendSignal("l"));
        right.setOnClickListener(v -> sendSignal("r"));
        go.setOnClickListener(v-> parsePath());
    }
    private void parsePath(){
        Log.i("[MyApp]","Parsing path");

        List<Point> pointList = canvas.getPoints();
        Vector init = new Vector(0,1);
        Vector curr;
        double initx = 0,inity = 0;
        boolean first = true;
        int i = 0;
        for(Point point : pointList){
            if(first){
                initx = point.x;
                inity = point.y;
                first = false;
            }else{
                curr = new Vector(initx - point.x,inity - point.y);
                initx = point.x;
                inity = point.y;
                curr.normalize();
                Log.i("[MyApp]","Normalized Vector "+ curr.toString());
                Log.i("[MyApp]","Calculated angle pair("+i+"):"+180*init.angle(curr)/Math.PI);
                if(init.Cross(curr)>=0){
                    if(180*init.angle(curr)/Math.PI<10){
                        //forward only
                    }
                    else
                    if(180*init.angle(curr)/Math.PI<=45 &&180*init.angle(curr)/Math.PI>=10 ){
                        sendSignal("r");
                    }else
                    if(180*init.angle(curr)/Math.PI<=70 && 180*init.angle(curr)/Math.PI>45){
                        sendSignal("rr");
                    }else
                    if(180*init.angle(curr)/Math.PI<135 && 180*init.angle(curr)/Math.PI>70){
                        sendSignal("rrr");
                    }
                    else{
                        sendSignal("rrrr");
                    }
                }else{
                    if(180*init.angle(curr)/Math.PI<10){
                        //forward only
                    }
                    else
                    if(180*init.angle(curr)/Math.PI<=45 &&180*init.angle(curr)/Math.PI>=10 ){
                        sendSignal("l");
                    }else
                    if(180*init.angle(curr)/Math.PI<=70 && 180*init.angle(curr)/Math.PI>45){
                        sendSignal("ll");
                    }else
                    if(180*init.angle(curr)/Math.PI<135 && 180*init.angle(curr)/Math.PI>70){
                        sendSignal("lll");
                    }
                    else{
                        sendSignal("llll");
                    }
                }
                sendSignal("u");
                i++;
                init = curr;
            }
        }
    }
    private void msg (String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void sendSignal ( String command ) {
        if ( btSocket != null ) {
            try {
                btSocket.getOutputStream().write(command.getBytes());
            } catch (IOException e) {
                msg("Error. Unable to send signal");
            }
        }
    }

    private void Disconnect () {
        if ( btSocket!=null ) {
            try {
                btSocket.close();
            } catch(IOException e) {
                msg("Error. Unable to close socket");
            }
        }

        finish();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected  void onPreExecute () {
        }

        @Override
        protected Void doInBackground (Void... devices) {
            try {
                if ( btSocket==null || !isBtConnected ) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = myBluetooth.getRemoteDevice(address);
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is this a compatible device?");
                finish();
            } else {
                msg("Connected");
                isBtConnected = true;
            }

        }
    }
}