package com.chensoul.generator;

import java.math.BigInteger;
import lombok.NoArgsConstructor;

/**
 * This is {@link com.chensoul.generator.HexRandomStringGenerator}.
 * Hex encoding implementation of the RandomStringGenerator that allows you to define the
 * length of the random part.
 *
 * @author Timur Duehr
 * @since 5.2.0
 * @version $Id: $Id
 */
@NoArgsConstructor
public class HexRandomStringGenerator extends AbstractRandomStringGenerator {

    /**
     * <p>Constructor for HexRandomStringGenerator.</p>
     *
     * @param defaultLength a int
     */
    public HexRandomStringGenerator(final int defaultLength) {
        super(defaultLength);
    }

    /** {@inheritDoc} */
    @Override
    protected String convertBytesToString(final byte[] random) {
        try {
            return bytesToHex(random);
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * <p>bytesToHex.</p>
     *
     * @param bytes an array of {@link byte} objects
     * @return a {@link java.lang.String} object
     */
    public String bytesToHex(byte[] bytes) {
        BigInteger bigInteger = new BigInteger(1, bytes);
        String hexString = bigInteger.toString(16);
        // 如果十六进制字符串不足两位，前面补零
        while (hexString.length() < bytes.length * 2) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
