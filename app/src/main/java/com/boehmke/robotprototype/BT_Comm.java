package com.boehmke.robotprototype;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.util.Log;

public class BT_Comm {

    //Target NXTs for communication
    final String nxt1 = "00:16:53:11:5B:09";
    private static final String TAG = "Robot Prototype";

    BluetoothAdapter localAdapter;
    BluetoothSocket socket_nxt1;
    boolean success = false;


    //Enables Bluetooth if not enabled
    public void enableBT() {
        localAdapter = BluetoothAdapter.getDefaultAdapter();
        //If Bluetooth not enable then do it
        if (localAdapter.isEnabled() == false) {
            localAdapter.enable();
            while (!(localAdapter.isEnabled())) {

            }
        }
    }

    //connect to both NXTs
    public boolean connectToNXTs() {
        //get the BluetoothDevice of the NXT
        //BluetoothDevice nxt_1 = localAdapter.getRemoteDevice(nxt1);
        BluetoothDevice nxt_1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(nxt1);
        //try to connect to the nxt
        try {
            socket_nxt1 = nxt_1.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket_nxt1.connect();
            success = true;
        } catch (IOException e) {
            Log.d("Robot Prototype", "Err: Device not found or cannot connect");
            success = false;
        }
        return success;
    }

    public void writeMessage(int nxt) throws InterruptedException {
        BluetoothSocket connSock;

        connSock = socket_nxt1;

        if (connSock != null) {
            try {
                OutputStreamWriter out = new OutputStreamWriter(connSock.getOutputStream());
                out.write(nxt);
                out.flush();
                //Log.d(TAG, "Write Successful!");
                //Thread.sleep(1000);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d(TAG, "Unable to write");
            }
        } else {
            //Error
        }
    }

    public int readMessage() {
        BluetoothSocket connSock;
        int n;

        connSock = socket_nxt1;

        if (connSock != null) {
            try {

                InputStreamReader in = new InputStreamReader(connSock.getInputStream());
                n = in.read();

                return n;


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return -1;
            }
        } else {
            //Error
            return -1;
        }

    }

}