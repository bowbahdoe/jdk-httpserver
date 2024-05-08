package dev.mccue.jdk.httpserver.test;

import dev.mccue.jdk.httpserver.ResponseLength;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResponseLengthTest {
    @Test
    public void unknownResponseLength() {
        assertEquals(ResponseLength.unknown(), ResponseLength.unknown());
        assertNotEquals(ResponseLength.unknown(), ResponseLength.known(0));
        assertNotEquals(ResponseLength.unknown(), ResponseLength.known(-1));
    }

    @Test
    public void knownResponseLength() {
        assertEquals(ResponseLength.known(4), ResponseLength.known(4));
        assertNotEquals(ResponseLength.known(4), ResponseLength.known(3));
        assertEquals(ResponseLength.known(0), ResponseLength.known(-1));
        assertEquals(ResponseLength.known(-1), ResponseLength.known(-2));
    }
}
