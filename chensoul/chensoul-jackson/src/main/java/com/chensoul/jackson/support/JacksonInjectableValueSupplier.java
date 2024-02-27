package com.chensoul.jackson.support;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

public class JacksonInjectableValueSupplier extends InjectableValues.Std {
    private static final long serialVersionUID = -7327438202032303292L;

    public JacksonInjectableValueSupplier(final Supplier<? extends Map<String, Object>> valueSupplier) {
        super(valueSupplier.get());
    }

    @Override
    public Object findInjectableValue(final Object valueId, final DeserializationContext deserializationContext,
                                      final BeanProperty beanProperty, final Object beanInstance) {
        String key = valueId.toString();
        Object valueToReturn = this._values.get(key);

        DirectFieldAccessFallbackBeanWrapper wrapper = new DirectFieldAccessFallbackBeanWrapper(beanInstance);
        if (!this._values.containsKey(key)) {
            return wrapper.getPropertyValue(key);
        }
        Class<?> propType = Objects.requireNonNull(wrapper.getPropertyType(key));

        return valueToReturn;
    }
}
