package com.boehmke.robotprototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Dan Boehmke on 3/21/2016.
 */
public class WaypointActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Button saveBut;
    private View.OnClickListener clickButton = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.saveButton:
                    break;
            }
        }
    };

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

        saveBut.setOnClickListener(clickButton);
    }

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
}
