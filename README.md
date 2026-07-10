# ParkLightClient

JavaFX desktop client for the ParkLight parking system.

Connects to the ParkLight server over TCP (port 34567) and sends JSON requests
to park vehicles, release them, view spots, and check revenue. This is a
standalone project - it shares no code with the server, only the JSON message
format.

## Tech stack

- Java 21
- JavaFX 21
- Maven
- gson for JSON

## How to run

Start the ParkLight server first (from the server repo), then:

    mvn clean javafx:run

## Structure

- `model/` - client-side copies of the data models (Vehicle, ParkingSpot, ...)
- `net/` - the socket client that talks to the server
- `controller/` - JavaFX controllers (one per screen)
- `resources/.../view/` - FXML layouts
