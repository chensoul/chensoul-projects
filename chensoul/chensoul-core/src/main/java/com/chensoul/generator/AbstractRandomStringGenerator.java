package com.chensoul.generator;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * This is {@link com.chensoul.generator.AbstractRandomStringGenerator}.
 * <p>
 * Implementation of the RandomStringGenerator that allows you to define the
 * length of the random part.
 *
 * @author Timur Duehr
 * @since 5.2.0
 * @version $Id: $Id
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractRandomStringGenerator implements RandomStringGenerator {
    /**
     * An instance of secure random to ensure randomness is secure.
     */
    protected final SecureRandom randomizer = new SecureRandom();

    /**
     * Default string length before encoding.
     */
    protected final long defaultLength;

    /**
     * Instantiates a new default random string generator
     * with length set to {@link com.chensoul.generator.RandomStringGenerator#DEFAULT_LENGTH}.
     */
    protected AbstractRandomStringGenerator() {
        this(DEFAULT_LENGTH);
    }

    /** {@inheritDoc} */
    @Override
    public String getAlgorithm() {
        return randomizer.getAlgorithm();
    }

    /** {@inheritDoc} */
    @Override
    public String getNewString(final int size) {
        val random = getNewStringAsBytes(size);
        return convertBytesToString(random);
    }

    /** {@inheritDoc} */
    @Override
    public String getNewString() {
        return getNewString(Long.valueOf(getDefaultLength()).intValue());
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getNewStringAsBytes(final int size) {
        val random = new byte[size];
        this.randomizer.nextBytes(random);
        return random;
    }

    /**
     * Converts byte[] to String by simple cast. Subclasses should override.
     *
     * @param random raw bytes
     * @return a converted String
     */
    protected String convertBytesToString(final byte[] random) {
        return new String(random, StandardCharsets.UTF_8);
    }
}
