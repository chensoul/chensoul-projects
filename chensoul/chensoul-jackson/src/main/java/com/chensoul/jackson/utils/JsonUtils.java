package com.chensoul.jackson.utils;

import com.chensoul.jackson.support.JacksonObjectMapperFactory;
import com.chensoul.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.MimeTypeUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper MAPPER = JacksonObjectMapperFactory.builder()
        .build().toObjectMapper();

    private JsonUtils() {
    }


    /**
     * @param obj
     * @return
     */
    @SneakyThrows
    public static String toJson(final Object obj) {
        return MAPPER.writeValueAsString(obj);
    }

    /**
     * @param obj
     * @param stream
     */
    @SneakyThrows
    public static void toJson(final Object obj, final OutputStream stream) {
        MAPPER.writeValue(stream, obj);
    }


    /**
     * @param obj
     * @param file
     */
    @SneakyThrows
    public static void toJson(final Object obj, final File file) {
        MAPPER.writeValue(file, obj);
    }

    /**
     * @param o
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toMap(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return fromJson((String) o, Map.class);
        }
        return MAPPER.convertValue(o, Map.class);
    }

    /**
     * @param json
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> List<T> toList(final String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return MAPPER.readValue(json, List.class);
    }

    /**
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> List<T> toList(final String json, final Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        final JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);

        return MAPPER.readValue(json, javaType);
    }

    /**
     * @param json
     * @param field
     * @return
     */
    public static JsonNode getJsonField(final String json, final String field) {
        final JsonNode jsonNode = getJsonNode(json);
        if (null == jsonNode) {
            return null;
        }
        return jsonNode.get(field);
    }

    /**
     * @param json
     * @return
     */
    public static JsonNode getJsonNode(final String json) {
        try {
            return MAPPER.readTree(json);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    /**
     * @param json
     * @param field
     * @return
     */
    public static String getJsonFieldString(final String json, final String field) {
        final JsonNode jsonNode = getJsonField(json, field);
        if (null == jsonNode) {
            return null;
        }
        return jsonNode.asText();
    }

    /**
     * @param json
     * @param field
     * @return
     */
    @SneakyThrows
    public static String fromJson(final String json, final String field) {
        return MAPPER.readTree(json).get(field).toString();
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    @SneakyThrows
    public static <T> T fromJson(final String json, final Class<T> clazz) {
        return (T) MAPPER.readValue(json, clazz);
    }


    /**
     * @param json
     * @param type
     * @return
     */
    @SneakyThrows
    public static <T> T fromJson(final String json, final TypeReference<T> type) {
        return (T) MAPPER.readValue(json, type);

    }

    /**
     * 字符串转换为指定对象，并增加泛型转义
     *
     * @param json             json字符串
     * @param parametrized     目标对象
     * @param parameterClasses 泛型对象
     */
    @SneakyThrows
    public static <T> T fromJson(final String json, final Class<?> parametrized, final Class<?>... parameterClasses) {
        if (StringUtils.isBlank(json) || parametrized == null) {
            return null;
        }
        final JavaType javaType = MAPPER.getTypeFactory()
            .constructParametricType(parametrized, parameterClasses);
        return (T) MAPPER.readValue(json, javaType);
    }

    @SneakyThrows
    public static String render(final Object model) {
        return MAPPER.writeValueAsString(model);
    }

    /**
     * Render properties and view.
     *
     * @param model    the properties
     * @param response the response
     */
    @SneakyThrows
    public static void render(final HttpServletResponse response, final Object model) {
        final MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setPrettyPrint(true);
        final MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);

        jsonConverter.write(model, jsonMimeType, new ServletServerHttpResponse(response));
    }

    /**
     * Render properties and view. Sets the response status to OK.
     *
     * @param response the response
     */
    public static void render(final HttpServletResponse response) {
        val map = new HashMap<String, Object>();
        response.setStatus(HttpServletResponse.SC_OK);
        render(response, map);
    }

    /**
     * Render exceptions. Adds error messages and the stack trace to the json properties
     * and sets the response status accordingly to note bad requests.
     *
     * @param ex       the ex
     * @param response the response
     */
    public static void renderException(final Exception ex, final HttpServletResponse response) {
        val map = new HashMap<String, Object>();
        map.put("error", ex.getMessage());
        map.put("stacktrace", Arrays.deepToString(ex.getStackTrace()));
        renderException(map, response);
    }

    /**
     * Render exceptions. Sets the response status accordingly to note bad requests.
     *
     * @param model    the properties
     * @param response the response
     */
    private static void renderException(final Map<String, Object> model, final HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        model.put("status", HttpServletResponse.SC_BAD_REQUEST);
        render(response, model);
    }

    /**
     * Is valid json?.
     *
     * @param json the json
     * @return true/false
     */
    @SneakyThrows
    public static boolean isValidJson(final String json) {
        return !MAPPER.readTree(json).isEmpty();
    }
}
