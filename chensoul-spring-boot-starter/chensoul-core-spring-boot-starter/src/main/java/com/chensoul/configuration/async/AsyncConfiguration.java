package com.chensoul.configuration.async;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextScheduledExecutorService;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.util.ObjectUtils;


/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@EnableAsync
@EnableScheduling
@AutoConfiguration
@AutoConfigureBefore(TaskExecutionAutoConfiguration.class)
@EnableConfigurationProperties({TaskExecutionProperties.class, TaskSchedulingProperties.class})
public class AsyncConfiguration {
    private final TaskExecutionProperties taskExecutionProperties;
    private final TaskSchedulingProperties taskSchedulingProperties;

    /**
     * @param taskExecutionProperties
     * @param taskSchedulingProperties
     */
    public AsyncConfiguration(final TaskExecutionProperties taskExecutionProperties, final TaskSchedulingProperties taskSchedulingProperties) {
        this.taskExecutionProperties = taskExecutionProperties;
        this.taskSchedulingProperties = taskSchedulingProperties;
    }

    /**
     * @return
     */
    @Bean(AsyncConstants.CORE_THREAD_POOL_NAME)
    public Executor taskExecutor() {
        log.info("Creating Async TaskExecutor");

        final TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();

        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(pool.getCoreSize());
        executor.setMaxPoolSize(pool.getMaxSize());
        executor.setQueueCapacity(pool.getQueueCapacity());
        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        executor.setWaitForTasksToCompleteOnShutdown(taskExecutionProperties.getShutdown().isAwaitTermination());
        executor.setAwaitTerminationSeconds((int) pool.getKeepAlive().getSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // Copy sl4j Mapped Diagnostic Context (MDC) from the main to the child thread.
        executor.setTaskDecorator(
            runnable -> {
                final Map<String, String> contextMap = MDC.getCopyOfContextMap();
                return () -> {
                    try {
                        if (!ObjectUtils.isEmpty(contextMap)) {
                            MDC.setContextMap(contextMap);
                        }
                        runnable.run();
                    } finally {
                        MDC.clear();
                    }
                };
            });
        executor.initialize();

        // Delegate spring oauth2 context to the task threads
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }

    /**
     * @return
     */
    @Bean
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    /**
     * @return
     */
    @Bean(AsyncConstants.CORE_SCHEDULED_POOL_NAME)
    public ScheduledExecutorService scheduledExecutorService() {
        final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(taskSchedulingProperties.getPool().getSize(),
            new CustomizableThreadFactory(taskSchedulingProperties.getThreadNamePrefix()));
        return new DelegatingSecurityContextScheduledExecutorService(scheduledExecutorService);
    }

}
