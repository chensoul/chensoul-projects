package com.chensoul.generator;

import java.math.BigInteger;
import lombok.NoArgsConstructor;

/**
 * This is {@link HexRandomStringGenerator}.
 * Hex encoding implementation of the RandomStringGenerator that allows you to define the
 * length of the random part.
 *
 * @author Timur Duehr
 * @since 5.2.0
 */
@NoArgsConstructor
public class HexRandomStringGenerator extends AbstractRandomStringGenerator {

    public HexRandomStringGenerator(final int defaultLength) {
        super(defaultLength);
    }

    @Override
    protected String convertBytesToString(final byte[] random) {
        try {
            return bytesToHex(random);
        } catch (final Exception e) {
            return null;
        }
    }

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
