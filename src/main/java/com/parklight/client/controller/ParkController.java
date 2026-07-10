package com.parklight.client.controller;

import com.parklight.client.Navigator;
import com.parklight.client.model.ParkingTicket;
import com.parklight.client.model.Vehicle;
import com.parklight.client.model.VehicleType;
import com.parklight.client.net.Request;
import com.parklight.client.net.Response;
import com.parklight.client.net.ServerClient;
import com.parklight.client.net.Servers;

import com.google.gson.reflect.TypeToken;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.lang.reflect.Type;

/**
 * Park Vehicle screen: sends a park request to the parking server and shows
 * the assigned spot (or an error).
 */
public class ParkController {

    @FXML
    private TextField plateField;
    @FXML
    private ChoiceBox<VehicleType> typeChoice;
    @FXML
    private Label resultLabel;

    private final ServerClient client = Servers.parking();

    @FXML
    public void initialize() {
        typeChoice.getItems().setAll(VehicleType.values());
        typeChoice.getSelectionModel().select(VehicleType.REGULAR);
    }

    @FXML
    private void park() {
        String plate = plateField.getText() == null ? "" : plateField.getText().trim();
        VehicleType type = typeChoice.getValue();

        if (plate.isEmpty()) {
            resultLabel.setText("Please enter a license plate");
            return;
        }
        if (type == null) {
            resultLabel.setText("Please choose a vehicle type");
            return;
        }

        try {
            Vehicle vehicle = new Vehicle(plate, type);
            Request<Vehicle> request = new Request<>("parking/park", vehicle);
            Type responseType = new TypeToken<Response<ParkingTicket>>() {}.getType();
            Response<ParkingTicket> response = client.send(request, responseType);

            if (response == null || !response.isSuccess()) {
                resultLabel.setText(response == null
                        ? "No response from server"
                        : "Failed: " + response.getMessage());
                return;
            }
            ParkingTicket ticket = response.getBody();
            resultLabel.setText("Parked at spot " + ticket.getSpot().getId()
                    + " - ticket " + ticket.getTicketId());
        } catch (Exception e) {
            resultLabel.setText("Parking server not reachable: " + e.getMessage());
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            Navigator.go((javafx.scene.Node) event.getSource(),
                    "/com/parklight/client/view/dashboard.fxml");
        } catch (Exception e) {
            resultLabel.setText("Navigation failed: " + e.getMessage());
        }
    }
}
