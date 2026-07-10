package com.parklight.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Swaps the root of the current scene to another FXML screen.
 * Keeps navigation logic in one place so controllers stay small.
 */
public final class Navigator {

    private Navigator() {
    }

    // Loads the given view (path under resources) and shows it in the same window
    // as the node that triggered the navigation.
    public static void go(Node sourceNode, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = sourceNode.getScene();
        scene.setRoot(root);
    }
}
