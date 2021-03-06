package com.boehmke.robotprototype;

import android.app.Activity;
import android.database.DataSetObserver;

/**
 * Created by Kevin on 4/6/2016.
 *
 * Observer for list of waypoints.
 */public class WaypointHistoryObserver extends DataSetObserver {

    private WaypointHistoryActivity activity;

    WaypointHistoryObserver(Activity activity) {
        this.activity = (WaypointHistoryActivity) activity;
    }

    @Override
    public void onChanged() {
        activity.refreshAdapter();
    }
}
