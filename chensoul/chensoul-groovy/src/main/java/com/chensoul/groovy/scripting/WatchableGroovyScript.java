package com.chensoul.groovy.scripting;

import com.chensoul.concurrent.TryReentrantLock;
import com.chensoul.lang.function.CheckedConsumer;
import com.chensoul.lang.function.CheckedSupplier;
import com.chensoul.spring.util.ResourceUtils;
import com.chensoul.spring.util.io.FileWatcherService;
import groovy.lang.GroovyObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Getter
@ToString(of = "resource")
@Accessors(chain = true)
public class WatchableGroovyScript implements ExecutableScript {
    private final TryReentrantLock lock = new TryReentrantLock();

    private final Resource resource;

    private FileWatcherService watcherService;

    private GroovyObject groovyScript;

    @Setter
    private boolean failOnError = true;

    public WatchableGroovyScript(final Resource script, final boolean enableWatcher) {
        this.resource = script;
        if (ResourceUtils.doesResourceExist(script)) {
            if (ResourceUtils.isFile(script) && enableWatcher) {
                watcherService = CheckedSupplier.unchecked(
                    () -> new FileWatcherService(script.getFile(),
                        CheckedConsumer.unchecked(file -> {
                            log.debug("Reloading script at [{}]", file);
                            compileScriptResource(script);
                            log.info("Reloaded script at [{}]", file);
                        }))).get();
                watcherService.start(script.getFilename());
            }
            compileScriptResource(script);
        }
    }

    public WatchableGroovyScript(final Resource script) {
        this(script, true);
    }

    @Override
    public <T> T execute(final Object[] args, final Class<T> clazz) throws Throwable {
        return execute(args, clazz, failOnError);
    }

    @Override
    public void execute(final Object[] args) throws Throwable {
        execute(args, Void.class, failOnError);
    }

    @Override
    public <T> T execute(final String methodName, final Class<T> clazz, final Object... args) throws Throwable {
        return execute(methodName, clazz, failOnError, args);
    }

    @Override
    public <T> T execute(final Object[] args, final Class<T> clazz, final boolean failOnError) throws Throwable {
        return lock.tryLock(() -> {
            try {
                log.trace("Beginning to execute script [{}]", this);
                return groovyScript != null
                    ? ScriptingUtils.executeGroovyScript(this.groovyScript, args, clazz, failOnError)
                    : null;
            } finally {
                log.trace("Completed script execution [{}]", this);
            }
        });
    }

    /**
     * Execute.
     *
     * @param <T>         the type parameter
     * @param methodName  the method name
     * @param clazz       the clazz
     * @param failOnError the fail on error
     * @param args        the args
     * @return the t
     */
    public <T> T execute(final String methodName, final Class<T> clazz, final boolean failOnError,
                         final Object... args) throws Throwable {
        return lock.tryLock(() -> {
            try {
                log.trace("Beginning to execute script [{}]", this);
                return groovyScript != null
                    ? ScriptingUtils.executeGroovyScript(groovyScript, methodName, args, clazz, failOnError)
                    : null;
            } finally {
                log.trace("Completed script execution [{}]", this);
            }
        });
    }

    @Override
    public void close() {
        if (watcherService != null) {
            log.trace("Shutting down watcher service for [{}]", this.resource);
            this.watcherService.close();
        }
    }

    private void compileScriptResource(final Resource script) {
        this.groovyScript = ScriptingUtils.parseGroovyScript(script, failOnError);
    }
}
