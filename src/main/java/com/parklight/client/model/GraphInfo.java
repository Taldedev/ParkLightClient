package com.parklight.client.model;

import java.util.List;

/**
 * Client-side view of the parking-lot graph: nodes (with positions and optional
 * spot info) and weighted edges. Used to draw the map.
 */
public class GraphInfo {

    private List<Node> nodes;
    private List<Edge> edges;

    public GraphInfo() {
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public static class Node {
        private String id;
        private double x;
        private double y;
        private boolean spot;
        private String type;
        private boolean occupied;

        public Node() {
        }

        public String getId() {
            return id;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public boolean isSpot() {
            return spot;
        }

        public String getType() {
            return type;
        }

        public boolean isOccupied() {
            return occupied;
        }
    }

    public static class Edge {
        private String from;
        private String to;
        private double weight;

        public Edge() {
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public double getWeight() {
            return weight;
        }
    }
}
