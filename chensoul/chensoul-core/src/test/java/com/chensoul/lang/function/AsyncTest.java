package com.chensoul.lang.function;

import java.util.concurrent.CompletionStage;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class AsyncTest {

    @Test
    public void testNoCustomExecutor() {
        CompletionStage<Void> completionStage = Async.runAsync(() -> {
        });
        assertNull(completionStage.toCompletableFuture().join());

        completionStage = Async.supplyAsync(() -> null);
        assertNull(completionStage.toCompletableFuture().join());
    }
}
