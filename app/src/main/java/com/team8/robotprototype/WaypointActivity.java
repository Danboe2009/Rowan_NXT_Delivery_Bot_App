package com.team8.robotprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Dan Boehmke on 3/21/2016.
 */
public class WaypointActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private ArrayList<Waypoint> points;
    private ArrayList<String> data = new ArrayList<>();

    private Button saveBut;
    private Button listBut;
    private Button pointsBut;

    private EditText nameEdit;
    private EditText xEdit;
    private EditText yEdit;
    private EditText headEdit;
    private CheckBox officeBox;

    public static WaypointDatabaseHelper database;

    private static final String TAG = "Robot Prototype";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoints);

        database = new WaypointDatabaseHelper(this, null, null, 1);

        points = new ArrayList<>();
        points = database.getWaypoints();

        if (points != null) {
            for (Waypoint way : points) {
                data.add(way.getName());
            }
        }

        saveBut = (Button) findViewById(R.id.saveButton);
        listBut = (Button) findViewById(R.id.listButton);
        pointsBut = (Button) findViewById(R.id.testPointsButton);

        nameEdit = (EditText) findViewById(R.id.editName);
        xEdit = (EditText) findViewById(R.id.editX);
        yEdit = (EditText) findViewById(R.id.editY);
        headEdit = (EditText) findViewById(R.id.editHeading);
        officeBox = (CheckBox) findViewById(R.id.officeBox);

        nameEdit.setText(R.string.elonMusk);
        xEdit.setText("" + new DecimalFormat("#").format(MainScreenActivity.X));
        yEdit.setText("" + new DecimalFormat("#").format(MainScreenActivity.Y));
        headEdit.setText("" + new DecimalFormat("#").format(MainScreenActivity.Head));

        saveBut.setOnClickListener(this);
        listBut.setOnClickListener(this);
        pointsBut.setOnClickListener(this);
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
                if (database.countCases() > 0) {
                    Intent myIntent = new Intent(this, WaypointHistoryActivity.class);
                    myIntent.putExtra("waypoints", database.getWaypoints());
                    startActivity(myIntent);
                } else {
                    Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                makeWaypoint();
                break;
            case R.id.listButton:
                if (database.countCases() > 0) {
                    Intent myIntent = new Intent(this, WaypointHistoryActivity.class);
                    myIntent.putExtra("waypoints", database.getWaypoints());
                    startActivity(myIntent);
                } else {
                    Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.testPointsButton:
                setTestPoints();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected");
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void makeWaypoint() {
        Waypoint tempWay = new Waypoint(nameEdit.getText().toString(),
                Float.parseFloat(xEdit.getText().toString()),
                Float.parseFloat(yEdit.getText().toString()),
                Float.parseFloat(headEdit.getText().toString()),
                officeBox.isChecked());
        database.addWaypoint(tempWay);

        points = database.getWaypoints();
        for (Waypoint way : points) {
            data.add(way.getName());
        }
    }

    public void setTestPoints() {
        Waypoint zero = new Waypoint("Zero", 0, 0, 0, true);
        database.addWaypoint(zero);
        Waypoint one = new Waypoint("Intersection", 97, 0, 0, false);
        database.addWaypoint(one);
        Waypoint two = new Waypoint("Dr. Elon Musk", 97, -46, 0, true);
        database.addWaypoint(two);
        Waypoint three = new Waypoint("Intersection", 97, -91, 0, false);
        database.addWaypoint(three);
        Waypoint four = new Waypoint("Dr. Chris", 32, -91, 0, true);
        database.addWaypoint(four);
    }
}
