package com.parklight.client;

/**
 * Client-wide settings: where the two servers live.
 */
public final class AppConfig {

    public static final String HOST = "localhost";

    public static final int PARKING_PORT = 34567;
    public static final int BILLING_PORT = 34568;

    private AppConfig() {
    }
}
