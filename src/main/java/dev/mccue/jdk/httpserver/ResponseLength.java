package dev.mccue.jdk.httpserver;


public final class ResponseLength {
    final long value;

    private ResponseLength(long value) {
        this.value = value;
    }

    public static ResponseLength known(long responseLength) {
        responseLength = Math.max(0, responseLength);
        return new ResponseLength(
                responseLength == 0 ? -1 : responseLength
        );
    }

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
                ? "ResponseLength[value=UNKNOWN]"
                : "ResponseLength[value=" + (value == -1 ? 0 : value) + "]";
    }
}