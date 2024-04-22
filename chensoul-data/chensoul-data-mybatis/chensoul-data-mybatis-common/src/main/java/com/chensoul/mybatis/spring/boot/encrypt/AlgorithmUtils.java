package com.chensoul.mybatis.spring.boot.encrypt;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Function;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class AlgorithmUtils {

    static final String PREFIX = "{@##@}";
    static char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     *
     */
    private AlgorithmUtils() {
    }

    /**
     * @param algorithmEnum
     * @param plaintext
     * @return
     */
    public static String resolve(Algorithm algorithmEnum, String plaintext) {
        return resolve(algorithmEnum, null, true, plaintext);
    }

    /**
     * @param algorithmEnum
     * @param password
     * @param isEncrypt
     * @param plaintext
     * @return
     */
    public static String resolve(Algorithm algorithmEnum, String password, boolean isEncrypt, String plaintext) {
        if (algorithmEnum == Algorithm.MD5_32) {
            return isEncrypt ? encrypt(plaintext, s -> md5With32(s)) : plaintext;
        } else if (algorithmEnum == Algorithm.MD5_16) {
            return isEncrypt ? encrypt(plaintext, s -> md5With16(s)) : plaintext;
        } else if (algorithmEnum == Algorithm.AES) {
            return isEncrypt ? encrypt(plaintext, s -> AES.encrypt(s, password))
                    : decrypt(plaintext, s -> AES.decrypt(s, password));
        } else {
            return isEncrypt ? encrypt(plaintext, s -> base64Encode(s)) : decrypt(plaintext, s -> base64Decode(s));
        }
    }

    /**
     * @param plaintext
     * @param function
     * @return
     */
    private static String encrypt(String plaintext, Function<String, String> function) {
        if (StringUtils.isBlank(plaintext)) {
            return null;
        }
        return !plaintext.startsWith(PREFIX) ? PREFIX + function.apply(plaintext) : plaintext;
    }

    /**
     * @param plaintext
     * @param function
     * @return
     */
    private static String decrypt(String plaintext, Function<String, String> function) {
        if (StringUtils.isBlank(plaintext)) {
            return null;
        }
        return plaintext.startsWith(PREFIX) ? function.apply(plaintext.substring(PREFIX.length())) : plaintext;
    }

    /**
     * @param data
     * @return
     */
    private static String md5With32(String data) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不存在");
        }

        messageDigest.update(data.getBytes());
        byte[] digest = messageDigest.digest();

        StringBuilder stringBuilder = new StringBuilder(digest.length * 2);
        for (int var2 = 0; var2 < digest.length; ++var2) {
            stringBuilder.append(chars[(digest[var2] & 240) >>> 4]);
            stringBuilder.append(chars[digest[var2] & 15]);
        }
        return stringBuilder.toString();
    }

    /**
     * @param data
     * @return
     */
    private static String md5With16(String data) {
        return md5With32(data).substring(8, 24);
    }

    /**
     * @param data
     * @return
     */
    public static String base64Encode(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param data
     * @return
     */
    public static String base64Decode(String data) {
        return new String(Base64.getDecoder().decode(data));
    }

}
