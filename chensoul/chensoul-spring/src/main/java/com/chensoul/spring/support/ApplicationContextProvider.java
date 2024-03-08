package com.chensoul.spring.support;

import com.chensoul.lang.function.FunctionUtils;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * An implementation of {@link ApplicationContextAware} that statically
 * holds the application context.
 *
 * @author Misagh Moayyed
 * @since 3.0.0.
 */
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        APPLICATION_CONTEXT = context;
    }

    /**
     * Process bean injections.
     *
     * @param bean the bean
     */
    public static void processBeanInjections(final Object bean) {
        val ac = getConfigurableApplicationContext();
        if (ac != null) {
            val bpp = new AutowiredAnnotationBeanPostProcessor();
            bpp.setBeanFactory(ac.getAutowireCapableBeanFactory());
            bpp.processInjection(bean);
        }
    }

    /**
     * Hold application context statically.
     *
     * @param ctx the ctx
     */
    public static void holdApplicationContext(final ApplicationContext ctx) {
        APPLICATION_CONTEXT = ctx;
    }

    /**
     * Register bean into application context.
     *
     * @param <T>                the type parameter
     * @param applicationContext the application context
     * @param beanClazz          the bean clazz
     * @param beanId             the bean id
     * @return the type registered
     */
    public static <T> T registerBeanIntoApplicationContext(final ConfigurableApplicationContext applicationContext,
                                                           final Class<T> beanClazz, final String beanId) {
        val beanFactory = applicationContext.getBeanFactory();
        val provider = beanFactory.createBean(beanClazz);
        beanFactory.initializeBean(provider, beanId);
        beanFactory.autowireBean(provider);
        beanFactory.registerSingleton(beanId, provider);
        return provider;
    }

    /**
     * Register bean into application context t.
     *
     * @param <T>                the type parameter
     * @param applicationContext the application context
     * @param beanInstance       the bean instance
     * @param beanId             the bean id
     * @return the t
     */
    public static <T> T registerBeanIntoApplicationContext(final ConfigurableApplicationContext applicationContext,
                                                           final T beanInstance, final String beanId) {
        val beanFactory = applicationContext.getBeanFactory();
        if (beanFactory.containsBean(beanId)) {
            return (T) applicationContext.getBean(beanId, beanInstance.getClass());
        }
        beanFactory.initializeBean(beanInstance, beanId);
        beanFactory.autowireBean(beanInstance);
        beanFactory.registerSingleton(beanId, beanInstance);
        return beanInstance;
    }

    /**
     * Gets configurable application context.
     *
     * @return the configurable application context
     */
    public static ConfigurableApplicationContext getConfigurableApplicationContext() {
        return (ConfigurableApplicationContext) APPLICATION_CONTEXT;
    }

    private static <T> Optional<T> getBean(final String name, final Class<T> clazz) {
        return getBean(APPLICATION_CONTEXT, name, clazz);
    }

    /**
     * Gets bean.
     *
     * @param <T>                the type parameter
     * @param applicationContext the application context
     * @param name               the name
     * @param clazz              the clazz
     * @return the bean
     */
    public static <T> Optional<T> getBean(final ApplicationContext applicationContext, final String name, final Class<T> clazz) {
        return FunctionUtils.tryGet(() -> {
            if (applicationContext != null && applicationContext.containsBean(name)) {
                return Optional.of(applicationContext.getBean(name, clazz));
            }
            return Optional.<T>empty();
        }, e -> Optional.<T>empty()).get();
    }
}
