package com.chensoul.util.gen;

import lombok.NoArgsConstructor;

/**
 * This is {@link Base64RandomStringGenerator}.
 * <p>
 * URL safe base64 encoding implementation of the RandomStringGenerator that allows you to define the
 * length of the random part.
 */
@NoArgsConstructor
public class Base64RandomStringGenerator extends AbstractRandomStringGenerator {

    public Base64RandomStringGenerator(final long defaultLength) {
        super(defaultLength);
    }

    /**
     * Converts byte[] to String by Base64 encoding.
     *
     * @param random raw bytes
     * @return a converted String
     */
    @Override
    protected String convertBytesToString(final byte[] random) {
        return java.util.Base64.getEncoder().encodeToString(random);
    }

}
