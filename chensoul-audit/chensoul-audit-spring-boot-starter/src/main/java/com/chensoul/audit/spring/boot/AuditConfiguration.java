package com.chensoul.audit.spring.boot;

import com.chensoul.audit.spi.ClientInfoResolver;
import com.chensoul.audit.spi.action.DefaultAuditActionResolver;
import com.chensoul.audit.spi.clientinfo.DefaultClientInfoResolver;
import com.chensoul.audit.spi.principal.SpringSecurityPrincipalResolver;
import com.chensoul.audit.spi.resource.ArgumentAsStringResourceResolver;
import com.chensoul.audit.spi.resource.MessageAwareResourceResolver;
import com.chensoul.audit.spi.resource.RequestAsStringResourceResolver;
import com.chensoul.audit.spi.resource.ReturnValueAsStringResourceResolver;
import com.chensoul.audit.support.DefaultAuditManagerRegistry;
import com.chensoul.audit.support.DefaultAuditResolverRegistry;
import com.chensoul.audit.support.FilteredAuditManager;
import com.chensoul.audit.support.GroovyAuditManager;
import com.chensoul.audit.support.Slf4jAuditManager;
import com.chensoul.spring.boot.common.properties.CoreProperties;
import com.chensoul.spring.boot.common.properties.audit.AuditEngineProperties;
import com.chensoul.spring.boot.common.properties.audit.AuditSlf4jProperties;
import com.chensoul.util.function.FunctionUtils;
import com.chensoul.util.text.DefaultMessageSanitizer;
import com.chensoul.util.text.MessageSanitizer;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

/**
 * This is {@link AuditConfiguration}.
 */
@EnableAspectJAutoProxy(proxyTargetClass = false)
@EnableConfigurationProperties(CoreProperties.class)
@AutoConfiguration
@ConditionalOnProperty(name = "chensoul.audit.engine.enabled", havingValue = "true", matchIfMissing = true)
public class AuditConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AuditEventRepository inMemoryAuditEventRepository() {
        return new InMemoryAuditEventRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditAspect auditAspect(
        final ApplicationContext applicationContext,
        final ClientInfoResolver clientInfoResolver,
        final AuditResolverRegistry auditResolverRegistry,
        final AuditManager auditManager,
        final CoreProperties properties) {

        final AuditEngineProperties audit = properties.getAudit().getEngine();
        final AuditAspect aspect = new AuditAspect(
            applicationContext.getApplicationName(),
            Arrays.asList(auditManager),
            auditResolverRegistry.getAuditActionResolvers(),
            auditResolverRegistry.getAuditResourceResolvers(),
            auditResolverRegistry.getAuditPrincipalResolvers());
        aspect.setFailOnFailure(!audit.isFailOnFailure());
        aspect.setEnabled(properties.getAudit().getEngine().isEnabled());
        aspect.setClientInfoResolver(clientInfoResolver);
        return aspect;
    }

