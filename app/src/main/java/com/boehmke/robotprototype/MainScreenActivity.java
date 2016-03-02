package com.boehmke.robotprototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class MainScreenActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ImageView blue;
    private Intent serviceIntent;
    private Button connectBut;
    private Button msgBut;
    //Target NXTs for communication
    final String nxt1 = "00:16:53:11:5B:09";

    BluetoothAdapter localAdapter;
    BluetoothSocket socket_nxt1;
    boolean success=false;

    BT_Comm btComm;

    private static final String TAG = "Robot Prototype";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_screen);

        blue = (ImageView) findViewById(R.id.idBlueState);
        serviceIntent = new Intent(this, BluetoothState.class);

        connectBut = (Button) findViewById(R.id.connectButton);
        msgBut = (Button) findViewById(R.id.msgButton);

        connectBut.setOnClickListener(clickButton);
        msgBut.setOnClickListener(clickButton);

        CheckBlueTooth();

        Log.d("Robot Prototype", "App Started.");

        btComm = new BT_Comm();
    }

    private View.OnClickListener clickButton = new View.OnClickListener()
    {

        public void onClick(View v)
        {
            switch (v.getId()) {
                case R.id.connectButton:
                    Boolean connected = btComm.connectToNXTs();
                    Log.d(TAG, connected.toString());
                    break;
                case R.id.msgButton:
                    try {
                        btComm.writeMessage("hello");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }

    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothState(intent);
        }
    };

    public void onResume() {
        super.onResume();
        startService(serviceIntent);
        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothState.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(serviceIntent);
    }

    private void CheckBlueTooth() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                mBluetoothAdapter.enable();
                                Toast.makeText(getApplicationContext(), "Bluetooth enabled.", Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // Do nothing
                                dialog.dismiss();

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Bluetooth is needed to connect to the robot. Turn it on?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        }
    }

    private void BluetoothState(Intent intent) {
        //Log.d(TAG, intent.getStringExtra("State"));
        if (intent.getStringExtra("State").equals("0")) {
            blue.setImageResource(R.drawable.blueoff);
        } else if (intent.getStringExtra("State").equals("1")) {
            blue.setImageResource(R.drawable.blueon);
        }
    }

    public  boolean connectToNXT(){
        //get the BluetoothDevice of the NXT
        BluetoothDevice nxt = localAdapter.getRemoteDevice(nxt1);
        //try to connect to the nxt
        try {
            socket_nxt1 = nxt.createRfcommSocketToServiceRecord(UUID
                    .fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket_nxt1.connect();
            success = true;
        } catch (IOException e) {
            Log.d(TAG,"Err: Device not found or cannot connect");
            success=false;
        }
        return success;
    }

    public void writeMessage(byte msg, String nxt) throws InterruptedException{
        BluetoothSocket connSock;
        //Swith nxt socket
        if(nxt.equals("nxt1")){
            connSock=socket_nxt1;
        }else{
            connSock=null;
        }

        if(connSock!=null){
            try {

                OutputStreamWriter out=new OutputStreamWriter(connSock.getOutputStream());
                out.write(msg);
                out.flush();

                Thread.sleep(1000);


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            //Error
        }
    }

    public int readMessage(String nxt){
        BluetoothSocket connSock;
        int n;
        //Swith nxt socket
        if(nxt.equals("nxt1")){
            connSock=socket_nxt1;
        }else{
            connSock=null;
        }

        if(connSock!=null){
            try {

                InputStreamReader in=new InputStreamReader(connSock.getInputStream());
                n=in.read();

                return n;


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return -1;
            }
        }else{
            //Error
            return -1;
        }
    }
}
