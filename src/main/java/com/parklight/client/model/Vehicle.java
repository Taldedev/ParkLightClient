package com.parklight.client.model;

/**
 * Client-side view of a vehicle. Mirrors the server's model for JSON exchange.
 */
public class Vehicle {

    private String licensePlate;
    private VehicleType type;

    public Vehicle() {
    }

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
}
