package com.boehmke.robotprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan Boehmke on 3/7/2016.
 */
public class WaypointHistoryActivity extends Activity {

    // Logcat tag
    private static final String TAG = "Robot Prototype";

    ListView listView;
    TextView noWaypointTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.waypointsListView);
        noWaypointTextView = (TextView) findViewById(R.id.noWaypointTextView);

        // get the items for the feed
        //ArrayList<Waypoint> items = (ArrayList<Waypoint>) getIntent().getExtras().getSerializable("waypoints");
        ArrayList<Waypoint> items = WaypointActivity.database.getWaypoints();

        ListAdapter customAdapter = new ListAdapter(this, R.layout.activity_history, items);

        listView.setAdapter(customAdapter);

        List<Waypoint> waypointList = WaypointActivity.database.getWaypoints();
        for (Waypoint w : waypointList) {
            Log.d("WP" + w.getName(), String.valueOf(w.getId()));
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

    public void refreshAdapter() {
        ArrayList<Waypoint> items = WaypointActivity.database.getWaypoints();
        if (items != null) {
            listView.setAdapter(new ListAdapter(this, R.layout.activity_history, items));
        } else {
            listView.setAdapter(new ListAdapter(this, R.layout.activity_history, new ArrayList<Waypoint>()));
            noWaypointTextView.setText("No waypoints saved!");
        }
    }
}
