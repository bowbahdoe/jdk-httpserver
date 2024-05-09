# JDK Http Server Utilities

[![javadoc](https://javadoc.io/badge2/dev.mccue/jdk-httpserver/javadoc.svg)](https://javadoc.io/doc/dev.mccue/jdk-httpserver)
[![Tests](https://github.com/bowbahdoe/jdk-httpserver/actions/workflows/test.yml/badge.svg)](https://github.com/bowbahdoe/jdk-httpserver/actions/workflows/test.yml)

Utilities for working with the JDK's [built-in HTTP server](https://docs.oracle.com/en/java/javase/21/docs/api/jdk.httpserver/module-summary.html).

Requires Java 21+

## Dependency Information

### Maven

```xml
<dependency>
    <groupId>dev.mccue</groupId>
    <artifactId>jdk-httpserver</artifactId>
    <version>2024.05.08</version>
</dependency>
```

### Gradle

```
dependencies {
    implementation("dev.mccue:jdk-httpserver:2024.05.08")
}
```

## Usage

```java
import com.sun.net.httpserver.HttpServer;
import dev.mccue.jdk.httpserver.Body;
import dev.mccue.jdk.httpserver.HttpExchangeUtils;

import java.io.IOException;
import java.net.InetSocketAddress;

void main() throws IOException {
    var server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/", exchange -> {
        exchange.getResponseHeaders().put("Content-Type", "text/html");
        HttpExchangeUtils.sendResponse(exchange, 200, Body.of("<h1> Hello, world! </h1>"));
    });
    server.start();
}
```

## Motivation and Explanation

The `jdk.httpserver` module has an unusually janky API.

When responding to a request you are expected to call `sendResponseHeaders`
with both the status code to send and the number of bytes that you will send later.
Then you should call `getResponseBody` to get, write to, and close the `OutputStream`
that returns.

So a typical usage will look like this.

```java
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

void main() throws IOException {
    var server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/", exchange -> {
        exchange.getResponseHeaders().put("Content-Type", "text/html");
        var body = "<h1> Hello, world! </h1>".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, body.length);
        try (var out = exchange.getResponseBody()) {
            out.write(b);
        }
    });
    server.start();
}
```

This has a few flaws. 

### 1. Response Lengths are unintuitive

The first of which is that, for some reason, a response length of `0` indicates that you want the server to send a chunked response.
`-1` is what actually indicates an empty body. This makes many seemingly innocuous usages incorrect.

I.E. the following is incorrect.

```java
exchange.sendResponseHeaders(200, body.length);
```

It should instead be this.

```java
exchange.sendResponseHeaders(200, body.length == 0 ? -1 : body.length);
```

Which is, at the very least, unintuitive.

To combat this, this library provides a dedicated `ResponseLength` type which more explicitly delineates
between when you know how many bytes will be sent vs. when you do not.

```java
HttpExchangeUtils.sendResponseHeaders(200, ResponseLength.known(body.length));
```

```java
HttpExchangeUtils.sendResponseHeaders(200, ResponseLength.unknown());
```

### 2. Writing a response body is error-prone.

Between when someone calls `sendResponseHeaders` and when they write out to the body
provided by `getResponseBody` things can go wrong.

```java
var body = "hello".getBytes(StandardCharsets.UTF_8);
exchange.sendResponseHeaders(200, body.length);

methodThatMightFail();

try (var out = exchange.getResponseBody()) {
    out.write(body);   
}
```

This is troublesome because it means you might have already sent a `200 OK` response header
before encountering a situation you would otherwise want to return a `500` or similar for.

To deal with that gap this library provides an explicit `Body` type. `Body`s wrap up both
the size that ultimately needs to be given to `sendResponseHeaders` and the process for
writing that response out.

```java
HttpExchangeUtils.sendResponse(exchange, 200, Body.of("hello"));
```

`Body`s also, for the convenience of all, have an opinion about what content type they should be sent with.

```java
// A hypothetical JsonBody can suggest that 
// it be sent with a Content-Type: application/json header.
HttpExchangeUtils.sendResponse(exchange, 200, new JsonBody(...));
```

All of this should, I hope, make the API safer to use in practice.