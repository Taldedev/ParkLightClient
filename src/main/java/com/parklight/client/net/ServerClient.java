package com.parklight.client.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;

/**
 * Opens a short-lived TCP connection to the ParkLight server for each request.
 * Sends one JSON request line and reads one JSON response line back.
 */
public class ServerClient {

    private final String host;
    private final int port;
    private final Gson gson = new Gson();

    public ServerClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // Sends a request and parses the response into the given type.
    // responseType describes Response<SomeBodyType> for gson.
    public <T> Response<T> send(Request<?> request, Type responseType) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
             Scanner reader = new Scanner(new InputStreamReader(socket.getInputStream()))) {

            writer.println(gson.toJson(request));
            writer.flush();

            if (reader.hasNextLine()) {
                String responseJson = reader.nextLine();
                return gson.fromJson(responseJson, responseType);
            }
            return null;
        }
    }
}
