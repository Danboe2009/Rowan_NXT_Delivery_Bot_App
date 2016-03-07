package com.boehmke.robotprototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ImageView blue;
    private TextView connection;
    private EditText messageText;
    private Intent serviceIntent;
    private Button connectBut;
    private Button msgBut;

    private ImageButton upBut;
    private ImageButton downBut;
    private ImageButton leftBut;
    private ImageButton rightBut;

    private Boolean connected;

    private BT_Comm btComm;

    private static final String TAG = "Robot Prototype";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_screen);

        blue = (ImageView) findViewById(R.id.idBlueState);
        serviceIntent = new Intent(this, BluetoothState.class);
        connection = (TextView) findViewById(R.id.connection);
        messageText = (EditText) findViewById(R.id.messageText);

        connectBut = (Button) findViewById(R.id.connectButton);
        msgBut = (Button) findViewById(R.id.msgButton);

        upBut = (ImageButton) findViewById(R.id.upButton);
        downBut = (ImageButton) findViewById(R.id.downButton);
        leftBut = (ImageButton) findViewById(R.id.leftButton);
        rightBut = (ImageButton) findViewById(R.id.rightButton);

        connectBut.setOnClickListener(clickButton);
        msgBut.setOnClickListener(clickButton);

        upBut.setOnClickListener(clickButton);
        downBut.setOnClickListener(clickButton);
        leftBut.setOnClickListener(clickButton);
        rightBut.setOnClickListener(clickButton);

        upBut.setOnTouchListener(touchButton);
        downBut.setOnTouchListener(touchButton);
        leftBut.setOnTouchListener(touchButton);
        rightBut.setOnTouchListener(touchButton);

        CheckBlueTooth();

        Log.d("Robot Prototype", "App Started.");

        btComm = new BT_Comm();
    }

    private View.OnClickListener clickButton = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.connectButton:
                    input();
                    connected = btComm.connectToNXTs();
                    Log.d(TAG, connected.toString());
                    if (connected == true) {
                        connection.setText("Connected");
                        connection.setTextColor(Color.GREEN);
                    }
                    setVisible();
                    break;
                case R.id.msgButton:
                    input();
                    //Log.d(TAG,"" + Integer.valueOf(messageText.getText().toString()));
                    sendMessage(Integer.valueOf(messageText.getText().toString()));
                    break;
            }
        }
    };

    private View.OnTouchListener touchButton = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN)
                switch (v.getId()) {
                    case R.id.upButton:
                        input();
                        sendMessage(1);
                        driving();
                        break;
                    case R.id.downButton:
                        input();
                        sendMessage(2);
                        driving();
                        break;
                    case R.id.leftButton:
                        input();
                        sendMessage(4);
                        driving();
                        break;
                    case R.id.rightButton:
                        input();
                        sendMessage(5);
                        driving();
                        break;
                }
            else if (action == MotionEvent.ACTION_UP)
                switch (v.getId()) {
                    case R.id.upButton:
                    case R.id.downButton:
                        sendMessage(0);
                        stopped();
                        break;
                    case R.id.leftButton:
                    case R.id.rightButton:
                        sendMessage(3);
                        stopped();
                        break;
                }
            return false;
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
                                /*Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // Do nothing
                                dialog.dismiss();*/
                                System.exit(0);

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

    private void input() {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    private void setVisible() {
        if(connected)
        {
            upBut.setVisibility(View.VISIBLE);
            downBut.setVisibility(View.VISIBLE);
            leftBut.setVisibility(View.VISIBLE);
            rightBut.setVisibility(View.VISIBLE);
            msgBut.setVisibility(View.VISIBLE);
        }
        else
        {
            upBut.setVisibility(View.INVISIBLE);
            downBut.setVisibility(View.INVISIBLE);
            leftBut.setVisibility(View.INVISIBLE);
            rightBut.setVisibility(View.INVISIBLE);
            msgBut.setVisibility(View.INVISIBLE);
        }
    }

    public void sendMessage(int value) {
        try {
            btComm.writeMessage(value);
            Log.d(TAG, "Message sent: " + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void driving()
    {
        connection.setText("Driving");
        connection.setTextColor(Color.GREEN);
    }

    public void stopped(){
        connection.setText("Stopped");
        connection.setTextColor(Color.RED);
    }
}
