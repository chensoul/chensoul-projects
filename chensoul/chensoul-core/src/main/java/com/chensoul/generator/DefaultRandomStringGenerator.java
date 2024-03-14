package com.chensoul.generator;

import java.util.stream.IntStream;
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
public class DefaultRandomStringGenerator extends AbstractRandomStringGenerator {

    /**
     * The array of printable characters to be used in our random string.
     */
    private static final char[] PRINTABLE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345679".toCharArray();

    /**
     * <p>Constructor for DefaultRandomStringGenerator.</p>
     *
     * @param defaultLength a long
     */
    public DefaultRandomStringGenerator(final long defaultLength) {
        super(defaultLength);
    }

    /**
     * {@inheritDoc}
     *
     * Convert bytes to string, taking into account {@link #PRINTABLE_CHARACTERS}.
     */
    @Override
    protected String convertBytesToString(final byte[] random) {
        char[] output = new char[random.length];
        IntStream.range(0, random.length).forEach(i -> {
            char[] printableCharacters = getPrintableCharacters();
            int index = Math.abs(random[i] % printableCharacters.length);
            output[i] = printableCharacters[index];
        });
        return new String(output);
    }

    /**
     * Get printable characters char [].
     *
     * @return the char []
     */
    protected char[] getPrintableCharacters() {
        return PRINTABLE_CHARACTERS;
    }
}
