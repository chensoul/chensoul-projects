package com.chensoul.spring.boot.oauth2.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.SneakyThrows;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class RSAUtil {
    public static final String ALGORITHM_KEY = "RSA";

    private RSAUtil() {
    }

    /**
     * @param privateKeyStr
     * @return
     */
    @SneakyThrows
    public static PrivateKey getPrivateKeyFromString(String privateKeyStr) {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr.replaceAll("\n", ""));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_KEY);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * @param publicKeyStr
     * @return
     */
    @SneakyThrows
    public static PublicKey getPublicKeyFromString(String publicKeyStr) {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr.replaceAll("\n", ""));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_KEY);
        return keyFactory.generatePublic(keySpec);
    }
}
