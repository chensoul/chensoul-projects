package com.chensoul.spring.boot.web.startup;

import org.apache.commons.lang3.ArrayUtils;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * This is {@link ApplicationEntrypointInitializerTests}.
 *
 * @author Misagh Moayyed
 * @since 7.0.0
 */
@Tag("Simple")
public class ApplicationEntrypointInitializerTests {
    @Test
    void verifyOperation() throws Throwable {
        ApplicationEntrypointInitializer.noOp().initialize(ArrayUtils.EMPTY_STRING_ARRAY);
        assertTrue(ApplicationEntrypointInitializer.noOp().getApplicationSources(ArrayUtils.EMPTY_STRING_ARRAY).isEmpty());
    }
}
