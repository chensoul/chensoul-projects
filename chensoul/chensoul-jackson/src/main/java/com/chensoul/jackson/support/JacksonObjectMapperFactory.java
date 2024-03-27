package com.chensoul.jackson.support;

import com.chensoul.jackson.JacksonObjectMapperCustomizer;
import com.chensoul.lang.function.CheckedSupplier;
import com.chensoul.spring.support.SpringExpressionLanguageValueResolver;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.net.URI;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

@SuperBuilder
@Getter
public class JacksonObjectMapperFactory {
    private final boolean defaultTypingEnabled;

    private final boolean singleValueAsArray;

    private final boolean singleArrayElementUnwrapped;

    private final boolean writeDatesAsTimestamps;

    @Builder.Default
    private final boolean failOnUnknownProperties = false;

    @Builder.Default
    private final boolean allowUnescapedControlChars = true;

    @Builder.Default
    private final boolean defaultViewInclusion = true;

    @Builder.Default
    private final boolean quoteFieldNames = true;

    @Builder.Default
    private final boolean useWrapperNameAsProperty = false;

    @Builder.Default
    private final Map<String, Object> injectableValues = new LinkedHashMap<>();

    @Builder.Default
    private final boolean minimal = false;

    private final JsonFactory jsonFactory;
    public static void configure(
        final ConfigurableApplicationContext applicationContext,
        final ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        ArrayList<JacksonObjectMapperCustomizer> serializers = new ArrayList<>(applicationContext.getBeansOfType(JacksonObjectMapperCustomizer.class).values());
        AnnotationAwareOrderComparator.sort(serializers);

        Map injectedValues = (Map) serializers
            .stream()
            .map(JacksonObjectMapperCustomizer::getInjectableValues)
            .flatMap(entry -> entry.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        objectMapper.setInjectableValues(new JacksonInjectableValueSupplier(() -> injectedValues));
    }

    /**
     * Produce an object mapper for YAML serialization/de-serialization.
     *
     * @return the object mapper
     */
    public ObjectMapper toObjectMapper() {
        val mapper = determineMapperInstance();
        return initialize(mapper);
    }

    /**
     * Initialize.
     *
     * @param mapper the mapper
     * @return the object mapper
     */
    protected ObjectMapper initialize(final MapperBuilder<?, ?> mapper) {
        val obm = mapper
            .defaultLocale(Locale.CHINA)
            .defaultTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, isSingleArrayElementUnwrapped())
            .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, isDefaultViewInclusion())
            .configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, isUseWrapperNameAsProperty())
            .configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), isQuoteFieldNames())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, isFailOnUnknownProperties())
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, false)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, isSingleValueAsArray())
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, isQuoteFieldNames())
            .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), isAllowUnescapedControlChars())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, isWriteDatesAsTimestamps())
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .build();

        obm.setInjectableValues(new JacksonInjectableValueSupplier(this::getInjectableValues))
            .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
            .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
            .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
            .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
            .findAndRegisterModules()
            .registerModule(getCasJacksonModule())
            .registerModule(new CustomJavaTimeModule())
            .registerModule(new ParameterNamesModule());

        if (isDefaultTypingEnabled()) {
            obm.activateDefaultTyping(obm.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }

        if (minimal) {
            obm.setDefaultPrettyPrinter(new MinimalPrettyPrinter());
        }
        return obm;
    }

    private MapperBuilder<?, ?> determineMapperInstance() {
        return JsonMapper.builder(jsonFactory);
    }

    private static Module getCasJacksonModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(URI.class, new URIDeserializer());
        return simpleModule;
    }

    private static final class URIDeserializer extends StdDeserializer<URI> {
        private static final long serialVersionUID = -7547162569192932415L;

        URIDeserializer() {
            this(null);
        }

        URIDeserializer(final Class<?> vc) {
            super(vc);
        }

        @Override
        public URI deserialize(final JsonParser jp, final DeserializationContext ctxt) {
            return CheckedSupplier.unchecked(() -> {
                val value = SpringExpressionLanguageValueResolver.getInstance().resolve(jp.getText().trim());
                return StringUtils.isNotBlank(value) ? new URI(value) : null;
            }).get();
        }
    }
}
