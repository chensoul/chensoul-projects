package com.chensoul.audit;

import com.chensoul.audit.spi.AuditActionResolver;
import com.chensoul.audit.spi.AuditResourceResolver;
import com.chensoul.audit.spi.PrincipalResolver;
import java.util.Arrays;
import java.util.Map;

/**
 * AuditResolverRegistry is responsible for registering and retrieving AuditResourceResolver instances.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface AuditResolverRegistry {
    /**
     * Register audit resource resolver.
     *
     * @param resolver the resolver
     * @param keys     the key
     */
    default void registerAuditResourceResolver(final AuditResourceResolver resolver, final String... keys) {
        Arrays.stream(keys).forEach(k -> registerAuditResourceResolver(k, resolver));
    }

    /**
     * Register audit resource resolver.
     *
     * @param key      the key
     * @param resolver the resolver
     */
    void registerAuditResourceResolver(String key, AuditResourceResolver resolver);

    /**
     * Register audit principal resolver.
     *
     * @param key      the key
     * @param resolver the resolver
     */
    void registerAuditPrincipalResolver(String key, PrincipalResolver resolver);


    /**
     * Register audit action resolver.
     *
     * @param key      the key
     * @param resolver the resolver
     */
    void registerAuditActionResolver(String key, AuditActionResolver resolver);

    /**
     * Register audit action resolvers.
     *
     * @param resolver the resolver
     * @param keys     the keys
     */
    default void registerAuditActionResolvers(final AuditActionResolver resolver, final String... keys) {
        Arrays.stream(keys).forEach(k -> registerAuditActionResolver(k, resolver));
    }

    /**
     * Register audit action resolvers.
     *
     * @param resolvers the resolvers
     */
    void registerAuditActionResolvers(Map<String, AuditActionResolver> resolvers);

    /**
     * Register audit resource resolvers.
     *
     * @param resolvers the resolvers
     */
    void registerAuditResourceResolvers(Map<String, AuditResourceResolver> resolvers);

    /**
     * Register audit resource resolvers.
     *
     * @param resolver the resolver
     * @param keys     the keys
     */
    default void registerAuditResourceResolvers(final AuditResourceResolver resolver,
                                                final String... keys) {
        Arrays.stream(keys).forEach(k -> registerAuditResourceResolver(k, resolver));
    }

    /**
     * Gets audit resource resolvers.
     *
     * @return the audit resource resolvers
     */
    Map<String, AuditResourceResolver> getAuditResourceResolvers();

    /**
     * Gets audit action resolvers.
     *
     * @return the audit action resolvers
     */
    Map<String, AuditActionResolver> getAuditActionResolvers();

    /**
     * Gets audit principal resolvers.
     *
     * @return the audit action resolvers
     */
    Map<String, PrincipalResolver> getAuditPrincipalResolvers();
}
