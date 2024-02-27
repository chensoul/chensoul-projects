package com.chensoul.groovy.scripting;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.DigestUtils;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface ScriptResourceCacheManager<K extends String, V extends ExecutableScript>
    extends AutoCloseable, DisposableBean {

    /**
     * Compute key.
     *
     * @param keys the keys
     * @return the key
     */
    static String computeKey(final String... keys) {
        String rawKey = String.join(":", keys);
        return DigestUtils.md5DigestAsHex(rawKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Get item.
     *
     * @param key the key
     * @return the item
     */
    V get(K key);

    /**
     * Contains key ?
     *
     * @param key the key
     * @return true/false
     */
    boolean containsKey(K key);

    /**
     * Put script resource cache manager.
     *
     * @param key   the key
     * @param value the value
     * @return the script resource cache manager
     */
    ScriptResourceCacheManager<K, V> put(K key, V value);

    /**
     * Remove script resource cache manager.
     *
     * @param key the key
     * @return the script resource cache manager
     */
    ScriptResourceCacheManager<K, V> remove(K key);

    /**
     * Gets keys.
     *
     * @return the keys
     */
    Set<String> getKeys();

    /**
     * Clear items.
     *
     * @return the groovy script resource cache manager
     */
    @CanIgnoreReturnValue
    default ScriptResourceCacheManager<K, V> clear() {
        close();
        return this;
    }

    @Override
    void close();

    @Override
    default void destroy() {
        close();
    }

    /**
     * Is cache empty?
     *
     * @return true/false
     */
    boolean isEmpty();

    /**
     * Resolve scriptable resource executable.
     *
     * @param scriptResource the script resource
     * @param keys           the keys
     * @return the executable compiled groovy script
     */
    ExecutableScript resolveScriptableResource(String scriptResource, String... keys);

}
