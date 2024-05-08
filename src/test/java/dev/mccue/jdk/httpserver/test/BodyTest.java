package dev.mccue.jdk.httpserver.test;

import dev.mccue.jdk.httpserver.Body;
import dev.mccue.jdk.httpserver.ResponseLength;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BodyTest {
    @Test
    public void byteArrayBody() throws IOException {
        var body = Body.of(new byte[] { 1, 2, 3 });

        assertEquals(body.defaultContentType(), Optional.empty());
        assertEquals(body.responseLength(), ResponseLength.known(3));

        var baos = new ByteArrayOutputStream();
        body.writeTo(baos);

        assertArrayEquals(new byte[] { 1, 2, 3 }, baos.toByteArray());
    }

    @Test
    public void stringBody() throws IOException {
        var body = Body.of("hello");

        assertEquals(body.defaultContentType(), Optional.empty());
        assertEquals(body.responseLength(), ResponseLength.known(5));

        var baos = new ByteArrayOutputStream();
        body.writeTo(baos);

        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), baos.toByteArray());
    }

    @Test
    public void inputStreamBody() throws IOException {
        var bais = new ByteArrayInputStream("hello".getBytes(StandardCharsets.UTF_8));
        var body = Body.of(bais);

        assertEquals(body.defaultContentType(), Optional.empty());
        assertEquals(body.responseLength(), ResponseLength.unknown());

        var baos = new ByteArrayOutputStream();
        body.writeTo(baos);

        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), baos.toByteArray());
    }

    @Test
    public void emptyBody() throws IOException {
        var body = Body.empty();

        assertEquals(body.defaultContentType(), Optional.empty());
        assertEquals(body.responseLength(), ResponseLength.known(0));

        var baos = new ByteArrayOutputStream();
        body.writeTo(baos);

        assertArrayEquals(new byte[] {}, baos.toByteArray());
    }
}
