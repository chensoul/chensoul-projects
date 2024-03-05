package com.chensoul.spring.boot.mybatis.encrypt;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DefaultEncryptor implements Encryptor {

    /**
     * @param algorithm
     * @param password
     * @param plaintext
     * @param metaObject
     * @return
     */
    public String encrypt(Algorithm algorithm, String password, String plaintext, Object metaObject) {
        return AlgorithmUtils.resolve(algorithm, password, true, plaintext);
    }

    /**
     * @param algorithm
     * @param password
     * @param plaintext
     * @param metaObject
     * @return
     */
    public String decrypt(Algorithm algorithm, String password, String plaintext, Object metaObject) {
        return AlgorithmUtils.resolve(algorithm, password, false, plaintext);
    }

}
