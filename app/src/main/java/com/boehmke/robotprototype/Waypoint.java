package com.boehmke.robotprototype;

/**
 * Created by Dan Boehmke on 3/23/2016.
 *
 * Waypoint used to guide robot.
 */
public class Waypoint implements java.io.Serializable {
    private long id;
    private String name;
    private float x;
    private float y;
    private float heading;
    private boolean isOffice;

    public Waypoint() {
        this.name = "Dan";
        this.x = 0;
        this.y = 0;
        this.heading = 0;
        this.isOffice = false;
    }

    public Waypoint(String name, float x, float y, float heading, boolean office) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.isOffice = office;
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

    public boolean isOffice() {
        return isOffice;
    }

    public void setOffice(boolean office) {
        isOffice = office;
    }

    public void setOfficeString(String office) {
        isOffice = office.equals("true");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
