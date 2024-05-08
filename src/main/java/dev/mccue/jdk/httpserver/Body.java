package dev.mccue.jdk.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * The body of an HTTP Response.
 */
public interface Body {
    ResponseLength responseLength();

    void writeTo(OutputStream outputStream) throws IOException;

    default Optional<String> defaultContentType() {
        return Optional.empty();
    }

    static Body of(String value) {
        return new StringBody(value);
    }

    static Body of(String value, Charset charset) {
        return new StringBody(value, charset);
    }

    static Body of(byte[] value) {
        return new ByteArrayBody(value);
    }

    static Body of(InputStream inputStream) {
        return new InputStreamBody(inputStream);
    }

    static Body of(InputStream inputStream, ResponseLength responseLength) {
        return new InputStreamBody(inputStream, responseLength);
    }

    static Body empty() {
        return EmptyBody.INSTANCE;
    }
}
