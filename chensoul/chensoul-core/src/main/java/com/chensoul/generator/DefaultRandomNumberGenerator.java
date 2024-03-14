package com.chensoul.generator;

import lombok.NoArgsConstructor;

/**
 * Implementation of the RandomStringGenerator that allows you to define the
 * length of the random part.
 *
 * @author Scott Battaglia
 * @since 3.0.0
 * @version $Id: $Id
 */
@NoArgsConstructor
public class DefaultRandomNumberGenerator extends DefaultRandomStringGenerator {
    private static final char[] PRINTABLE_CHARACTERS = "012345679".toCharArray();

    /**
     * <p>Constructor for DefaultRandomNumberGenerator.</p>
     *
     * @param defaultLength a int
     */
    public DefaultRandomNumberGenerator(final int defaultLength) {
        super(defaultLength);
    }

    /** {@inheritDoc} */
    @Override
    protected char[] getPrintableCharacters() {
        return PRINTABLE_CHARACTERS;
    }
}
