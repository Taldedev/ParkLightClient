package com.parklight.client.controller;

import com.parklight.client.Navigator;
import com.parklight.client.model.ParkingTicket;
import com.parklight.client.net.Request;
import com.parklight.client.net.Response;
import com.parklight.client.net.ServerClient;
import com.parklight.client.net.Servers;
import com.parklight.client.net.TicketIdBody;

import com.google.gson.reflect.TypeToken;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Release/Billing screen: lists currently parked vehicles from the parking
 * server, releases the selected one, and shows total revenue from billing.
 */
public class ReleaseController {

    @FXML
    private TableView<ParkingTicket> activeTable;
    @FXML
    private TableColumn<ParkingTicket, String> plateColumn;
    @FXML
    private TableColumn<ParkingTicket, String> spotColumn;
    @FXML
    private TableColumn<ParkingTicket, String> ticketColumn;
    @FXML
    private Label releaseLabel;
    @FXML
    private Label revenueLabel;

    private final ServerClient parking = Servers.parking();
    private final ServerClient billing = Servers.billing();

    @FXML
    public void initialize() {
        plateColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getVehicle() == null
                        ? "" : cell.getValue().getVehicle().getLicensePlate()));
        spotColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getSpot() == null
                        ? "" : cell.getValue().getSpot().getId()));
        ticketColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getTicketId()));
        loadActive();
    }

    // Loads the list of currently parked vehicles into the table.
    @FXML
    private void loadActive() {
        try {
            Request<Object> request = new Request<>("parking/active", null);
            Type responseType =
                    new TypeToken<Response<List<ParkingTicket>>>() {}.getType();
            Response<List<ParkingTicket>> response = parking.send(request, responseType);

            if (response == null || !response.isSuccess()) {
                releaseLabel.setText("Failed to load parked vehicles");
                return;
            }
            ObservableList<ParkingTicket> data =
                    FXCollections.observableArrayList(response.getBody());
            activeTable.setItems(data);
            releaseLabel.setText(data.size() + " vehicle(s) currently parked");
        } catch (Exception e) {
            releaseLabel.setText("Parking server not reachable: " + e.getMessage());
        }
    }

    @FXML
    private void release() {
        ParkingTicket selected = activeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            releaseLabel.setText("Please select a vehicle to release");
            return;
        }
        try {
            Request<TicketIdBody> request = new Request<>(
                    "parking/release", new TicketIdBody(selected.getTicketId()));
            Type responseType = new TypeToken<Response<ParkingTicket>>() {}.getType();
            Response<ParkingTicket> response = parking.send(request, responseType);

            if (response == null || !response.isSuccess()) {
                releaseLabel.setText(response == null
                        ? "No response from server"
                        : "Failed: " + response.getMessage());
                return;
            }
            ParkingTicket ticket = response.getBody();
            releaseLabel.setText("Released " + ticket.getVehicle().getLicensePlate()
                    + " - price " + ticket.getPrice());
            loadActive(); // refresh the table so the released car disappears
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