    @Bean
    @ConditionalOnMissingBean
    protected AuditManager auditManager(
        final AuditManagerRegistry auditManagerRegistry,
        final CoreProperties properties) {
        final AuditEngineProperties audit = properties.getAudit().getEngine();
        final AuditManager auditManager = new FilteredAuditManager(
            auditManagerRegistry.getAuditManagers(),
            audit.getSupportedActions(), audit.getExcludedActions());
        return auditManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientInfoResolver clientInfoResolver() {
        return new DefaultClientInfoResolver();
    }

    @Configuration
    @AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
    @Slf4j
    public static class AuditResolverRegistryConfiguration {
        @ConditionalOnMissingBean
        @Bean
        public AuditResolverRegistry auditResolverRegistry(
            final List<AuditResolverRegistryConfigurer> configurers) {
            final AuditResolverRegistry registry = new DefaultAuditResolverRegistry();
            configurers.forEach(configurer -> {
                log.trace("Registering audit manager [{}]", configurer.getName());
                configurer.configure(registry);
            });
            return registry;
        }

        @Bean
        @ConditionalOnMissingBean
        public AuditResolverRegistryConfigurer auditResolverRegistryConfigurer(final ApplicationContext applicationContext) {
            return auditResolverRegistry -> {
                final MessageSanitizer messageSanitizer = new DefaultMessageSanitizer();

                final RequestAsStringResourceResolver requestAsStringResourceResolver = new RequestAsStringResourceResolver();
                requestAsStringResourceResolver.setResourcePostProcessor(messageSanitizer::sanitize);

                final ArgumentAsStringResourceResolver argumentAsStringResourceResolver = new ArgumentAsStringResourceResolver();
                argumentAsStringResourceResolver.setResourcePostProcessor(messageSanitizer::sanitize);

                auditResolverRegistry.registerAuditActionResolver(DefaultAuditActionResolver.class.getSimpleName(), new DefaultAuditActionResolver());

                auditResolverRegistry.registerAuditResourceResolver(MessageAwareResourceResolver.class.getSimpleName(), new MessageAwareResourceResolver(applicationContext, argumentAsStringResourceResolver));
                auditResolverRegistry.registerAuditResourceResolver(ArgumentAsStringResourceResolver.class.getSimpleName(), new ArgumentAsStringResourceResolver());
                auditResolverRegistry.registerAuditResourceResolver(RequestAsStringResourceResolver.class.getSimpleName(), requestAsStringResourceResolver);
                auditResolverRegistry.registerAuditResourceResolver(ReturnValueAsStringResourceResolver.class.getSimpleName(), new ReturnValueAsStringResourceResolver());

                auditResolverRegistry.registerAuditPrincipalResolver(SpringSecurityPrincipalResolver.class.getSimpleName(), new SpringSecurityPrincipalResolver());
            };
        }
    }

    @Configuration
    @AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
    @Slf4j
    public static class AuditManagerRegistryConfiguration {
        @ConditionalOnMissingBean
        @Bean
        public AuditManagerRegistry auditManagerRegistry(final List<AuditManagerRegistryConfigurer> configurers) {
            final AuditManagerRegistry auditManagerRegistry = new DefaultAuditManagerRegistry();
            configurers.forEach(cfg -> {
                log.trace("Configuring audit trail execution plan via [{}]", cfg.getName());
                cfg.configure(auditManagerRegistry);
            });
            return auditManagerRegistry;
        }

        @Bean
        @ConditionalOnMissingBean(name = "slf4jAuditManagerRegistryConfigurer")
        @ConditionalOnProperty(name = "chensoul.audit.slf4j.enabled", havingValue = "true", matchIfMissing = true)
        public AuditManagerRegistryConfigurer slf4jAuditManagerRegistryConfigurer(final CoreProperties properties) {
            return auditManagerRegistry -> {
                final AuditSlf4jProperties slf4j = properties.getAudit().getSlf4j();
                final Slf4jAuditManager manager = new Slf4jAuditManager();
                manager.setUseSingleLine(slf4j.isUseSingleLine());
                if (!slf4j.getAuditableFields().isEmpty()) {
                    final List fields = StringUtils.commaDelimitedListToSet(slf4j.getAuditableFields())
                        .stream()
                        .map(field -> AuditManager.AuditableFields.valueOf(field.toUpperCase(Locale.ENGLISH)))
                        .collect(Collectors.toList());
                    manager.setAuditableFields(fields);
                }
                auditManagerRegistry.registerAuditManager(manager);
            };
        }

        @Bean
        @ConditionalOnMissingBean(name = "groovyAuditManagerRegistryConfigurer")
        @ConditionalOnProperty(name = "chensoul.audit.groovy.enabled", havingValue = "true", matchIfMissing = false)
        public AuditManagerRegistryConfigurer groovyAuditManagerRegistryConfigurer(
            final ConfigurableApplicationContext applicationContext,
            final CoreProperties properties) {

            return auditManagerRegistry -> FunctionUtils.doAndHandle(__ -> {
                final File templateFile = properties.getAudit().getGroovy().getTemplate().getLocation().getFile();
                final GroovyAuditManager mgr = new GroovyAuditManager(templateFile, applicationContext);
                auditManagerRegistry.registerAuditManager(mgr);
            });
        }
    }

}
