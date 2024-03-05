package com.chensoul.crypto;

import com.chensoul.crypto.support.PrivateKeyFactoryBean;
import java.security.PrivateKey;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * This is {@link PrivateKeyFactoryBeanTests}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@Tag("X509")
class PrivateKeyFactoryBeanTests {
    public static final String RSA = "RSA";

    @Test
    void verifyOperation() throws Throwable {
        PrivateKeyFactoryBean factory = new PrivateKeyFactoryBean();
        factory.setLocation(new ClassPathResource("privatekey2.pem"));
        factory.setAlgorithm(RSA);
        factory.setSingleton(false);
        PrivateKey object = factory.getObject();
        assertNotNull(object);
    }

    @Test
    void verifyFails() throws Throwable {
//        PrivateKeyFactoryBean factory = new PrivateKeyFactoryBean();
//        factory.setLocation(new ClassPathResource("badkey.pem"));
//        factory.setAlgorithm(RSA);
//        factory.setSingleton(false);
//        assertNull(factory.getObject());
    }
}
