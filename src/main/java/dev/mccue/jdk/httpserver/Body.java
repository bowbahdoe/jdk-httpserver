package dev.mccue.jdk.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * The body of an HTTP Response.
 *
 * @see HttpExchangeUtils
 */
public interface Body {
    /**
     * Writes the response body to the provided {@link OutputStream}. The number of bytes written
     * must match what is returned by {@link Body#responseLength()}.
     *
     * <p>
     *     It is not expected that calling this method will close any underlying resources
     *     of the {@link Body}, though an implementation may choose to do so.
     * </p>
     *
     * <p>
     *     This method should not close the provided {@link OutputStream}.
     * </p>
     *
     * @param outputStream The {@link OutputStream} to write the body to.
     * @throws IOException If something goes wrong while writing to the {@link OutputStream}.
     */
    void writeTo(OutputStream outputStream) throws IOException;

    /**
     * @return The length of the response body that will be written when {@link Body#writeTo(OutputStream)}
     * is called.
     */
    default ResponseLength responseLength() {
        return ResponseLength.unknown();
    }

    /**
     * The content type that should be used for this {@link Body}.
     *
     * <p>
     *     It is expected that some types of bodies will have an opinion about
     *     what {@code Content-Type} header should be sent along with them. The value
     *     returned by this method will be used if there is no content type specified by
     *     the user.
     * </p>
     *
     * @return The content type to use by default.
     */
    default Optional<String> defaultContentType() {
        return Optional.empty();
    }

    /**
     * Constructs a body which wraps the given {@link String}.
     *
     * <p>
     *     By default {@code UTF-8} will be used to encode the {@link String}
     *     into the bytes that will be sent by the returned {@link Body}.
     * </p>
     *
     * @param value The {@link String} to send.
     * @return A body which wraps the given {@link String}.
     */
    static Body of(String value) {
        return new StringBody(value);
    }

    /**
     * Constructs a body which wraps the given {@link String}.
     *
     * <p>
     *     The provided {@link Charset} will be used to encode the {@link String}
     *     into the bytes that will be sent by the returned {@link Body}.
     * </p>
     *
     * @param value The {@link String} to send.
     * @return A body which wraps the given {@link String}.
     */
    static Body of(String value, Charset charset) {
        return new StringBody(value, charset);
    }

    /**
     * Creates a {@link Body} wrapping the given {@code byte[]}.
     *
     * <p>
     *     Note that this does not do any defensive copying of the underlying byte array.
     * </p>
     *
     * @param value The {@code byte[]} to send.
     * @return A body which wraps the given {@code byte[]}.
     */
    static Body of(byte[] value) {
        return new ByteArrayBody(value);
    }

    /**
     * Creates a {@link Body} wrapping the given {@code byte[]}.
     *
     * <p>
     *     Note that the {@link InputStream} provided will not be closed when {@link Body#writeTo(OutputStream)}
     *     is called.
     * </p>
     *
     * @param inputStream The {@link InputStream} to send.
     * @return A body which wraps the given {@link InputStream}.
     */
    static Body of(InputStream inputStream) {
        return new InputStreamBody(inputStream);
    }

    /**
     * Creates a {@link Body} wrapping the given {@code byte[]}.
     *
     * <p>
     *     Note that the {@link InputStream} provided will not be closed when {@link Body#writeTo(OutputStream)}
     *     is called.
     * </p>
     *
     * @param inputStream inputStream The {@link InputStream} to send.
     * @param responseLength The expected number of bytes that will be written
     *                       when {@link InputStream#transferTo(OutputStream)} is called.
     * @return A body which wraps the given {@link InputStream}.
     */
    static Body of(InputStream inputStream, ResponseLength responseLength) {
        return new InputStreamBody(inputStream, responseLength);
    }

    /**
     * Creates an empty {@link Body} which will send no bytes to the client.
     *
     * @return An empty body.
     */
    static Body empty() {
        return EmptyBody.INSTANCE;
    }
}
