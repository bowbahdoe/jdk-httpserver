package dev.mccue.jdk.httpserver;

import java.io.IOException;
import java.io.OutputStream;

final class EmptyBody implements Body {
    static final EmptyBody INSTANCE = new EmptyBody();

    private EmptyBody() {}

    @Override
    public ResponseLength responseLength() {
        return ResponseLength.known(0);
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
    }

    @Override
    public String toString() {
        return "EmptyBody[]";
    }
}
