package com.boehmke.robotprototype;

import android.content.Context;
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
    Waypoint p;

    // Logcat tag
    private static final String TAG = "Robot Prototype";

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<Waypoint> items) {
        super(context, resource, items);
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
            Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
            Button navigateButton = (Button) v.findViewById(R.id.navigateButton);

            deleteButton.setOnClickListener(this);
            navigateButton.setOnClickListener(this);

            deleteButton.setContentDescription(String.valueOf(p.getId()));
            navigateButton.setContentDescription(String.valueOf(p.getId()));

            parentContext = parent.getContext();

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
                break;
            case R.id.navigateButton:
                Log.d(TAG, "onClick: " + v.getId());
                Waypoint w = WaypointActivity.database.getWaypoint(v.getId());
        Toast.makeText(v.getContext(), "Name = " + w.getName() + " X = " + w.getX() + " Y = " + w.getY() + " Heading = " + w.getHeading(), Toast.LENGTH_SHORT).show();
        MainScreenActivity.navigate(w);
                break;
        }
    }
}