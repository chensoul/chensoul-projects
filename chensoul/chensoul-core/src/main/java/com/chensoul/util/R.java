package com.chensoul.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Pojo for response with code, message and data
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {
    private static final long serialVersionUID = 6551531108468957025L;

    private int code;
    private String message;
    private T data;

    public R() {
    }

    public static <T> R<T> ok() {
        return new R<T>().setCode(0).setMessage("OK");
    }

    public static <T> R<T> ok(final T data) {
        return new R<T>().setCode(0).setMessage("OK").setData(data);
    }

    public static <T> R<T> error() {
        return error(ResultCode.SYSTEM_ERROR.getName());
    }

    public static <T> R<T> error(final String message) {
        return error(ResultCode.SYSTEM_ERROR.getCode(), message);
    }

    public static <T> R<T> error(final int code, final String message) {
        return new R<T>().setCode(code).setMessage(message);
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("code", this.getCode());
        map.put("message", this.getMessage());
        map.put("data", this.getData());

        return map;
    }

}
