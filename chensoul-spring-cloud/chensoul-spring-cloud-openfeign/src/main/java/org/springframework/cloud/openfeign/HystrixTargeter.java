package org.springframework.cloud.openfeign;

import feign.Feign;
import feign.Target;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;

/**
 * 注入默认的 fallbackFactory
 */
@SuppressWarnings("unchecked")
class HystrixTargeter implements Targeter {

    @Override
    public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,
                        Target.HardCodedTarget<T> target) {
        if (!(feign instanceof HystrixFeign.Builder)) {
            return feign.target(target);
        }
        HystrixFeign.Builder builder = (HystrixFeign.Builder) feign;
        SetterFactory setterFactory = getOptional(factory.getName(), context, SetterFactory.class);
        if (setterFactory != null) {
            builder.setterFactory(setterFactory);
        }
        Class<?> fallback = factory.getFallback();
        if (fallback != void.class) {
            return targetWithFallback(factory.getName(), context, target, builder, fallback);
        }
        Class<?> fallbackFactory = factory.getFallbackFactory();
        if (fallbackFactory != void.class) {
            return targetWithFallbackFactory(factory.getName(), context, target, builder, fallbackFactory);
        }

        HystrixFallbackFactory hystrixFallbackFactory = new HystrixFallbackFactory(target);
        return (T) builder.target(target, hystrixFallbackFactory);
    }

    private <T> T targetWithFallbackFactory(String feignClientName, FeignContext context,
                                            Target.HardCodedTarget<T> target, HystrixFeign.Builder builder, Class<?> fallbackFactoryClass) {
        FallbackFactory<? extends T> fallbackFactory = (FallbackFactory<? extends T>) getFromContext("fallbackFactory",
                feignClientName, context, fallbackFactoryClass, FallbackFactory.class);
        return builder.target(target, fallbackFactory);
    }

    private <T> T targetWithFallback(String feignClientName, FeignContext context, Target.HardCodedTarget<T> target,
                                     HystrixFeign.Builder builder, Class<?> fallback) {
        T fallbackInstance = getFromContext("fallback", feignClientName, context, fallback, target.type());
        return builder.target(target, fallbackInstance);
    }

    private <T> T getFromContext(String fallbackMechanism, String feignClientName, FeignContext context,
                                 Class<?> beanType, Class<T> targetType) {
        Object fallbackInstance = context.getInstance(feignClientName, beanType);
        if (fallbackInstance == null) {
            throw new IllegalStateException(
                    String.format("No " + fallbackMechanism + " instance of type %s found for loadbalance client %s",
                            beanType, feignClientName));
        }

        if (!targetType.isAssignableFrom(beanType)) {
            throw new IllegalStateException(String.format(
                    "Incompatible " + fallbackMechanism + " instance. Fallback/fallbackFactory of "
                    + "type %s is not assignable to %s for loadbalance client %s",
                    beanType, targetType, feignClientName));
        }
        return (T) fallbackInstance;
    }


    private <T> T getOptional(String feignClientName, FeignContext context, Class<T> beanType) {
        return context.getInstance(feignClientName, beanType);
    }

}
