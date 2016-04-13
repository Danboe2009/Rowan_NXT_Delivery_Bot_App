package com.team8.robotprototype;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Waypoint>
        implements View.OnClickListener {

    Context parentContext;
    Activity activity;
    Waypoint p;
    DataSetObserver observer;

    // Logcat tag
    private static final String TAG = "Robot Prototype";

    public ListAdapter(Activity context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.activity = context;
        observer = new WaypointHistoryObserver(activity);
        registerDataSetObserver(observer);
    }

    public ListAdapter(Activity context, int resource, List<Waypoint> items) {
        super(context, resource, items);
        this.activity = context;
        observer = new WaypointHistoryObserver(activity);
        registerDataSetObserver(observer);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.activity_item, null);
        }

        p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.nameTextView);
            TextView tt2 = (TextView) v.findViewById(R.id.xTextView);
            TextView tt3 = (TextView) v.findViewById(R.id.yTextView);
            TextView tt4 = (TextView) v.findViewById(R.id.headingTextView);
            TextView isOfficeView = (TextView) v.findViewById(R.id.isOfficeTextView);
            Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
            Button navigateButton = (Button) v.findViewById(R.id.navigateButton);

            deleteButton.setOnClickListener(this);
            navigateButton.setOnClickListener(this);

            deleteButton.setContentDescription(String.valueOf(p.getId()));
            navigateButton.setContentDescription(String.valueOf(p.getId()));

            parentContext = parent.getContext();

            if (p.isOffice()) {
                isOfficeView.setText("(Office)");
                Log.d("Waypoint", "is office = true");
            } else {
                Log.d("Waypoint", "is office = false");
            }

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText("X = " + p.getX());
            }

            if (tt3 != null) {
                tt3.setText("Y = " + p.getY());
            }

            if (tt4 != null) {
                tt4.setText("Heading = " + p.getHeading());
            }
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteButton:
                Log.d(TAG, "Delete: " + v.getContentDescription());
                Waypoint way = WaypointHistoryActivity.items.get(Integer.parseInt(v.getContentDescription().toString()) - 1);
                WaypointActivity.database.deleteWaypoint(way.getdId());
                Toast.makeText(v.getContext(), "Entry Deleted.", Toast.LENGTH_SHORT).show();
                Log.d("DELETING", v.getContentDescription().toString());
                notifyDataSetChanged();
                break;
            case R.id.navigateButton:
                Log.d(TAG, "Navigate: " + v.getContentDescription());
                Waypoint w = WaypointHistoryActivity.items.get(Integer.parseInt(v.getContentDescription().toString()) - 1);
                //Waypoint w = WaypointActivity.database.getWaypoint(Integer.parseInt(v.getContentDescription().toString()));
                Toast.makeText(v.getContext(), "Name = " + w.getName() + " X = " + w.getX() + " Y = " + w.getY() + " Heading = " + w.getHeading(), Toast.LENGTH_SHORT).show();
                MainScreenActivity.selectWaypoints(w);
                break;
        }
    }
}