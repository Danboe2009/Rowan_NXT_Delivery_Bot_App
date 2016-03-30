package com.boehmke.robotprototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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
    private TextView currentX;
    private EditText messageText;
    private Intent serviceIntent;

    private Button connectBut;
    private Button msgBut;
    private Button readBut;
    private Button wayBut;
    private Button testBut;

    private ImageButton upBut;
    private ImageButton downBut;
    private ImageButton leftBut;
    private ImageButton rightBut;

    private Boolean connected;

    private static BT_Comm btComm;

    private static final String TAG = "Robot Prototype";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_screen);

        blue = (ImageView) findViewById(R.id.idBlueState);
        serviceIntent = new Intent(this, BluetoothState.class);
        connection = (TextView) findViewById(R.id.connection);
        currentX = (TextView) findViewById(R.id.currentX);
        messageText = (EditText) findViewById(R.id.messageText);

        connectBut = (Button) findViewById(R.id.connectButton);
        msgBut = (Button) findViewById(R.id.msgButton);
        readBut = (Button) findViewById(R.id.readButton);
        wayBut = (Button) findViewById(R.id.setWaypoint);
        testBut = (Button) findViewById(R.id.testButton);

        upBut = (ImageButton) findViewById(R.id.upButton);
        downBut = (ImageButton) findViewById(R.id.downButton);
        leftBut = (ImageButton) findViewById(R.id.leftButton);
        rightBut = (ImageButton) findViewById(R.id.rightButton);

        connectBut.setOnClickListener(clickButton);
        msgBut.setOnClickListener(clickButton);
        readBut.setOnClickListener(clickButton);
        wayBut.setOnClickListener(clickButton);
        testBut.setOnClickListener(clickButton);

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

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE support.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "LE support.", Toast.LENGTH_SHORT).show();
        }
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
                    //setVisible();
                    break;
                case R.id.msgButton:
                    input();
                    //Log.d(TAG,"" + Integer.valueOf(messageText.getText().toString()));
                    sendMessage(Integer.parseInt(messageText.getText().toString()));
                    break;
                case R.id.readButton:
                    input();
                    readMessage();
                    break;
                case R.id.setWaypoint:
                    openWaypoint();
                    break;
                case R.id.testButton:
                    input();
                    testNav();
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
                        sendMessage(3);
                        driving();
                        break;
                    case R.id.rightButton:
                        input();
                        sendMessage(4);
                        driving();
                        break;
                }
            else if (action == MotionEvent.ACTION_UP)
                switch (v.getId()) {
                    case R.id.upButton:
                    case R.id.downButton:
                    case R.id.leftButton:
                    case R.id.rightButton:
                        sendMessage(0);
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
            readBut.setVisibility(View.VISIBLE);
            currentX.setVisibility(View.VISIBLE);
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

    public static void sendMessage(int value) {
        try {
            btComm.writeMessage(value);
            Log.d(TAG, "Message sent: " + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void readMessage() {
        sendMessage(5);
        String msg = btComm.readMessage();
        Log.d(TAG, "Message read: " + msg);
        currentX.setText("" + msg);
    }

    public void openWaypoint(){
        Intent myIntent = new Intent(this,WaypointActivity.class);
        startActivity(myIntent);
    }

    public void driving()
    {
        connection.setText("Driving");
        connection.setTextColor(Color.GREEN);
    }

    public void stopped(){
        connection.setText("Stopped");
        connection.setTextColor(Color.RED);
        readMessage();
    }

    public void testNav() {
        sendMessage(6);
        sendMessage(10);
        sendMessage(0);
        sendMessage(0);
        sendMessage(-2);
    }

    public static void navigate(Waypoint w) {
        sendMessage(6);
        sendMessage((int) w.getX());
        sendMessage((int) w.getY());
        sendMessage((int) w.getHeading());
        sendMessage(-2);
    }
}
