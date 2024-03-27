package com.chensoul.generator;

import lombok.NoArgsConstructor;

/**
 * This is {@link com.chensoul.generator.Base64RandomStringGenerator}.
 * <p>
 * URL safe base64 encoding implementation of the RandomStringGenerator that allows you to define the
 * length of the random part.
 *
 * @author chensoul
 *
 */
@NoArgsConstructor
public class Base64RandomStringGenerator extends AbstractRandomStringGenerator {

    /**
     * <p>Constructor for Base64RandomStringGenerator.</p>
     *
     * @param defaultLength a long
     */
    public Base64RandomStringGenerator(final long defaultLength) {
        super(defaultLength);
    }

    /**
     * {@inheritDoc}
     *
     * Converts byte[] to String by Base64 encoding.
     */
    @Override
    protected String convertBytesToString(final byte[] random) {
        return java.util.Base64.getEncoder().encodeToString(random);
    }

}
