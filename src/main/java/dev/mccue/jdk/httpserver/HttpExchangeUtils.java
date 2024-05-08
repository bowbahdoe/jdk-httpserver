package dev.mccue.jdk.httpserver;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public final class HttpExchangeUtils {
    private HttpExchangeUtils() {
    }

    public static void sendResponseHeaders(
            HttpExchange exchange, int rCode, ResponseLength responseLength
    ) throws IOException {
        exchange.sendResponseHeaders(
                rCode,
                responseLength.value
        );
    }

    public static void sendResponse(
            HttpExchange exchange, int rCode, Body body
    ) throws IOException {
        var headers = exchange.getResponseHeaders();

        body.defaultContentType()
                .ifPresent(contentType -> headers.putIfAbsent(
                        "Content-Type",
                        List.of(contentType)
                ));

        sendResponseHeaders(exchange, rCode, body.responseLength());
        try (var os = exchange.getResponseBody()) {
            body.writeTo(os);
        }
    }
}
