package com.parklight.client.controller;

import com.parklight.client.Navigator;
import com.parklight.client.model.CashOut;
import com.parklight.client.net.Request;
import com.parklight.client.net.Response;
import com.parklight.client.net.ServerClient;
import com.parklight.client.net.Servers;

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
 * Shows the history of register close-outs (cash-outs) in a table.
 */
public class CashOutHistoryController {

    @FXML
    private TableView<CashOut> historyTable;
    @FXML
    private TableColumn<CashOut, String> dateColumn;
    @FXML
    private TableColumn<CashOut, String> amountColumn;
    @FXML
    private TableColumn<CashOut, String> countColumn;
    @FXML
    private Label statusLabel;

    private final ServerClient billing = Servers.billing();

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDateTime()));
        amountColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getAmount())));
        countColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getTicketCount())));
        loadHistory();
    }

    @FXML
    private void loadHistory() {
        try {
            Request<Object> request = new Request<>("billing/cashouts", null);
            Type responseType = new TypeToken<Response<List<CashOut>>>() {}.getType();
            Response<List<CashOut>> response = billing.send(request, responseType);

            if (response == null || !response.isSuccess()) {
                statusLabel.setText("Failed to load history");
                return;
            }
            List<CashOut> body = response.getBody();
            ObservableList<CashOut> data = FXCollections.observableArrayList(body);
            historyTable.setItems(data);

            double sum = 0;
            for (CashOut c : body) {
                sum += c.getAmount();
            }
            statusLabel.setText(data.size() + " close-out(s), total collected all-time: " + sum);
        } catch (Exception e) {
            statusLabel.setText("Billing server not reachable: " + e.getMessage());
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            Navigator.go((javafx.scene.Node) event.getSource(),
                    "/com/parklight/client/view/release.fxml");
        } catch (Exception e) {
            statusLabel.setText("Navigation failed: " + e.getMessage());
        }
    }
}
