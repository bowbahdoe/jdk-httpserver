package dev.mccue.jdk.httpserver;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Helpers for working with an {@link HttpExchange}.
 *
 * <p>
 *     In an ideal world, these would be instance methods on the {@link HttpExchange} class itself.
 *     If that ever happens this library will be obsolete in a good way.
 * </p>
 */
public final class HttpExchangeUtils {
    private HttpExchangeUtils() {
    }

    /**
     * Starts sending the response back to the client using the current set of
     * response headers and the numeric response code as specified in this
     * method. The response body length is also specified as follows. If the
     * response length is known, this specifies an
     * exact number of bytes to send and the application must send that exact
     * amount of data. If the response length is unknown, then
     * chunked transfer encoding is used and an arbitrary amount of data may be
     * sent. The application terminates the response body by closing the
     * {@link OutputStream}.
     *
     * <p> If the content-length response header has not already been set then
     * this is set to the appropriate value depending on the response length
     * parameter.
     *
     * <p> This method must be called prior to calling {@link HttpExchange#getResponseBody()}.
     *
     * @param exchange       the {@link HttpExchange} to use
     * @param rCode          the response code to send
     * @param responseLength a {@link ResponseLength} which specifies the amount of bytes that
     *                       will eventually be written to the response body.
     * @throws IOException   if the response headers have already been sent or an I/O error occurs
     * @see   HttpExchange#getResponseBody()
     */
    public static void sendResponseHeaders(
            HttpExchange exchange, int rCode, ResponseLength responseLength
    ) throws IOException {
        exchange.sendResponseHeaders(
                rCode,
                responseLength.value
        );
    }

    /**
     * Sends a response to the client.
     *
     * <p>
     *     If no {@code Content-Type} header has been specified,
     *     will use the one returned by {@link Body#defaultContentType()}.
     * </p>
     *
     * <p> Calling this will close the {@link InputStream} returned by {@link HttpExchange#getResponseBody()}, which
     * implicitly closes the {@link InputStream} returned from {@link HttpExchange#getRequestBody()} (if it is not already closed).
     *
     * @param exchange       the {@link HttpExchange} to use
     * @param rCode          the response code to send
     * @param body           the {@link Body} to send
     * @throws IOException   if the response headers have already been sent or an I/O error occurs
     */
    public static void sendResponse(
            HttpExchange exchange, int rCode, Body body
    ) throws IOException {
        body.defaultContentType()
                .ifPresent(contentType -> exchange.getResponseHeaders().putIfAbsent(
                        "Content-Type",
                        List.of(contentType)
                ));

        sendResponseHeaders(exchange, rCode, body.responseLength());
        try (var os = exchange.getResponseBody()) {
            body.writeTo(os);
        }
    }
}
