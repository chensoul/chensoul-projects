package com.chensoul.configuration.webmvc;

import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncService {

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;


    @Async
    public void testAsyncCall() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        log.info("In testAsyncCall: {}", username);
    }

    public void testScheduledCall() {
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                String username = SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();
                log.info("In testScheduledCall: {}", username);
            }
        }, 1, java.util.concurrent.TimeUnit.SECONDS);
    }
}
