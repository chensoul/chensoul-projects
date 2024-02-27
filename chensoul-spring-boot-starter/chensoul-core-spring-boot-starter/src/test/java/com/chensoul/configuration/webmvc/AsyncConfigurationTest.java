package com.chensoul.configuration.webmvc;

import com.chensoul.configuration.async.AsyncConfiguration;
import com.chensoul.configuration.logging.LoggingConfiguration;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "test@user")
@Import({AsyncService.class})
@ImportAutoConfiguration(classes = {
        AsyncConfiguration.class,
        LoggingConfiguration.class,
})
class AsyncConfigurationTest {
    @Autowired
    private AsyncService asyncService;

    @Test
    public void testAsync() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        log.info("Before testAsync: {}", username);

        asyncService.testAsyncCall();
    }

    @Test
    public void testScheduledCall() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        log.info("Before testScheduledCall: {}", username);

        asyncService.testScheduledCall();

        ThreadUtils.sleepQuietly(Duration.ofSeconds(5));
    }

}
