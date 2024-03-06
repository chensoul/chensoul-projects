package com.chensoul.crypto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.chensoul.crypto.support.GlibcCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * This tests {@link GlibcCryptPasswordEncoder}.
 *
 * @author Martin Böhmer
 * @since 5.3.10
 */
@Slf4j
@Tag("PasswordOps")
class GlibcCryptPasswordEncoderTests {

    private static final String PASSWORD_CLEAR = "12345abcDEF!$";

    private static boolean testEncodingRoundtrip(final String algorithm) {
        GlibcCryptPasswordEncoder encoder = new GlibcCryptPasswordEncoder(algorithm, 0, null);

        String passwordHash = encoder.encode(PASSWORD_CLEAR);
        log.debug("Password [{}] was encoded by algorithm [{}] to hash [{}]", PASSWORD_CLEAR, algorithm, passwordHash);

        Boolean match = encoder.matches(PASSWORD_CLEAR, passwordHash);
        log.debug("Does password [{}] match original password [{}]: [{}]", passwordHash, PASSWORD_CLEAR, match);

        return match;
    }

    private static boolean testMatchWithDifferentSalt(final String algorithm, final String encodedPassword) {
        GlibcCryptPasswordEncoder encoder = new GlibcCryptPasswordEncoder(algorithm, 0, null);
        Boolean match = encoder.matches(PASSWORD_CLEAR, encodedPassword);
        log.debug("Does password [{}] match original password [{}]: [{}]", encodedPassword, PASSWORD_CLEAR, match);
        return match;
    }

    @Test
    void sha512EncodingTest() {
        assertTrue(testEncodingRoundtrip("SHA-512"));
        assertTrue(testEncodingRoundtrip("6"));
        assertTrue(testMatchWithDifferentSalt("SHA-512",
            "$6$rounds=1000$df273de606d3609a$GAPcq.K4jO3KkfusCM7Zr8Cci4qf.jOsWj5hkGBpwRg515bKk93afAXHy/lg.2LPr8ZItHoR3AR5X3XOXndZI0"));
    }

    @Test
    void ha256EncodingTest() {
        assertTrue(testEncodingRoundtrip("SHA-256"));
        assertTrue(testEncodingRoundtrip("5"));
        assertTrue(testMatchWithDifferentSalt("SHA-256", "$5$rounds=1000$e98244bb01b64f47$2qphrK8axtGjgmCJFYwaH7czw5iK9feLl7tKjyTlDy0"));
    }

    @Test
    void md5EncodingTest() {
        assertTrue(testEncodingRoundtrip("MD5"));
        assertTrue(testEncodingRoundtrip("1"));
        assertTrue(testMatchWithDifferentSalt("MD5", "$1$c4676fd0$HOHZ2CYp45lZAAQyvF4C21"));
    }

    @Test
    void desUnixCryptEncodingTest() {
        assertTrue(testEncodingRoundtrip("aB"));
        assertTrue(testEncodingRoundtrip("42xyz"));
        assertTrue(testMatchWithDifferentSalt("aB", "aB4fMcNOggJoQ"));
    }

    @Test
    void verifyBadInput() throws Throwable {
        val encoder = new GlibcCryptPasswordEncoder(null, 0, null);
        assertNull(encoder.encode(null));
        assertNull(encoder.encode("password"));
        assertFalse(encoder.matches("rawPassword", null));
    }

    @Test
    void verifyNoSalt() throws Throwable {
        val encoder = new GlibcCryptPasswordEncoder("MD5", 10, "secret");
        assertNotNull(encoder.encode("value"));
    }

}
