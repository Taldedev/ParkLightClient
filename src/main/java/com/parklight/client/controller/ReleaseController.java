package com.parklight.client.controller;

import com.parklight.client.Navigator;
import com.parklight.client.model.ParkingTicket;
import com.parklight.client.net.Request;
import com.parklight.client.net.Response;
import com.parklight.client.net.ServerClient;
import com.parklight.client.net.Servers;
import com.parklight.client.net.TicketIdBody;

import com.google.gson.reflect.TypeToken;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.lang.reflect.Type;

/**
 * Release/Billing screen: releases a vehicle through the parking server and
 * shows total revenue from the billing server.
 */
public class ReleaseController {

    @FXML
    private TextField ticketField;
    @FXML
    private Label releaseLabel;
    @FXML
    private Label revenueLabel;

    private final ServerClient parking = Servers.parking();
    private final ServerClient billing = Servers.billing();

    @FXML
    private void release() {
        String ticketId = ticketField.getText() == null ? "" : ticketField.getText().trim();
        if (ticketId.isEmpty()) {
            releaseLabel.setText("Please enter a ticket id");
            return;
        }
        try {
            Request<TicketIdBody> request =
                    new Request<>("parking/release", new TicketIdBody(ticketId));
            Type responseType = new TypeToken<Response<ParkingTicket>>() {}.getType();
            Response<ParkingTicket> response = parking.send(request, responseType);

            if (response == null || !response.isSuccess()) {
                releaseLabel.setText(response == null
                        ? "No response from server"
                        : "Failed: " + response.getMessage());
                return;
            }
            ParkingTicket ticket = response.getBody();
            releaseLabel.setText("Released. Price: " + ticket.getPrice());
        } catch (Exception e) {
            releaseLabel.setText("Parking server not reachable: " + e.getMessage());
        }
    }

    @FXML
    private void showRevenue() {
        try {
            Request<Object> request = new Request<>("billing/revenue", null);
            Type responseType = new TypeToken<Response<Double>>() {}.getType();
            Response<Double> response = billing.send(request, responseType);

            if (response == null || !response.isSuccess()) {
                revenueLabel.setText(response == null
                        ? "No response from server"
                        : "Failed: " + response.getMessage());
                return;
            }
            revenueLabel.setText("Total revenue: " + response.getBody());
        } catch (Exception e) {
            revenueLabel.setText("Billing server not reachable: " + e.getMessage());
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            Navigator.go((javafx.scene.Node) event.getSource(),
                    "/com/parklight/client/view/dashboard.fxml");
        } catch (Exception e) {
            releaseLabel.setText("Navigation failed: " + e.getMessage());
        }
    }
}
