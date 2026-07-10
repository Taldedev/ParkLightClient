package com.parklight.client.controller;

import com.parklight.client.Navigator;
import com.parklight.client.model.GraphInfo;
import com.parklight.client.net.Request;
import com.parklight.client.net.Response;
import com.parklight.client.net.ServerClient;
import com.parklight.client.net.Servers;

import com.google.gson.reflect.TypeToken;

import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Visual map of the parking lot. Spots are drawn as lots colored by status,
 * the entrance and lane are marked, and edge distances are shown. If a path is
 * set (after parking), the route from entrance to the spot is highlighted.
 */
public class MapController {

    @FXML
    private Pane canvas;
    @FXML
    private Label statusLabel;

    private final ServerClient client = Servers.parking();

    // Optional highlighted route (list of node ids), set when arriving from a park.
    private List<String> highlightPath;

    @FXML
    public void initialize() {
        this.highlightPath = com.parklight.client.AppState.consumeLastPath();
        loadAndDraw();
    }

    // Allows another screen to open the map with a route highlighted.
    public void setHighlightPath(List<String> path) {
        this.highlightPath = path;
        if (canvas != null) {
            loadAndDraw();
        }
    }

    @FXML
    private void loadAndDraw() {
        try {
            Request<Object> request = new Request<>("parking/graph", null);
            Type responseType = new TypeToken<Response<GraphInfo>>() {}.getType();
            Response<GraphInfo> response = client.send(request, responseType);

            if (response == null || !response.isSuccess() || response.getBody() == null) {
                statusLabel.setText("Failed to load map");
                return;
            }
            draw(response.getBody());
            int free = 0;
            int total = 0;
            for (GraphInfo.Node n : response.getBody().getNodes()) {
                if (n.isSpot()) {
                    total++;
                    if (!n.isOccupied()) {
                        free++;
                    }
                }
            }
            statusLabel.setText(free + " of " + total + " spots free");
        } catch (Exception e) {
            statusLabel.setText("Parking server not reachable: " + e.getMessage());
        }
    }

    private void draw(GraphInfo graph) {
        canvas.getChildren().clear();

        Map<String, GraphInfo.Node> byId = new HashMap<>();
        for (GraphInfo.Node n : graph.getNodes()) {
            byId.put(n.getId(), n);
        }

        // Lane edges + distance labels.
        if (graph.getEdges() != null) {
            for (GraphInfo.Edge e : graph.getEdges()) {
                GraphInfo.Node a = byId.get(e.getFrom());
                GraphInfo.Node b = byId.get(e.getTo());
                if (a == null || b == null) {
                    continue;
                }
                Line lane = new Line(a.getX(), a.getY(), b.getX(), b.getY());
                lane.setStroke(Color.web("#cfcfcf"));
                lane.setStrokeWidth(6);
                canvas.getChildren().add(lane);

                Text dist = new Text((a.getX() + b.getX()) / 2 + 6,
                        (a.getY() + b.getY()) / 2 - 6,
                        String.valueOf((int) e.getWeight()));
                dist.setFill(Color.web("#888888"));
                dist.setFont(Font.font(11));
                canvas.getChildren().add(dist);
            }
        }

        // Highlighted route on top of the lanes.
        if (highlightPath != null && highlightPath.size() > 1) {
            for (int i = 0; i < highlightPath.size() - 1; i++) {
                GraphInfo.Node a = byId.get(highlightPath.get(i));
                GraphInfo.Node b = byId.get(highlightPath.get(i + 1));
                if (a == null || b == null) {
                    continue;
                }
                Line route = new Line(a.getX(), a.getY(), b.getX(), b.getY());
                route.setStroke(Color.web("#f6a821"));
                route.setStrokeWidth(5);
                canvas.getChildren().add(route);
            }
        }

        // Nodes: spots as lots, structural nodes as markers.
        for (GraphInfo.Node n : graph.getNodes()) {
            if (n.isSpot()) {
                drawSpot(n);
            } else {
                drawStructural(n);
            }
        }
    }

    // A parking spot drawn as a rounded lot with an id + type label.
    private void drawSpot(GraphInfo.Node n) {
        double w = 72;
        double h = 40;
        Rectangle lot = new Rectangle(n.getX() - w / 2, n.getY() - h / 2, w, h);
        lot.setArcWidth(12);
        lot.setArcHeight(12);
        lot.setStroke(Color.web("#333333"));
        lot.setStrokeWidth(1.5);
        lot.setFill(n.isOccupied() ? Color.web("#e06666") : Color.web("#93c47d"));
        canvas.getChildren().add(lot);

        Text label = new Text(n.getId() + " " + (n.getType() == null ? "" : shortType(n.getType()))
                + "\n" + (n.isOccupied() ? "OCCUPIED" : "FREE"));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextOrigin(VPos.CENTER);
        label.setFont(Font.font("System", FontWeight.BOLD, 9));
        label.setFill(Color.web("#1b1b1b"));
        label.setX(n.getX() - w / 2 + 4);
        label.setY(n.getY());
        canvas.getChildren().add(label);
    }

    // Compact type label: R / E / D.
    private String shortType(String type) {
        if (type == null) {
            return "";
        }
        switch (type) {
            case "REGULAR":  return "R";
            case "ELECTRIC": return "E";
            case "DISABLED": return "D";
            default:         return type;
        }
    }

    // Entrance / aisle drawn as a small marker with a label.
    private void drawStructural(GraphInfo.Node n) {
        double size = 26;
        Rectangle marker = new Rectangle(n.getX() - size / 2, n.getY() - size / 2, size, size);
        marker.setArcWidth(6);
        marker.setArcHeight(6);
        marker.setStroke(Color.web("#333333"));
        marker.setStrokeWidth(1.5);
        marker.setFill("ENTRANCE".equals(n.getId())
                ? Color.web("#6fa8dc") : Color.web("#d9d9d9"));
        canvas.getChildren().add(marker);

        Text label = new Text(n.getX() - size, n.getY() + size,
                "ENTRANCE".equals(n.getId()) ? "ENTRANCE" : "LANE");
        label.setFont(Font.font(11));
        label.setFill(Color.web("#444444"));
        canvas.getChildren().add(label);
    }

    @FXML
    private void goPark(javafx.event.ActionEvent event) {
        navigate(event, "/com/parklight/client/view/park.fxml");
    }

    @FXML
    private void goRelease(javafx.event.ActionEvent event) {
        navigate(event, "/com/parklight/client/view/release.fxml");
    }

    private void navigate(javafx.event.ActionEvent event, String fxmlPath) {
        try {
            com.parklight.client.Navigator.go(
                    (javafx.scene.Node) event.getSource(), fxmlPath);
        } catch (Exception e) {
            statusLabel.setText("Navigation failed: " + e.getMessage());
        }
    }
}
