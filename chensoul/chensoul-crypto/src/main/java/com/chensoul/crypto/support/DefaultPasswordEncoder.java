package com.chensoul.crypto.support;

import com.chensoul.util.BooleanUtils;
import com.chensoul.util.StringUtils;
import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class DefaultPasswordEncoder implements PasswordEncoder {
    private final String encodingAlgorithm;

    private final String characterEncoding;

    @Override
    public String encode(final CharSequence password) {
        if (password == null) {
            return null;
        }

        if (StringUtils.isBlank(this.encodingAlgorithm)) {
            log.warn("No encoding algorithm is defined. Password cannot be encoded; Returning null");
            return null;
        }

        final String encodingCharToUse = StringUtils.isNotBlank(this.characterEncoding)
            ? this.characterEncoding : Charset.defaultCharset().name();

        log.debug("Using [{}] as the character encoding algorithm to update the digest", encodingCharToUse);

        try {
            final byte[] pswBytes = password.toString().getBytes(Charset.forName(encodingCharToUse));
            final String encoded = Hex.encodeHexString(DigestUtils.getDigest(this.encodingAlgorithm).digest(pswBytes));
            log.debug("Encoded password via algorithm [{}] and character-encoding [{}] is [{}]", this.encodingAlgorithm,
                encodingCharToUse, encoded);
            return encoded;
        } catch (final Exception e) {
            log.error("Failed to encode the password via algorithm [{}] and character-encoding [{}]", this.encodingAlgorithm,
                encodingCharToUse);
        }
        return null;
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        String encodedRawPassword = StringUtils.isNotBlank(rawPassword) ? this.encode(rawPassword.toString()) : null;
        Boolean matched = StringUtils.equals(encodedRawPassword, encodedPassword);
        String msg = String.format("Provided password does%smatch the encoded password",
            BooleanUtils.toString(matched, StringUtils.EMPTY, " not "));
        log.debug(msg);
        return matched;
    }
}
