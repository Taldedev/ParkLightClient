package com.parklight.client.controller;

import com.parklight.client.Navigator;
import com.parklight.client.model.ParkingSpot;
import com.parklight.client.net.Request;
import com.parklight.client.net.Response;
import com.parklight.client.net.ServerClient;
import com.parklight.client.net.Servers;

import com.google.gson.reflect.TypeToken;

import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Dashboard screen: shows all parking spots from the parking server.
 */
public class DashboardController {

    @FXML
    private TableView<ParkingSpot> spotsTable;
    @FXML
    private TableColumn<ParkingSpot, String> idColumn;
    @FXML
    private TableColumn<ParkingSpot, String> typeColumn;
    @FXML
    private TableColumn<ParkingSpot, String> statusColumn;
    @FXML
    private Label statusLabel;

    private final ServerClient client = Servers.parking();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getType() == null
                        ? "" : cell.getValue().getType().name()));
        statusColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().isOccupied()
                        ? "Occupied" : "Free"));
        refresh();
    }

    @FXML
    private void refresh() {
        try {
            Request<Object> request = new Request<>("parking/spots", null);
            Type responseType =
                    new TypeToken<Response<List<ParkingSpot>>>() {}.getType();
            Response<List<ParkingSpot>> response = client.send(request, responseType);

            if (response == null || !response.isSuccess()) {
                statusLabel.setText("Failed to load spots");
                return;
            }
            ObservableList<ParkingSpot> data =
                    FXCollections.observableArrayList(response.getBody());
            spotsTable.setItems(data);
            statusLabel.setText("Loaded " + data.size() + " spots");
        } catch (Exception e) {
            statusLabel.setText("Parking server not reachable: " + e.getMessage());
        }
    }

    @FXML
    private void goPark(ActionEvent event) {
        navigate(event, "/com/parklight/client/view/park.fxml");
    }

    @FXML
    private void goRelease(ActionEvent event) {
        navigate(event, "/com/parklight/client/view/release.fxml");
    }

    // Shared navigation helper. Shows the error in the status label if the
    // target screen fails to load.
    private void navigate(ActionEvent event, String fxmlPath) {
        try {
            Navigator.go((javafx.scene.Node) event.getSource(), fxmlPath);
        } catch (Exception e) {
            statusLabel.setText("Navigation failed: " + e.getMessage());
        }
    }
}
