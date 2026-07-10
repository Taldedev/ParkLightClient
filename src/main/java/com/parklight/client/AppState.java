package com.parklight.client;

import java.util.List;

/**
 * Small holder for state passed between screens (which cannot be handed directly
 * to a controller when navigation swaps the scene root). Currently carries the
 * most recent parking path so the map can highlight it once.
 */
public final class AppState {

    private static List<String> lastPath;

    private AppState() {
    }

    public static void setLastPath(List<String> path) {
        lastPath = path;
    }

    // Returns the last path and clears it, so it is highlighted only once.
    public static List<String> consumeLastPath() {
        List<String> p = lastPath;
        lastPath = null;
        return p;
    }
}
