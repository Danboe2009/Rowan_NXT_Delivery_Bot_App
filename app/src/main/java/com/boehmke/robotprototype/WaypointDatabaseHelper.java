package com.boehmke.robotprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Dan Boehmke on 3/28/2016.
 *
 * Database helper for database of waypoints.
 */
public class WaypointDatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String TAG = "Robot Prototype";

    // Database Constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "robotPrototype.db";

    // Table Names
    public static final String TABLE_WAYPOINTS = "waypoints";

    // Common column names
    public static final String KEY_ID = "_id";
    public static final String WAYPOINT_NAME = "name";
    public static final String WAYPOINT_X = "x";
    public static final String WAYPOINT_Y = "y";
    public static final String WAYPOINT_HEAD = "heading";
    public static final String WAYPOINT_OFFICE = "isOffice";

    private long count;

    public WaypointDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        Log.d(TAG, "Database Loaded");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_WAYPOINTS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WAYPOINT_NAME + " TEXT, " + WAYPOINT_X + " TEXT, " +
                WAYPOINT_Y + " TEXT, " + WAYPOINT_HEAD + " TEXT, " +
                WAYPOINT_OFFICE + " TEXT " +
                ");";
        db.execSQL(query);
        Log.d(TAG, query);
        Log.d(TAG, "Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAYPOINTS);
        onCreate(db);
    }

    public void addWaypoint(Waypoint way) {
        ContentValues values = new ContentValues();
        values.put(WAYPOINT_NAME, way.getName());
        values.put(WAYPOINT_X, way.getX());
        values.put(WAYPOINT_Y, way.getY());
        values.put(WAYPOINT_HEAD, way.getHeading());
        values.put(WAYPOINT_OFFICE, way.isOffice());

        Log.d(TAG, "Values = " + values.toString());

        SQLiteDatabase db = getWritableDatabase();
        count = db.insert(TABLE_WAYPOINTS, null, values);
        //Log.d(TAG, "count = " + count);
        db.close();
    }

    public ArrayList<Waypoint> getWaypoints() {
        int size = countCases();
        //Log.d(TAG,"Size = " + size);
        if (size > 0) {
            ArrayList<Waypoint> tempList = new ArrayList<>();

            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_WAYPOINTS + " WHERE 1";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Waypoint tempWay = new Waypoint();
                tempWay.setId(Long.parseLong(cursor.getString(0)));
                tempWay.setName(cursor.getString(1));
                tempWay.setX(Float.parseFloat(cursor.getString(2)));
                tempWay.setY(Float.parseFloat(cursor.getString(3)));
                tempWay.setHeading(Float.parseFloat(cursor.getString(4)));
                tempWay.setOfficeString(cursor.getString(5));

                tempList.add(tempWay);

                cursor.moveToNext();
            }
            db.close();

            return tempList;
        }

        return null;
    }

    public int countCases() {

        String SQLQuery = "SELECT COUNT(" + KEY_ID + ") FROM " + TABLE_WAYPOINTS + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQLQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public Waypoint getWaypoint(int id) {
        Waypoint tempWay = new Waypoint();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WAYPOINTS + " WHERE 1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (Long.parseLong(cursor.getString(0)) == id) {
                tempWay.setId(Long.parseLong(cursor.getString(0)));
                tempWay.setName(cursor.getString(1));
                tempWay.setX(Float.parseFloat(cursor.getString(2)));
                tempWay.setY(Float.parseFloat(cursor.getString(3)));
                tempWay.setHeading(Float.parseFloat(cursor.getString(4)));
                tempWay.setOfficeString(cursor.getString(5)); Log.d("Database", cursor.getString(5));
            }

            cursor.moveToNext();
        }
        db.close();

        return tempWay;
    }

    public void deleteWaypoint(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WAYPOINTS, KEY_ID + "=" + name, null);
        db.close();
    }
}
