package com.facultyeval.unit;

import com.facultyeval.config.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for JwtUtil
 * Tests JWT token generation, validation, and claim extraction.
 */
@DisplayName("JwtUtil Unit Tests")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Base64-encoded 256-bit secret for testing
    private static final String TEST_SECRET = "dGVzdFNlY3JldEtleUZvckZhY3VsdHlFdmFsdWF0aW9uU3lzdGVtMTIzNDU2Nzg=";
    private static final long TEST_EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", TEST_EXPIRATION);
    }

    @Test
    @DisplayName("Should generate a non-null JWT token")
    void testGenerateToken_returnsNonNull() {
        String token = jwtUtil.generateToken("admin", "ADMIN");
        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    @DisplayName("Should have three parts (header.payload.signature)")
    void testGenerateToken_hasThreeParts() {
        String token = jwtUtil.generateToken("student01", "STUDENT");
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT should have exactly 3 parts");
    }

    @Test
    @DisplayName("Should extract correct username from token")
    void testExtractUsername_returnsCorrectUsername() {
        String username = "faculty01";
        String token = jwtUtil.generateToken(username, "FACULTY");
        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    @DisplayName("Should extract correct role from token")
    void testExtractRole_returnsCorrectRole() {
        String token = jwtUtil.generateToken("admin", "ADMIN");
        assertEquals("ADMIN", jwtUtil.extractRole(token));
    }

    @Test
    @DisplayName("Should return true for valid token with matching username")
    void testIsTokenValid_withMatchingUsername_returnsTrue() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username, "STUDENT");
        assertTrue(jwtUtil.isTokenValid(token, username));
    }

    @Test
    @DisplayName("Should return false for valid token with wrong username")
    void testIsTokenValid_withWrongUsername_returnsFalse() {
        String token = jwtUtil.generateToken("user1", "FACULTY");
        assertFalse(jwtUtil.isTokenValid(token, "user2"));
    }

    @Test
    @DisplayName("Should return false for expired token")
    void testIsTokenValid_withExpiredToken_returnsFalse() throws InterruptedException {
        // Set expiration to 1ms (guaranteed to expire after a brief sleep)
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 1L);
        String token = jwtUtil.generateToken("user", "STUDENT");
        // Ensure the token has expired
        Thread.sleep(50);
        assertFalse(jwtUtil.isTokenValid(token, "user"));
    }

    @Test
    @DisplayName("Tokens for different users should be different")
    void testGenerateToken_differentUsersHaveDifferentTokens() {
        String token1 = jwtUtil.generateToken("user1", "STUDENT");
        String token2 = jwtUtil.generateToken("user2", "STUDENT");
        assertNotEquals(token1, token2);
    }
}
