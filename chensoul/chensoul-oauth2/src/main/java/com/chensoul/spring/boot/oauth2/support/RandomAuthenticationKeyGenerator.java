package com.chensoul.spring.boot.oauth2.support;

import com.chensoul.util.RandomStringUtils;
import java.util.Map;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

/**
 *
 */
public class RandomAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {
    /**
     * @param values
     * @return
     */
    @Override
    protected String generateKey(final Map<String, String> values) {
        return super.generateKey(values) + RandomStringUtils.randomAlphabetic(8);
    }
}
