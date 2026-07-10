package com.parklight.client.net;

/**
 * A response received from the server, parsed from JSON.
 *
 * @param <T> the body type
 */
public class Response<T> {

    private boolean success;
    private String message;
    private T body;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }
}
