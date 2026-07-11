package com.parklight.client.controller;

import com.parklight.client.Navigator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Welcome screen: shows the app icon and a short description, then lets the
 * user enter the main map screen.
 */
public class WelcomeController {

    @FXML
    private Label statusLabel;

    @FXML
    private void enter(ActionEvent event) {
        try {
            Navigator.go((javafx.scene.Node) event.getSource(),
                    "/com/parklight/client/view/map.fxml");
        } catch (Exception e) {
            if (statusLabel != null) {
                statusLabel.setText("Could not open the map: " + e.getMessage());
            }
        }
    }
}
