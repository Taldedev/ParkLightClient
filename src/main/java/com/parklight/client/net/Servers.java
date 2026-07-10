package com.parklight.client.net;

import com.parklight.client.AppConfig;

/**
 * Provides ready-made clients for the parking and billing servers.
 */
public final class Servers {

    private Servers() {
    }

    public static ServerClient parking() {
        return new ServerClient(AppConfig.HOST, AppConfig.PARKING_PORT);
    }

    public static ServerClient billing() {
        return new ServerClient(AppConfig.HOST, AppConfig.BILLING_PORT);
    }
}
