package com.boehmke.robotprototype;

import android.app.Activity;
import android.os.Bundle;
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
}
