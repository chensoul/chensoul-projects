package com.chensoul.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * BooleanUtils provides utility methods and decorators for {@code Boolean} objects.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class BooleanUtils {
    /**
     * The false String {@code "false"}.
     */
    public static final String FALSE = "false";
    /**
     * The no String {@code "no"}.
     */
    public static final String NO = "no";
    /**
     * The off String {@code "off"}.
     */
    public static final String OFF = "off";
    /**
     * The on String {@code "on"}.
     */
    public static final String ON = "on";
    /**
     * The true String {@code "true"}.
     */
    public static final String TRUE = "true";
    /**
     * The yes String {@code "yes"}.
     */
    public static final String YES = "yes";
    private static final List<Boolean> BOOLEAN_LIST = Collections.unmodifiableList(Arrays.asList(Boolean.FALSE, Boolean.TRUE));

    /**
     * <p>isTrue.</p>
     *
     * @param bool a {@link java.lang.Boolean} object
     * @return a boolean
     */
    public static boolean isTrue(Boolean bool) {
        if (bool == null) {
            return false;
        }
        return bool.booleanValue() ? true : false;
    }

    /**
     * <p>isNotTrue.</p>
     *
     * @param bool a {@link java.lang.Boolean} object
     * @return a boolean
     */
    public static boolean isNotTrue(Boolean bool) {
        return !isTrue(bool);
    }

    /**
     * <p>isFalse.</p>
     *
     * @param bool a {@link java.lang.Boolean} object
     * @return a boolean
     */
    public static boolean isFalse(Boolean bool) {
        if (bool == null) {
            return false;
        }
        return bool.booleanValue() ? false : true;
    }

    /**
     * <p>isNotFalse.</p>
     *
     * @param bool a {@link java.lang.Boolean} object
     * @return a boolean
     */
    public static boolean isNotFalse(Boolean bool) {
        return !isFalse(bool);
    }

    /**
     * <p>toString.</p>
     *
     * @param bool a boolean
     * @param trueString a {@link java.lang.String} object
     * @param falseString a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String toString(final boolean bool, final String trueString, final String falseString) {
        return bool ? trueString : falseString;
    }

    /**
     * <p>toBoolean.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a boolean
     */
    public static boolean toBoolean(final String str) {
        return toBooleanObject(str) == Boolean.TRUE;
    }

    /**
     * <p>toBooleanObject.</p>
     *
     * @param value a {@link java.lang.Integer} object
     * @return a {@link java.lang.Boolean} object
     */
    public static Boolean toBooleanObject(final Integer value) {
        if (value == null) {
            return null;
        }
        return value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * <p>toBooleanObject.</p>
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.Boolean} object
     */
    public static Boolean toBooleanObject(final String str) {
        // Previously used equalsIgnoreCase, which was fast for interned 'true'.
        // Non interned 'true' matched 15 times slower.
        //
        // Optimisation provides same performance as before for interned 'true'.
        // Similar performance for null, 'false', and other strings not length 2/3/4.
        // 'true'/'TRUE' match 4 times slower, 'tRUE'/'True' 7 times slower.
        if (str == TRUE) {
            return Boolean.TRUE;
        }
        if (str == null) {
            return null;
        }
        switch (str.length()) {
            case 1: {
                final char ch0 = str.charAt(0);
                if (ch0 == 'y' || ch0 == 'Y' ||
                    ch0 == 't' || ch0 == 'T' ||
                    ch0 == '1') {
                    return Boolean.TRUE;
                }
                if (ch0 == 'n' || ch0 == 'N' ||
                    ch0 == 'f' || ch0 == 'F' ||
                    ch0 == '0') {
                    return Boolean.FALSE;
                }
                break;
            }
            case 2: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                if ((ch0 == 'o' || ch0 == 'O') &&
                    (ch1 == 'n' || ch1 == 'N')) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'n' || ch0 == 'N') &&
                    (ch1 == 'o' || ch1 == 'O')) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 3: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                if ((ch0 == 'y' || ch0 == 'Y') &&
                    (ch1 == 'e' || ch1 == 'E') &&
                    (ch2 == 's' || ch2 == 'S')) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'o' || ch0 == 'O') &&
                    (ch1 == 'f' || ch1 == 'F') &&
                    (ch2 == 'f' || ch2 == 'F')) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 4: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                if ((ch0 == 't' || ch0 == 'T') &&
                    (ch1 == 'r' || ch1 == 'R') &&
                    (ch2 == 'u' || ch2 == 'U') &&
                    (ch3 == 'e' || ch3 == 'E')) {
                    return Boolean.TRUE;
                }
                break;
            }
            case 5: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                final char ch4 = str.charAt(4);
                if ((ch0 == 'f' || ch0 == 'F') &&
                    (ch1 == 'a' || ch1 == 'A') &&
                    (ch2 == 'l' || ch2 == 'L') &&
                    (ch3 == 's' || ch3 == 'S') &&
                    (ch4 == 'e' || ch4 == 'E')) {
                    return Boolean.FALSE;
                }
                break;
            }
            default:
                break;
        }

        return null;
    }
}
