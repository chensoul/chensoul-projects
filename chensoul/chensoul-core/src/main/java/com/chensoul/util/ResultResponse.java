package com.chensoul.util;

import com.chensoul.exception.ErrorCode;
import com.chensoul.exception.ErrorResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
@Accessors(chain = true)
public class ResultResponse<T> extends ErrorResponse implements Serializable {
    private static final long serialVersionUID = 6551531108468957025L;

    @Getter
    private T data;

    public ResultResponse(int status, int code, final T data) {
        super(status, code, null);
        this.data = data;
    }

    public ResultResponse(int status, int code, String message) {
        super(status, code, message);
    }

    /**
     * <p>ok.</p>
     *
     * @param <T> a T class
     * @return a {@link ResultResponse} object
     */
    public static <T> ResultResponse<T> ok() {
        return new ResultResponse<T>(200, 0, "OK");
    }

    /**
     * <p>ok.</p>
     *
     * @param data a T object
     * @param <T> a T class
     * @return a {@link ResultResponse} object
     */
    public static <T> ResultResponse<T> ok(final T data) {
        return new ResultResponse<T>(200, 0, data);
    }

    /**
     * <p>error.</p>
     *
     * @param <T> a T class
     * @return a {@link ResultResponse} object
     */
    public static <T> ResultResponse<T> error() {
        return error(ErrorCode.SYSTEM_ERROR.getName());
    }

    /**
     * <p>error.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param <T> a T class
     * @return a {@link ResultResponse} object
     */
    public static <T> ResultResponse<T> error(final String message) {
        return error(500, ErrorCode.SYSTEM_ERROR.getCode(), message);
    }

    /**
     * <p>error.</p>
     *
     * @param code a int
     * @param message a {@link java.lang.String} object
     * @param <T> a T class
     * @return a {@link ResultResponse} object
     */
    public static <T> ResultResponse<T> error(int status, int code, final String message) {
        return new ResultResponse<T>(status, code, message);
    }

    /**
     * <p>toMap.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("code", this.getCode());
        map.put("status", this.getStatus());
        map.put("message", this.getMessage());
        map.put("data", this.getData());
        map.put("timestamp", this.getTimestamp());

        return map;
    }

}
