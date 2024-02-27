package com.chensoul.jackson.serializer.sanitizer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.chensoul.util.text.Sanitizers;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import lombok.SneakyThrows;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class StringSanitizerSerialize extends JsonSerializer<String> implements ContextualSerializer {
    private static Map<Sanitizer.SanitizerType, Function<String, String>> processMap = new HashMap<>();

    static {
        processMap.put(Sanitizer.SanitizerType.BANK_CARD, (str) -> Sanitizers.bankCard(str));
        processMap.put(Sanitizer.SanitizerType.ADDRESS, (str) -> Sanitizers.address(str));
        processMap.put(Sanitizer.SanitizerType.CHINESE_NAME, (str) -> Sanitizers.chineseName(str));
        processMap.put(Sanitizer.SanitizerType.CAR_NUMBER, (str) -> Sanitizers.carNumber(str));
        processMap.put(Sanitizer.SanitizerType.EMAIL, (str) -> Sanitizers.email(str));
        processMap.put(Sanitizer.SanitizerType.ID_CARD, (str) -> Sanitizers.idCard(str));
        processMap.put(Sanitizer.SanitizerType.PHONE, (str) -> Sanitizers.phone(str));
        processMap.put(Sanitizer.SanitizerType.PASSWORD, (str) -> Sanitizers.password(str));
    }

    private Sanitizer.SanitizerType type;

    public StringSanitizerSerialize(Sanitizer.SanitizerType type) {
        this.type = type;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (processMap.containsKey(this.type)) {
            gen.writeObject(processMap.get(this.type).apply(value));
        } else {
            gen.writeObject(value);
        }
    }

    @Override
    @SneakyThrows
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (null == property) {
            return prov.findNullValueSerializer(null);
        }

        if (Objects.equals(property.getType().getRawClass(), String.class)) {
            Sanitizer sanitizer = property.getAnnotation(Sanitizer.class);
            if (null == sanitizer) {
                sanitizer = property.getContextAnnotation(Sanitizer.class);
            }

            if (null != sanitizer) {
                return new StringSanitizerSerialize(sanitizer.type());
            }
        }
        return prov.findValueSerializer(property.getType(), property);
    }

}
