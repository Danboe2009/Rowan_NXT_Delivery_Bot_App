package com.boehmke.robotprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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

        // Spinner element
        //Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        if (points != null) {
            for (Waypoint way : points) {
                data.add(way.getName());
            }
        }

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, data);
        //adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //spinner.setAdapter(adapter);

        saveBut = (Button) findViewById(R.id.saveButton);
        listBut = (Button) findViewById(R.id.listButton);
        pointsBut = (Button) findViewById(R.id.testPointsButton);

        nameEdit = (EditText) findViewById(R.id.editName);
        xEdit = (EditText) findViewById(R.id.editX);
        yEdit = (EditText) findViewById(R.id.editY);
        headEdit = (EditText) findViewById(R.id.editHeading);
        officeBox = (CheckBox) findViewById(R.id.officeBox);

        nameEdit.setText("Elon Musk");
        xEdit.setText("0.0");
        yEdit.setText("0.0");
        headEdit.setText("0.0");

        saveBut.setOnClickListener(this);
        listBut.setOnClickListener(this);
        pointsBut.setOnClickListener(this);
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
        Waypoint one = new Waypoint("One", 315, 0, 0, true);
        database.addWaypoint(one);
        Waypoint two = new Waypoint("Two", 534, 0, 0, true);
        database.addWaypoint(two);
        Waypoint three = new Waypoint("Three", 1208, 0, 0, true);
        database.addWaypoint(three);
        Waypoint four = new Waypoint("Four", 1377, 0, 0, true);
        database.addWaypoint(four);
    }
}
