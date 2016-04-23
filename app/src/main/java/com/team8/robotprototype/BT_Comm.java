package com.team8.robotprototype;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class BT_Comm {

    //Target NXTs for communication
    final String nxt1 = "00:16:53:11:5B:09";
    private static final String TAG = "Robot Prototype";

    BluetoothAdapter localAdapter;
    BluetoothSocket socket_nxt1;
    boolean success = false;


    //connect to both NXTs
    public boolean connectToNXT() {
        //get the BluetoothDevice of the NXT
         BluetoothDevice nxt_1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(nxt1);
        //try to connect to the nxt
        try {
            socket_nxt1 = nxt_1.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket_nxt1.connect();
            success = true;
        } catch (IOException e) {
            //Log.d("Robot Prototype", "Err: Device not found or cannot connect");
            success = false;
        }
        return success;
    }

    public void writeMessage(int nxt) throws InterruptedException {
        BluetoothSocket connSock;

        connSock = socket_nxt1;

        if (connSock != null) {
            try {
                DataOutputStream out = new DataOutputStream(connSock.getOutputStream());
                out.writeInt(nxt);
                out.flush();
                //Log.d(TAG, "Write Successful!");
                //Thread.sleep(1000);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Log.d(TAG, "Unable to write");
            }
        } else {
            //Error
        }
    }

    public String readMessage() {
        BluetoothSocket connSock;
        float x;
        float y;
        float h;

        connSock = socket_nxt1;

        if (connSock != null) {
            try {
                //Log.d(TAG,"line 1");
                //InputStreamReader in = new InputStreamReader(connSock.getInputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(connSock.getInputStream()));
                //Log.d(TAG,"line 2");
                x = Float.parseFloat(in.readLine());
                //Log.d(TAG,"line x");
                y = Float.parseFloat(in.readLine());
                //Log.d(TAG,"line y");
                h = Float.parseFloat(in.readLine());
                //Log.d(TAG,"line h");
                //Log.d(TAG, "X = " + x);
                //Log.d(TAG, "Y = " + y);
                //Log.d(TAG, "h = " + h);

                String cat = " x = " + x + " y = " + y + " heading = " + h;
                MainScreenActivity.X = x;
                MainScreenActivity.Y = y;
                MainScreenActivity.Head = h;
                return cat;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Log.d(TAG, "Exception");
                return "-1";
            }
        } else {
            //Error
            //Log.d(TAG, "Error");
            return "-1";
        }
    }
}