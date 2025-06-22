package com.piltong.modudoc.client.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginRequest {

    @Test
    void testConstructorAndGetters() {
        LoginRequest request = new LoginRequest("testId", "testPassword");

        assertEquals("testId", request.getId());
        assertEquals("testPassword", request.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        LoginRequest request1 = new LoginRequest("user1", "pass1");
        LoginRequest request2 = new LoginRequest("user1", "pass1");
        LoginRequest request3 = new LoginRequest("user2", "pass2");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
    }

    @Test
    void testToStringContainsId() {
        LoginRequest request = new LoginRequest("myId", "myPass");
        String str = request.toString();
        assertNotNull(str);
        assertTrue(str.contains("myId"));
    }
}
