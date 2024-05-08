package dev.mccue.jdk.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

final class InputStreamBody implements Body {
    private final InputStream value;
    private final ResponseLength responseLength;

    InputStreamBody(InputStream value) {
        this(value, ResponseLength.unknown());
    }
    InputStreamBody(InputStream value, ResponseLength responseLength) {
        this.value = Objects.requireNonNull(value);
        this.responseLength = Objects.requireNonNull(responseLength);
    }

    @Override
    public ResponseLength responseLength() {
        return responseLength;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        this.value.transferTo(outputStream);
    }

    @Override
    public String toString() {
        return "InputStreamBody[value=" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InputStreamBody i && value.equals(i.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
