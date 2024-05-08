package dev.mccue.jdk.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

final class StringBody implements Body {
    private final String value;
    private final byte[] bytes;

    StringBody(String value, Charset charset) {
        this.value = Objects.requireNonNull(value);
        this.bytes = value.getBytes(charset);
    }

    StringBody(String value) {
        this(value, StandardCharsets.UTF_8);
    }

    @Override
    public ResponseLength responseLength() {
        return ResponseLength.known(bytes.length);
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(bytes);
    }

    @Override
    public String toString() {
        return "StringBody[value=" + value + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringBody s && value.equals(s.value);
    }
}
