package com.chensoul.text;

import com.chensoul.constant.StringPool;
import com.chensoul.util.StringUtils;
import java.util.function.Function;

/**
 * Sanitizers is responsible for sanitizing sensitive information.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class Sanitizers {
    private Sanitizers() {
    }

    /**
     * <p>address.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String address(final String str) {
        return sensitive(str, 2, 1);
    }

    /**
     * <p>bankCard.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String bankCard(final String str) {
        return sensitive(str, 2, 1);
    }

    /**
     * <p>carNumber.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String carNumber(final String str) {
        return sensitive(str, 2, 1);
    }

    /**
     * <p>chineseName.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String chineseName(final String str) {
        return sensitive(str, s -> {
            final int length = StringUtils.length(s);
            return StringUtils.substring(s, 0, 1) + StringUtils.repeat(StringPool.STAR, length - 1) + StringUtils.substring(s, length - 0);
        });
    }

    /**
     * <p>idCard.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String idCard(final String str) {
        return sensitive(str, 4, 1);
    }

    /**
     * <p>phone.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String phone(final String str) {
        return sensitive(str, 3, 4);
    }

    /**
     * <p>password.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String password(final String str) {
        return sensitive(str, s -> StringUtils.repeat(StringPool.STAR, 6));
    }

    /**
     * <p>email.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String email(final String str) {
        return sensitive(str, s -> StringUtils.substring(s, 0, 1)
            + StringUtils.repeat(StringPool.STAR, s.indexOf("@") - 1)
            + "@"
            + StringUtils.substringAfter(s, StringPool.AT));
    }

    private static String sensitive(final String str, final int first, final int last) {
        return sensitive(str, s -> {
            final int length = StringUtils.length(s);
            return StringUtils.substring(s, 0, first) + StringUtils.repeat(StringPool.STAR, length - first - last) + StringUtils.substring(s, length - last);
        });
    }

    private static String sensitive(final String str, final Function<String, String> function) {
        return StringUtils.isEmpty(str) ? null : function.apply(str);
    }

}
