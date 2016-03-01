package com.boehmke.robotprototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainScreenActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ImageView blue;
    private Intent serviceIntent;

    private static final String TAG = "Robot Prototype";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_screen);

        blue = (ImageView) findViewById(R.id.idBlueState);
        serviceIntent = new Intent(this,BluetoothState.class);

        CheckBlueTooth();

        Log.d("Robot Prototype", "App Started.");
    }

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

    private void BluetoothState(Intent intent){
        Log.d(TAG,intent.getStringExtra("State"));
        if(intent.getStringExtra("State").equals("0"))
        {
            blue.setImageResource(R.drawable.blueoff);
        }
        else if(intent.getStringExtra("State").equals("1"))
        {
            blue.setImageResource(R.drawable.blueon);
        }
    }
}
