package dev.mccue.jdk.httpserver;

import java.io.IOException;
import java.io.OutputStream;

record ByteArrayBody(byte[] value) implements Body {
    @Override
    public ResponseLength responseLength() {
        return ResponseLength.known(value.length);
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(value);
    }
}
