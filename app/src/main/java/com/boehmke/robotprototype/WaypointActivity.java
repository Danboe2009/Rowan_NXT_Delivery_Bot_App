package com.boehmke.robotprototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Dan Boehmke on 3/21/2016.
 */
public class WaypointActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Button saveBut;

    private EditText nameEdit;
    private EditText xEdit;
    private EditText yEdit;
    private EditText headEdit;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoints);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.office_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        saveBut = (Button) findViewById(R.id.saveButton);

        nameEdit = (EditText) findViewById(R.id.editName);
        xEdit = (EditText) findViewById(R.id.editX);
        yEdit = (EditText) findViewById(R.id.editY);
        headEdit = (EditText) findViewById(R.id.editHeading);

        nameEdit.setText("Elon Musk");
        xEdit.setText("0.0");
        yEdit.setText("0.0");
        headEdit.setText("0.0");

        saveBut.setOnClickListener(clickButton);
    }

    private View.OnClickListener clickButton = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.saveButton:
                    makeWaypoint();
                    break;
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                Float.parseFloat(headEdit.getText().toString()));
    }
}
