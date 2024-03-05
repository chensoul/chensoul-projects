package com.chensoul.lang.function;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Apply a computation only once.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public final class OnceConsumer<T> {

    final T t;
    private final AtomicBoolean hasRun = new AtomicBoolean(false);

    private OnceConsumer(final T t) {
        this.t = t;
    }

    /**
     * Create a do once consumer.
     *
     * @param t   input which is passed to operation
     * @param <T> type of input on which operation is applied.
     * @return
     */
    public static <T> OnceConsumer<T> of(final T t) {
        return new OnceConsumer<>(t);
    }

    /**
     * Apply a computation on subject only once.
     * <pre><code>
     * List&lt;String&gt; lst = new ArrayList&lt;&gt;();
     *
     * OnceConsumer&lt;List&lt;String&gt;&gt; once = OnceConsumer.of(lst);
     * once.applyOnce((l) -&gt; l.add("Hello World"));
     * once.applyOnce((l) -&gt; l.add("Hello World"));
     *
     * assertThat(lst).hasSize(1).contains("Hello World");
     *
     * </code></pre>
     *
     * @param consumer computation run once with input t
     */
    public void applyOnce(final Consumer<T> consumer) {
        if (hasRun.compareAndSet(false, true)) {
            consumer.accept(t);
        }
    }
}
