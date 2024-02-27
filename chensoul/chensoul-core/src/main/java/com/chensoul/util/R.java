package com.chensoul.util;

import com.chensoul.constant.ResultCode;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Result Object
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
@Getter
public class R<T> implements Serializable {
    private static final long serialVersionUID = 6551531108468957025L;

    private final int code;
    private final String message;
    private final T data;

    private R(final int code, final String message, final T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<>(0, "OK", null);
    }

    public static <T> R<T> ok(final T object) {
        return new R<>(0, "OK", object);
    }

    public static <T> R<T> error() {
        return error(ResultCode.SYSTEM_ERROR.getName());
    }

    public static <T> R<T> error(final String message) {
        return error(500, message);
    }

    public static <T> R<T> error(final int code, final String message) {
        return new R<>(code, message, null);
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("code", this.getCode());
        map.put("message", this.getMessage());
        map.put("data", this.getData());

        return map;
    }

}
