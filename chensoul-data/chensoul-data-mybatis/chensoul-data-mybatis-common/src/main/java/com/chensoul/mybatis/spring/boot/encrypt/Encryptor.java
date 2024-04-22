package com.chensoul.mybatis.spring.boot.encrypt;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface Encryptor {

    /**
     * @param algorithm
     * @param password
     * @param plaintext
     * @param metaObject
     * @return
     */
    String encrypt(Algorithm algorithm, String password, String plaintext, Object metaObject);

    /**
     * @param algorithm
     * @param password
     * @param plaintext
     * @param metaObject
     * @return
     */
    String decrypt(Algorithm algorithm, String password, String plaintext, Object metaObject);

}
