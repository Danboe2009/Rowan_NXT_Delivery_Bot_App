package com.boehmke.robotprototype;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dan Boehmke on 2/29/2016.
 */
public class BluetoothState extends Service {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final String TAG = "Robot Prototype";
    public static final String BROADCAST_ACTION = "com.boehmke.robotprototype.MainScreenActivity";
    private final Handler handler = new Handler();
    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);

        //Toast toast = Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT);
        //toast.show();

        Log.d("Robot Prototype", "Service Started.");
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            //Log.d("Robot Prototype", "State Changed!");
            if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                intent.putExtra("State", "0");
            } else if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                intent.putExtra("State", "1");
            }
            sendBroadcast(intent);
            handler.postDelayed(this, 1000); // 5 seconds
        }
    };

    public void onDestroy() {
        //Toast toast = Toast.makeText(getApplicationContext(), "Service Destroyed", Toast.LENGTH_SHORT);
        //toast.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
