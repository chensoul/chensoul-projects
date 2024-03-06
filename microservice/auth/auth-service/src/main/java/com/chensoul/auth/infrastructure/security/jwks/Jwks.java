package com.chensoul.auth.infrastructure.security.jwks;

import com.chensoul.util.KeyGeneratorUtils;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.PasswordLookup;
import com.nimbusds.jose.jwk.RSAKey;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

public final class Jwks {

    private Jwks() {
    }

    /**
     * https://huongdanjava.com/define-json-web-key-set-for-authorization-server-using-spring-authorization-server-and-pkcs12-key-store-file.html
     *
     * @return {@link JWKSet}
     */
    @SneakyThrows
    public static JWKSet buildJWKSet(Resource keystoreLocation, String storePassword) {
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        try (InputStream fis = keystoreLocation.getInputStream()) {
            keyStore.load(fis, storePassword.toCharArray());
            return JWKSet.load(keyStore, new PasswordLookup() {
                @Override
                public char[] lookupPassword(String name) {
                    return storePassword.toCharArray();
                }
            });
        }
    }

    public static JWKSet buildJWKSet(String rsaPublicKey, String rsaPrivateKey) {
        RSAKey rsaKey = generateRsa(rsaPublicKey, rsaPrivateKey);
        return new JWKSet(rsaKey);
    }

    private static RSAKey generateRsa(String rsaPublicKey, String rsaPrivateKey) {
        RSAPublicKey publicKey = (RSAPublicKey) KeyGeneratorUtils.createPublicKey(rsaPublicKey);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyGeneratorUtils.createPrivateKey(rsaPrivateKey);
        // @formatter:off
		return new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		// @formatter:on
    }

    public static RSAKey generateRsa() {
        KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // @formatter:off
        return new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
        // @formatter:on
    }

    public static ECKey generateEc() {
        KeyPair keyPair = KeyGeneratorUtils.generateEcKey();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        Curve curve = Curve.forECParameterSpec(publicKey.getParams());
        // @formatter:off
		return new ECKey.Builder(curve, publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		// @formatter:on
    }

    public static OctetSequenceKey generateSecret() {
        SecretKey secretKey = KeyGeneratorUtils.generateSecretKey();
        // @formatter:off
		return new OctetSequenceKey.Builder(secretKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		// @formatter:on
    }

}
