package dev.mccue.jdk.httpserver;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * {@link HttpExchanges}
 */
public final class HttpExchangeUtils {
    private HttpExchangeUtils() {
    }

    /**
     * {@link HttpExchanges#sendResponseHeaders(HttpExchange, int, ResponseLength)}
     */
    public static void sendResponseHeaders(
            HttpExchange exchange, int rCode, ResponseLength responseLength
    ) throws IOException {
        HttpExchanges.sendResponseHeaders(exchange, rCode, responseLength);
    }

    /**
     * {@link HttpExchanges#sendResponse(HttpExchange, int, Body)} (HttpExchange, int, ResponseLength)}
     */
    public static void sendResponse(
            HttpExchange exchange, int rCode, Body body
    ) throws IOException {
        HttpExchanges.sendResponse(exchange, rCode, body);
    }
}
