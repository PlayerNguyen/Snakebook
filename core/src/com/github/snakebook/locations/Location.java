package com.github.snakebook.locations;

public class Location {

    public float x;
    public float y;

    public Location(float x, float y) {
        this.x = x;
        this.y = y;
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

    /**
     * Calculates a distance from one point to other point on the location.
     *
     * @param otherLocation another location to calculate.
     * @return a distance from current point to other point.
     */
    public double distance(Location otherLocation) {
        return Math.sqrt(
                Math.pow(this.x - otherLocation.x, 2) + Math.pow(this.y - otherLocation.y, 2)
        );
    }

    public Location add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Location multiply(float f) {
        this.x *= f;
        this.y *= f;
        return this;
    }



    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
