package com.boehmke.robotprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Dan Boehmke on 3/7/2016.
 */
public class WaypointHistoryActivity extends Activity {

    // Logcat tag
    private static final String TAG = "Robot Prototype";

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.waypointsListView);

        // get the items for the feed
        ArrayList<Waypoint> items = (ArrayList<Waypoint>) getIntent().getExtras().getSerializable("waypoints");

        ListAdapter customAdapter = new ListAdapter(this, R.layout.activity_history, items);

        listView.setAdapter(customAdapter);
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
                startActivity(new Intent(getApplicationContext(), WaypointHistoryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
