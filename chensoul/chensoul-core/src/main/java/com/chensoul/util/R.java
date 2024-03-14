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
 * @version $Id: $Id
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {
    private static final long serialVersionUID = 6551531108468957025L;

    private int code;
    private String message;
    private T data;

    /**
     * <p>Constructor for R.</p>
     */
    public R() {
    }

    /**
     * <p>ok.</p>
     *
     * @param <T> a T class
     * @return a {@link com.chensoul.util.R} object
     */
    public static <T> R<T> ok() {
        return new R<T>().setCode(0).setMessage("OK");
    }

    /**
     * <p>ok.</p>
     *
     * @param data a T object
     * @param <T> a T class
     * @return a {@link com.chensoul.util.R} object
     */
    public static <T> R<T> ok(final T data) {
        return new R<T>().setCode(0).setMessage("OK").setData(data);
    }

    /**
     * <p>error.</p>
     *
     * @param <T> a T class
     * @return a {@link com.chensoul.util.R} object
     */
    public static <T> R<T> error() {
        return error(ResultCode.SYSTEM_ERROR.getName());
    }

    /**
     * <p>error.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param <T> a T class
     * @return a {@link com.chensoul.util.R} object
     */
    public static <T> R<T> error(final String message) {
        return error(ResultCode.SYSTEM_ERROR.getCode(), message);
    }

    /**
     * <p>error.</p>
     *
     * @param code a int
     * @param message a {@link java.lang.String} object
     * @param <T> a T class
     * @return a {@link com.chensoul.util.R} object
     */
    public static <T> R<T> error(final int code, final String message) {
        return new R<T>().setCode(code).setMessage(message);
    }

    /**
     * <p>toMap.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("code", this.getCode());
        map.put("message", this.getMessage());
        map.put("data", this.getData());

        return map;
    }

}
