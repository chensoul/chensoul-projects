package com.chensoul.util;

/*-
 * #%L
 * Chensoul :: Core
 * %%
 * Copyright (C) 2023 - 2024 chensoul.cc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.SneakyThrows;

/**
 * <p>KeyGeneratorUtils class.</p>
 *
 * @author chensoul
 * @version $Id: $Id
 */
public final class KeyGeneratorUtils {

    private static final String RSA_ALGORITHM = "RSA";

    private KeyGeneratorUtils() {
    }

    /**
     * <p>generateSecretKey.</p>
     *
     * @return a {@link javax.crypto.SecretKey} object
     */
    public static SecretKey generateSecretKey() {
        SecretKey hmacKey;
        try {
            hmacKey = KeyGenerator.getInstance("HmacSha256").generateKey();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return hmacKey;
    }

    /**
     * <p>createPrivateKey.</p>
     *
     * @param privateKeyStr a {@link java.lang.String} object
     * @return a {@link java.security.PrivateKey} object
     */
    @SneakyThrows
    public static PrivateKey createPrivateKey(String privateKeyStr) {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr.replaceAll("\n", ""));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * <p>createPublicKey.</p>
     *
     * @param publicKeyStr a {@link java.lang.String} object
     * @return a {@link java.security.PublicKey} object
     */
    @SneakyThrows
    public static PublicKey createPublicKey(String publicKeyStr) {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr.replaceAll("\n", ""));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * <p>generateRsaKey.</p>
     *
     * @return a {@link java.security.KeyPair} object
     */
    public static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * <p>generateEcKey.</p>
     *
     * @return a {@link java.security.KeyPair} object
     */
    public static KeyPair generateEcKey() {
        EllipticCurve ellipticCurve = new EllipticCurve(
            new ECFieldFp(new BigInteger(
                "115792089210356248762697446949407573530086143415290314195533631308867097853951")),
            new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"),
            new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291"));
        ECPoint ecPoint = new ECPoint(
            new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
            new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109"));
        ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, ecPoint,
            new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"), 1);

        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(ecParameterSpec);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

}
