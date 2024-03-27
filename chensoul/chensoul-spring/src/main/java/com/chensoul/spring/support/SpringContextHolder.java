package com.chensoul.spring.support;

import com.chensoul.exception.BusinessException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@Lazy(false)
@SuppressWarnings("all")
public class SpringContextHolder implements BeanFactoryPostProcessor, ApplicationContextAware {
    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext applicationContext;

    /**
     *
     */
    private SpringContextHolder() {
    }

    /**
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param applicationContext the ApplicationContext object to be used by this object
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * @return
     */
    public static ConfigurableListableBeanFactory getConfigurableBeanFactory() {
        ConfigurableListableBeanFactory factory;
        if (null != beanFactory) {
            factory = beanFactory;
        } else {
            if (!(applicationContext instanceof ConfigurableApplicationContext)) {
                throw new BusinessException("No ConfigurableListableBeanFactory from context!");
            }

            factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        }

        return factory;
    }

    /**
     * @return
     */
    public static AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return applicationContext.getAutowireCapableBeanFactory();
    }

    /**
     * @return
     */
    public static BeanFactory getParentBeanFactory() {
        return applicationContext.getParentBeanFactory();
    }

    /**
     * @param name
     * @return
     */
    public static boolean containsLocalBean(String name) {
        return applicationContext.containsLocalBean(name);
    }

    /**
     * @param beanName
     * @return
     */
    public static boolean containsBeanDefinition(String beanName) {
        return applicationContext.containsLocalBean(beanName);
    }

    /**
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key);
    }

    /**
     * @return
     */
    public static String getApplicationName() {
        return getProperty("spring.application.name");
    }

    /**
     * @return
     */
    public static String[] getActiveProfiles() {
        return null == applicationContext ? null : applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * @return
     */
    public static String getActiveProfile() {
        String[] activeProfiles = getActiveProfiles();
        return ArrayUtils.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
    }

    /**
     * @return
     */
    public static int getBeanDefinitionCount() {
        return applicationContext.getBeanDefinitionCount();
    }

    /**
     * @return
     */
    public static String[] getBeanDefinitionNames() {
        return applicationContext.getBeanDefinitionNames();
    }

    /**
     * @param type
     * @return
     */
    public static String[] getBeanNamesForType(ResolvableType type) {
        return applicationContext.getBeanNamesForType(type);
    }

    /**
     * @param type
     * @return
     */
    public static String[] getBeanNamesForType(Class<?> type) {
        return applicationContext.getBeanNamesForType(type);
    }

    /**
     * @param type
     * @param includeNonSingletons
     * @param allowEagerInit
     * @return
     */
    public static String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return applicationContext.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }

    /**
     * @param type
     * @param <T>
     * @return
     *
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return applicationContext.getBeansOfType(type);
    }

    /**
     * @param type
     * @param includeNonSingletons
     * @param allowEagerInit
     * @param <T>
     * @return
     *
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
        throws BeansException {
        return applicationContext.getBeansOfType(type, includeNonSingletons, allowEagerInit);
    }

    /**
     * @param annotationType
     * @return
     */
    public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return applicationContext.getBeanNamesForAnnotation(annotationType);
    }

    /**
     * @param annotationType
     * @return
     *
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
        throws BeansException {
        return applicationContext.getBeansWithAnnotation(annotationType);
    }

    /**
     * @param beanName
     * @param annotationType
     * @param <A>
     * @return
     *
     */
    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
        throws NoSuchBeanDefinitionException {
        return applicationContext.findAnnotationOnBean(beanName, annotationType);
    }

    /**
     * @param name
     * @param <T>
     * @return
     *
     */
    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    /**
     * @param name
     * @param requiredType
     * @param <T>
     * @return
     *
     */
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * @param name
     * @param args
     * @param <T>
     * @return
     *
     */
    public static <T> T getBean(String name, Object... args) throws BeansException {
        return (T) applicationContext.getBean(name, args);
    }

    /**
     * @param requiredType
     * @param <T>
     * @return
     *
     */
    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    /**
     * @param requiredType
     * @param consumer
     * @param <T>
     *
     */
    public static <T> void getBeanIfExist(Class<T> requiredType, Consumer<T> consumer) throws BeansException {
        try {
            T bean = applicationContext.getBean(requiredType);
            consumer.accept(bean);
        } catch (NoSuchBeanDefinitionException ignore) {
            // ignore
        }
    }

    /**
     * @param requiredType
     * @param args
     * @param <T>
     * @return
     *
     */
    public static <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return applicationContext.getBean(requiredType, args);
    }

    /**
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
        return applicationContext.getBeanProvider(requiredType);
    }

    /**
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
        return applicationContext.getBeanProvider(requiredType);
    }

    /**
     * @param name
     * @return
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * @param name
     * @return
     *
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * @param name
     * @return
     *
     */
    public static boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isPrototype(name);
    }

    /**
     * @param name
     * @param typeToMatch
     * @return
     *
     */
    public static boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return applicationContext.isTypeMatch(name, typeToMatch);
    }

    /**
     * @param name
     * @param typeToMatch
     * @return
     *
     */
    public static boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return applicationContext.isTypeMatch(name, typeToMatch);
    }

    /**
     * @param name
     * @return
     *
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    /**
     * @param name
     * @return
     */
    public static String[] getAliases(String name) {
        return applicationContext.getAliases(name);
    }

    /**
     * @param event
     */
    public static void publishEvent(Object event) {
        applicationContext.publishEvent(event);
    }

    /**
     * @param event
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    /**
     * @param code
     * @param args
     * @param defaultMessage
     * @param locale
     * @return
     */
    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return applicationContext.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * @param code
     * @param args
     * @param locale
     * @return
     */
    public static String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return applicationContext.getMessage(code, args, locale);
    }

    /**
     * @param resolvable
     * @param locale
     * @return
     */
    public static String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return applicationContext.getMessage(resolvable, locale);
    }

    /**
     * @param locationPattern
     * @return
     */
    public static Resource[] getResources(String locationPattern) throws IOException {
        return applicationContext.getResources(locationPattern);
    }

    /**
     * @param location
     * @return
     */
    public static Resource getResource(String location) {
        return applicationContext.getResource(location);
    }

    /**
     * @return
     */
    public static ClassLoader getClassLoader() {
        return applicationContext.getClassLoader();
    }

    /**
     * @param beanFactory the bean factory used by the application context
     *
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringContextHolder.beanFactory = beanFactory;
    }

}
