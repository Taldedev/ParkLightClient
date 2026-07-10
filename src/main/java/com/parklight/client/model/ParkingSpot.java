package com.parklight.client.model;

/**
 * Client-side view of a parking spot. Mirrors the server's model for JSON exchange.
 */
public class ParkingSpot {

    private String id;
    private SpotType type;
    private double x;
    private double y;
    private boolean occupied;

    public ParkingSpot() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SpotType getType() {
        return type;
    }

    public void setType(SpotType type) {
        this.type = type;
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

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
