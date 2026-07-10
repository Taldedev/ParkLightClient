package com.parklight.client.net;

import java.util.HashMap;
import java.util.Map;

/**
 * A request sent to the server as JSON.
 * headers carries the action, body carries the payload.
 *
 * @param <T> the body type
 */
public class Request<T> {

    private Map<String, String> headers = new HashMap<>();
    private T body;

    public Request(String action, T body) {
        this.headers.put("action", action);
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }
}
