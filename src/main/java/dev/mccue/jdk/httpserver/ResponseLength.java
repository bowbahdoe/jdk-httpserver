package dev.mccue.jdk.httpserver;

/**
 * The length (in bytes)of a response body that will be sent through
 * an {@link com.sun.net.httpserver.HttpExchange}.
 *
 * <p>
 *     This is either a known quantity, in which case the body written must match the number of bytes
 *     specified, or unknown, in which case the response is sent with a chunked encoding.
 * </p>
 *
 * <p>
 *     The need for this arises out of an idiosyncrasy in the {@link com.sun.net.httpserver.HttpExchange#sendResponseHeaders(int, long)}
 *     method where {@code 0} means that the size of a response is unknown and {@code -1} means that there is no response
 *     body to send.
 * </p>
 */
public final class ResponseLength {
    // Note: should expose via case patterns, whenever those land
    final long value;

    private ResponseLength(long value) {
        this.value = value;
    }

    /**
     * Construct a {@link ResponseLength} given a known response length
     * that will be sent. Negative values are treated as if zero were provided.
     *
     * <p>
     *     Note that unlike the {@link com.sun.net.httpserver.HttpExchange#sendResponseHeaders(int, long)}
     *     method, {@code 0} means a length of {@code 0}, not an unknown length.
     * </p>
     *
     * @param responseLength The length of the response body to be sent.
     * @return An instance of {@link ResponseLength}.
     */
    public static ResponseLength known(long responseLength) {
        return new ResponseLength(
                responseLength <= 0 ? -1 : responseLength
        );
    }

    /**
     * Construct a {@link ResponseLength} which represents an unknown number
     * of bytes to be sent in a response.
     *
     * @return An instance of {@link ResponseLength}.
     */
    public static ResponseLength unknown() {
        return new ResponseLength(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ResponseLength other && value == other.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toString() {
        return value == 0
                ? "ResponseLength[UNKNOWN]"
                : "ResponseLength[" + (value == -1 ? 0 : value) + "]";
    }
}