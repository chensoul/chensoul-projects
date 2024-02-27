package com.chensoul.crypto;

import static com.chensoul.crypto.PrivateKeyFactoryBeanTests.RSA;
import com.chensoul.crypto.support.PublicKeyFactoryBean;
import lombok.val;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * This is {@link PublicKeyFactoryBeanTests}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@Tag("X509")
class PublicKeyFactoryBeanTests {

    @Test
    void verifyOperation() throws Throwable {
        PublicKeyFactoryBean factory = new PublicKeyFactoryBean(new ClassPathResource("publickey.pem"), RSA);
        factory.setSingleton(false);
        val object = factory.getObject();
        assertNotNull(object);
    }

    @Test
    void verifyFails() throws Throwable {
        PublicKeyFactoryBean factory = new PublicKeyFactoryBean(new ClassPathResource("badkey.pem"), RSA);
        factory.setSingleton(false);
        assertNull(factory.toCipher());
    }
}
