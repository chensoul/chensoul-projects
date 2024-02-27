package com.chensoul.websocket.util;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class MicrometerHelper {
    private MicrometerHelper() {
    }

    private static SimpleMeterRegistry registry = new SimpleMeterRegistry();

    private static String total = "websocket.connections.session.total";

    private static String active = "websocket.connections.session.active";

    private static AtomicInteger sessionTotal = new AtomicInteger(0);

    private static AtomicInteger sessionActive = new AtomicInteger(0);

    static {
        Gauge.builder(total, sessionTotal, AtomicInteger::get).register(registry);
        Gauge.builder(active, sessionActive, AtomicInteger::get).register(registry);
    }

    /**
     *
     */
    public static void connectionEstablished() {
        Metrics.gauge(total, sessionTotal).incrementAndGet();
        Metrics.gauge(active, sessionActive).incrementAndGet();
    }

    /**
     *
     */
    public static void connectionClosed() {
        Metrics.gauge(active, sessionActive).decrementAndGet();
    }

}
