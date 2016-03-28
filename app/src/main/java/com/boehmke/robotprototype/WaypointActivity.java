package com.boehmke.robotprototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Dan Boehmke on 3/21/2016.
 */
public class WaypointActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private ArrayList<Waypoint> points;
    private ArrayList<String> data = new ArrayList<>();

    private Button saveBut;

    private EditText nameEdit;
    private EditText xEdit;
    private EditText yEdit;
    private EditText headEdit;
    private CheckBox officeBox;

    public static WaypointDatabaseHelper database;

    private boolean checked;

    private static final String TAG = "Robot Prototype";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoints);

        points = new ArrayList<>();

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        for (Waypoint way : points) {
            data.add(way.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        saveBut = (Button) findViewById(R.id.saveButton);

        nameEdit = (EditText) findViewById(R.id.editName);
        xEdit = (EditText) findViewById(R.id.editX);
        yEdit = (EditText) findViewById(R.id.editY);
        headEdit = (EditText) findViewById(R.id.editHeading);
        officeBox = (CheckBox) findViewById(R.id.officeBox);

        nameEdit.setText("Elon Musk");
        xEdit.setText("0.0");
        yEdit.setText("0.0");
        headEdit.setText("0.0");

        saveBut.setOnClickListener(clickButton);
        officeBox.setOnClickListener(clickButton);

        database = new WaypointDatabaseHelper(this, null, null, 1);
    }

    private View.OnClickListener clickButton = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.saveButton:
                    makeWaypoint();
                    break;
                case R.id.officeBox:
                    CheckBox checkBox = (CheckBox) v;
                    checked = checkBox.isChecked();
                    break;
            }
        }
    };

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
                checked);
        points.add(tempWay);

        for (Waypoint way : points) {
            data.add(way.getName());
        }
    }
}
