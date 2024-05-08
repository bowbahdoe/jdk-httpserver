package dev.mccue.jdk.httpserver.test;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import dev.mccue.jdk.httpserver.Body;
import dev.mccue.jdk.httpserver.HttpExchangeUtils;
import dev.mccue.jdk.httpserver.ResponseLength;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpExchangeUtilsTest {
    @Test
    public void testSendResponseHeaders() throws IOException {
        record Call(int rCode, long responseLength) {}
        var calls = new ArrayList<Call>();
        var exchange = new HttpExchange() {
            @Override
            public Headers getRequestHeaders() {
                return null;
            }

            @Override
            public Headers getResponseHeaders() {
                return new Headers();
            }

            @Override
            public URI getRequestURI() {
                return null;
            }

            @Override
            public String getRequestMethod() {
                return null;
            }

            @Override
            public HttpContext getHttpContext() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getRequestBody() {
                return null;
            }

            @Override
            public OutputStream getResponseBody() {
                return null;
            }

            @Override
            public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
                calls.add(new Call(rCode, responseLength));
            }

            @Override
            public InetSocketAddress getRemoteAddress() {
                return null;
            }

            @Override
            public int getResponseCode() {
                return 0;
            }

            @Override
            public InetSocketAddress getLocalAddress() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public Object getAttribute(String name) {
                return null;
            }

            @Override
            public void setAttribute(String name, Object value) {

            }

            @Override
            public void setStreams(InputStream i, OutputStream o) {

            }

            @Override
            public HttpPrincipal getPrincipal() {
                return null;
            }
        };

        HttpExchangeUtils.sendResponseHeaders(exchange, 111, ResponseLength.known(0));
        HttpExchangeUtils.sendResponseHeaders(exchange, 222, ResponseLength.known(-1));
        HttpExchangeUtils.sendResponseHeaders(exchange, 333, ResponseLength.known(4));
        HttpExchangeUtils.sendResponseHeaders(exchange, 444, ResponseLength.unknown());

        assertEquals(calls, List.of(
                new Call(111, -1),
                new Call(222, -1),
                new Call(333, 4),
                new Call(444, 0)
        ));
    }

    @Test
    public void sendResponseTest() throws IOException {
        var baos = new ByteArrayOutputStream();
        var exchange = new HttpExchange() {
            @Override
            public Headers getRequestHeaders() {
                return null;
            }

            @Override
            public Headers getResponseHeaders() {
                return new Headers();
            }

            @Override
            public URI getRequestURI() {
                return null;
            }

            @Override
            public String getRequestMethod() {
                return null;
            }

            @Override
            public HttpContext getHttpContext() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getRequestBody() {
                return null;
            }

            @Override
            public OutputStream getResponseBody() {
                return baos;
            }

            @Override
            public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
            }

            @Override
            public InetSocketAddress getRemoteAddress() {
                return null;
            }

            @Override
            public int getResponseCode() {
                return 0;
            }

            @Override
            public InetSocketAddress getLocalAddress() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public Object getAttribute(String name) {
                return null;
            }

            @Override
            public void setAttribute(String name, Object value) {

            }

            @Override
            public void setStreams(InputStream i, OutputStream o) {

            }

            @Override
            public HttpPrincipal getPrincipal() {
                return null;
            }
        };

        HttpExchangeUtils.sendResponse(exchange, 200, Body.of("Hello"));
        assertArrayEquals("Hello".getBytes(StandardCharsets.UTF_8), baos.toByteArray());
    }
}
