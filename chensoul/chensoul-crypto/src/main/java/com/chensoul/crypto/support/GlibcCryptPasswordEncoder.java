package com.chensoul.crypto.support;

import com.chensoul.util.gen.HexRandomStringGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This is {@link GlibcCryptPasswordEncoder}.
 *
 * @author Martin Böhmer
 * @since 5.3.10
 */
@Slf4j
@AllArgsConstructor
public class GlibcCryptPasswordEncoder implements PasswordEncoder {
    private static final int SALT_LENGTH = 8;

    private final String encodingAlgorithm;

    private final int strength;

    private String secret;

    @Override
    public String encode(final CharSequence password) {
        if (password == null) {
            return null;
        }

        if (StringUtils.isBlank(this.encodingAlgorithm)) {
            log.warn("No encoding algorithm is defined. Password cannot be encoded; Returning null");
            return null;
        }

        return Crypt.crypt(password.toString(), generateCryptSalt());
    }

    /**
     * Special note on DES UnixCrypt:
     * In DES UnixCrypt, so first two characters of the encoded password are the salt.
     * <p>
     * When you change your password, the {@code /bin/passwd} program selects a salt based on the time of day.
     * The salt is converted into a two-character string and is stored in the {@code /etc/passwd} file along with the
     * encrypted {@code "password."[10]} In this manner, when you type your password at login time, the same salt is used again.
     * UNIX stores the salt as the first two characters of the encrypted password.
     *
     * @param rawPassword     the raw password as it was provided
     * @param encodedPassword the encoded password.
     * @return true/false
     */
    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        if (StringUtils.isBlank(encodedPassword)) {
            log.warn("The encoded password provided for matching is null. Returning false");
            return false;
        }
        String providedSalt;
        int lastDollarIndex = encodedPassword.lastIndexOf('$');
        if (lastDollarIndex == -1) {
            providedSalt = encodedPassword.substring(0, 2);
            log.debug("Assuming DES UnixCrypt as no delimiter could be found in the encoded password provided");
        } else {
            providedSalt = encodedPassword.substring(0, lastDollarIndex);
            log.debug("Encoded password uses algorithm [{}]", providedSalt.charAt(1));
        }
        String encodedRawPassword = Crypt.crypt(rawPassword.toString(), providedSalt);
        Boolean matched = StringUtils.equals(encodedRawPassword, encodedPassword);
        String msg = String.format("Provided password does %smatch the encoded password",
            BooleanUtils.toString(matched, StringUtils.EMPTY, "not "));
        log.debug(msg);
        return matched;
    }

    private String generateCryptSalt() {
        StringBuilder cryptSalt = new StringBuilder();
        if ("1".equals(this.encodingAlgorithm) || "MD5".equalsIgnoreCase(this.encodingAlgorithm)) {
            cryptSalt.append("$1$");
            log.debug("Encoding with MD5 algorithm");
        } else if ("5".equals(this.encodingAlgorithm) || "SHA-256".equalsIgnoreCase(this.encodingAlgorithm)) {
            cryptSalt.append("$5$rounds=").append(this.strength).append('$');
            log.debug("Encoding with SHA-256 algorithm and [{}] rounds", this.strength);
        } else if ("6".equals(this.encodingAlgorithm) || "SHA-512".equalsIgnoreCase(this.encodingAlgorithm)) {
            cryptSalt.append("$6$rounds=").append(this.strength).append('$');
            log.debug("Encoding with SHA-512 algorithm and [{}] rounds", this.strength);
        } else {
            cryptSalt.append(this.encodingAlgorithm);
            log.debug("Encoding with DES UnixCrypt algorithm as no indicator for another algorithm was found.");
        }

        if (StringUtils.isBlank(this.secret)) {
            log.debug("No secret was found. Generating a salt with length [{}]", SALT_LENGTH);
            HexRandomStringGenerator keygen = new HexRandomStringGenerator(SALT_LENGTH);
            this.secret = keygen.getNewString();
        } else {
            log.trace("The provided secret is used as a salt");
        }
        cryptSalt.append(this.secret);
        return cryptSalt.toString();
    }

}
