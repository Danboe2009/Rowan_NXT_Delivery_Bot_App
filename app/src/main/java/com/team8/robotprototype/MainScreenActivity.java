package com.team8.robotprototype;

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
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;

public class MainScreenActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ImageView blue;
    private TextView connection;
    private static TextView currentX;
    private TextView currentY;
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

    public static float X;
    public static float Y;
    public static float Head;
    public static boolean _onWaypoint = false;
    public static Waypoint _lastWaypoint;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_screen);

        blue = (ImageView) findViewById(R.id.idBlueState);
        serviceIntent = new Intent(this, BluetoothState.class);
        connection = (TextView) findViewById(R.id.connection);
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
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

       //Log.d("Robot Prototype", "App Started.");

        btComm = new BT_Comm();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE support.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "LE support.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_main:
                startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                return true;
            case R.id.menu_waypoint:
                startActivity(new Intent(getApplicationContext(), WaypointActivity.class));
                return true;
            case R.id.menu_database:
                if (WaypointActivity.database.countCases() > 0) {
                    Intent myIntent = new Intent(this, WaypointHistoryActivity.class);
                    myIntent.putExtra("waypoints", WaypointActivity.database.getWaypoints());
                    startActivity(myIntent);
                } else {
                    Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private View.OnClickListener clickButton = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.connectButton:
                    input();
                    connected = btComm.connectToNXT();
                    //Log.d(TAG, connected.toString());
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

    public static void sendMessage(int value) {
        try {
            btComm.writeMessage(value);
            //Log.d(TAG, "Message sent: " + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void readMessage() {
        sendMessage(5);
        String msg = btComm.readMessage();
        //Log.d(TAG, "Message read: " + msg);
        currentX.setText("" + msg);
    }

    public void openWaypoint() {
        Intent myIntent = new Intent(this, WaypointActivity.class);
        startActivity(myIntent);
    }

    public void driving() {
        connection.setText("Driving");
        connection.setTextColor(Color.GREEN);
        _onWaypoint = false;
    }

    public void stopped() {
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
        _onWaypoint = true;
        _lastWaypoint = w;
    }

    public static void navigate(ArrayList<Waypoint> w) {
        //Log.d(TAG, "Size = " + w.size());
        sendMessage(6);
        //Log.d(TAG, "navigate: 6");
        for (int i = 0; i < w.size(); i++) {
            sendMessage((int) w.get(i).getX());
            //Log.d(TAG, "navigate: X");
            sendMessage((int) w.get(i).getY());
            //Log.d(TAG, "navigate: Y");
            sendMessage((int) w.get(i).getHeading());
            //Log.d(TAG, "navigate: Heading");
            if (i != w.size() - 1) {
                sendMessage(1);
                //Log.d(TAG, "navigate: 1");
            }
        }
        sendMessage(-2);
        //Log.d(TAG, "navigate: -2");
        _onWaypoint = true;
        //Log.d(TAG,"Size = " + w.size());
        if(w.size() != 0) {
            _lastWaypoint = w.get(w.size() - 1);
        }
    }

    public static void selectWaypoints(Waypoint goal) {
        Waypoint curr = null;
        ArrayList<Waypoint> waypoints = WaypointHistoryActivity.items;
        //Log.d(TAG,"ITEMS: " + WaypointHistoryActivity.items.size());
        if (_onWaypoint) {
            curr = _lastWaypoint;
        } else {
            readMessage();
            float actual_x = X;
            float actual_y = Y;

            for (int i = 50; curr == null; i += 50) {
                for (int j = 0; j < waypoints.size(); j++) {
                    if (((actual_x - i) <= waypoints.get(j).getX()) &&
                            (waypoints.get(j).getX() <= (actual_x + i)) &&
                            ((actual_y - i) <= waypoints.get(j).getY()) &&
                            (waypoints.get(j).getY() <= (actual_y + i))) {
                        curr = waypoints.get(j);
                        //Log.d(TAG, "curr = " + curr.getName());
                        //navigate(w);
                    }
                }
            }
        }

        ArrayList<Waypoint> path = new ArrayList<>();
        int index = 0;
        boolean goalReached = false;

        if (curr.getId() == goal.getId()) {
            //do nothing
        } else if (curr.getId() < goal.getId()) {
            //increment
            for (int i = (int) curr.getId(); !goalReached; i++) {
                if (i >= waypoints.size()) {
                    i -= waypoints.size();
                }

                if (waypoints.get(i).getId() == goal.getId()) {
                    goalReached = true;
                    path.add(index++, waypoints.get(i));
                } else if (!waypoints.get(i).isOffice()) {
                    //add intersection
                    path.add(index++, waypoints.get(i));
                }
            }
        } else {
            //decrement
            for (int i = (int) curr.getId() - 1; !goalReached; i--) {
                if (i < 0) {
                    i += waypoints.size();
                }
                if (waypoints.get(i).getId() == goal.getId()) {
                    goalReached = true;
                    path.add(index++, waypoints.get(i));
                } else if (!waypoints.get(i).isOffice()) {
                    //add intersection
                    path.add(index++, waypoints.get(i));
                }
            }
        }
        //Log.d(TAG,"Path size = " + path.size());
        if(path.size() != 0) {
            navigate(path);
        }
    }
}
