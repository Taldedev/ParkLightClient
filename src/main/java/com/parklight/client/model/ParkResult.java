package com.parklight.client.model;

import java.util.List;

/**
 * Client-side view of a park result: the issued ticket plus the path the
 * algorithm chose from the entrance to the assigned spot.
 */
public class ParkResult {

    private ParkingTicket ticket;
    private List<String> path;

    public ParkResult() {
    }

    public ParkingTicket getTicket() {
        return ticket;
    }

    public void setTicket(ParkingTicket ticket) {
        this.ticket = ticket;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }
}
