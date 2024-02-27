package com.chensoul.groovy.scripting;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.chensoul.spring.support.SpringExpressionLanguageValueResolver;
import com.chensoul.spring.support.bean.Beans;
import com.chensoul.spring.util.ResourceUtils;
import com.chensoul.util.concurrent.TryReentrantLock;
import com.chensoul.util.logging.LoggingUtils;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.Resource;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class GroovyScriptResourceCacheManager implements ScriptResourceCacheManager<String, ExecutableScript> {
    private final TryReentrantLock lock = new TryReentrantLock();

    private final Cache<String, ExecutableScript> cache;

    public GroovyScriptResourceCacheManager(final int initialCapacity, int cacheSize, final String duration) {
        this.cache = Beans.newCacheBuilder(initialCapacity, cacheSize, duration).build();
    }

    @Override
    public ExecutableScript get(final String key) {
        return lock.tryLock(() -> cache.getIfPresent(key));
    }

    @Override
    public boolean containsKey(final String key) {
        return get(key) != null;
    }

    @Override
    @CanIgnoreReturnValue
    public ScriptResourceCacheManager<String, ExecutableScript> put(
        final String key, final ExecutableScript value) {
        return lock.tryLock(() -> {
            this.cache.put(key, value);
            return this;
        });
    }

    @Override
    @CanIgnoreReturnValue
    public ScriptResourceCacheManager<String, ExecutableScript> remove(final String key) {
        return lock.tryLock(() -> {
            this.cache.invalidate(key);
            return this;
        });
    }

    @Override
    public Set<String> getKeys() {
        return lock.tryLock(() -> cache.asMap().keySet());
    }

    @Override
    public void close() {
        lock.tryLock(__ -> cache.invalidateAll());
    }

    @Override
    public boolean isEmpty() {
        return lock.tryLock(() -> cache.asMap().isEmpty());
    }

    @Override
    public ExecutableScript resolveScriptableResource(
        final String scriptResource,
        final String... keys) {

        String cacheKey = ScriptResourceCacheManager.computeKey(keys);
        log.trace("Constructed cache key [{}] for keys [{}] mapped as groovy script", cacheKey, keys);
        ExecutableScript script = null;
        if (containsKey(cacheKey)) {
            script = get(cacheKey);
            log.trace("Located cached groovy script [{}] for key [{}]", script, cacheKey);
        } else {
            try {
                if (ScriptingUtils.isExternalGroovyScript(scriptResource)) {
                    String scriptPath = SpringExpressionLanguageValueResolver.getInstance().resolve(scriptResource);
                    Resource resource = ResourceUtils.getResourceFrom(scriptPath);
                    script = new WatchableGroovyScript(resource);
                } else {
                    String resourceToUse = scriptResource;
                    if (ScriptingUtils.isInlineGroovyScript(resourceToUse)) {
                        val matcher = ScriptingUtils.getMatcherForInlineGroovyScript(resourceToUse);
                        if (matcher.find()) {
                            resourceToUse = matcher.group(1);
                        }
                    }
                    script = new GroovyShellScript(resourceToUse);
                }
                log.trace("Groovy script [{}] for key [{}] is not cached", scriptResource, cacheKey);
                put(cacheKey, script);
                log.trace("Cached groovy script [{}] for key [{}]", script, cacheKey);
            } catch (final Exception e) {
                LoggingUtils.error(log, e);
            }
        }
        return script;
    }
}
