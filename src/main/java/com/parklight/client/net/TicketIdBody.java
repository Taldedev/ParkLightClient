package com.parklight.client.net;

/**
 * Request body carrying just a ticket id (used by release and ticket lookups).
 */
public class TicketIdBody {

    private String ticketId;

    public TicketIdBody(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }
}
