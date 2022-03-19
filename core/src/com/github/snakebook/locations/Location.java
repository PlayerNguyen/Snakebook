package com.github.snakebook.locations;

public class Location {

    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
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
                Math.pow(otherLocation.x, 2) + Math.pow(otherLocation.y, 2)
        );
    }
    
//    public Vector vectorize() {
//
//    }
}
