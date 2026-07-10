package com.parklight.client.model;

/**
 * Client-side view of a register close-out.
 */
public class CashOut {

    private String id;
    private double amount;
    private String dateTime;
    private int ticketCount;

    public CashOut() {
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getTicketCount() {
        return ticketCount;
    }
}
