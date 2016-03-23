package com.boehmke.robotprototype;

/**
 * Created by Dan Boehmke on 3/23/2016.
 */
public class Waypoint {
    private String name;
    private float x;
    private float y;
    private float heading;

    public Waypoint(String name, float x, float y, float heading) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }
}
