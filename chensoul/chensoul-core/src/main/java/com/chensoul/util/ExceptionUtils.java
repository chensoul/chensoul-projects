package com.chensoul.util;


import com.chensoul.collection.ArrayUtils;
import com.chensoul.reflect.ClassUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link java.lang.Exception} Utilities class
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class ExceptionUtils {
    private static final Object CAUSE_METHOD_NAMES_LOCK = new Object();
    private static String[] CAUSE_METHOD_NAMES = {
        "getCause",
        "getNextException",
        "getTargetException",
        "getException",
        "getSourceException",
        "getRootCause",
        "getCausedByException",
        "getNested",
        "getLinkedException",
        "getNestedException",
        "getLinkedCause",
        "getThrowable",
    };

    /**
     * <p>getRootCause.</p>
     *
     * @param throwable a {@link java.lang.Throwable} object
     * @return a {@link java.lang.Throwable} object
     */
    public static Throwable getRootCause(Throwable throwable) {
        List list = getThrowableList(throwable);
        return list.size() < 2 ? null : (Throwable) list.get(list.size() - 1);
    }

    /**
     * <p>getThrowableList.</p>
     *
     * @param throwable a {@link java.lang.Throwable} object
     * @return a {@link java.util.List} object
     */
    public static List<Throwable> getThrowableList(Throwable throwable) {
        List<Throwable> list = new ArrayList();
        while (throwable != null && list.contains(throwable) == false) {
            list.add(throwable);
            throwable = ExceptionUtils.getCause(throwable);
        }
        return list;
    }

    /**
     * <p>getCause.</p>
     *
     * @param throwable a {@link java.lang.Throwable} object
     * @return a {@link java.lang.Throwable} object
     */
    public static Throwable getCause(Throwable throwable) {
        synchronized (CAUSE_METHOD_NAMES_LOCK) {
            return getCause(throwable, CAUSE_METHOD_NAMES);
        }
    }

    /**
     * <p>getCause.</p>
     *
     * @param throwable a {@link java.lang.Throwable} object
     * @param methodNames an array of {@link java.lang.String} objects
     * @return a {@link java.lang.Throwable} object
     */
    public static Throwable getCause(Throwable throwable, String[] methodNames) {
        if (throwable == null) {
            return null;
        }
        Throwable cause = getCauseUsingWellKnownTypes(throwable);
        if (cause == null) {
            if (methodNames == null) {
                synchronized (CAUSE_METHOD_NAMES_LOCK) {
                    methodNames = CAUSE_METHOD_NAMES;
                }
            }
            for (int i = 0; i < methodNames.length; i++) {
                String methodName = methodNames[i];
                if (methodName != null) {
                    cause = getCauseUsingMethodName(throwable, methodName);
                    if (cause != null) {
                        break;
                    }
                }
            }

            if (cause == null) {
                cause = getCauseUsingFieldName(throwable, "detail");
            }
        }
        return cause;
    }

    private static Throwable getCauseUsingWellKnownTypes(Throwable throwable) {
        if (throwable instanceof SQLException) {
            return ((SQLException) throwable).getNextException();
        } else if (throwable instanceof InvocationTargetException) {
            return ((InvocationTargetException) throwable).getTargetException();
        } else {
            return null;
        }
    }

    private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
        Method method = null;
        try {
            method = throwable.getClass().getMethod(methodName, null);
        } catch (NoSuchMethodException ignored) {
            // exception ignored
        } catch (SecurityException ignored) {
            // exception ignored
        }

        if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
            try {
                return (Throwable) method.invoke(throwable, ArrayUtils.EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException ignored) {
                // exception ignored
            } catch (IllegalArgumentException ignored) {
                // exception ignored
            } catch (InvocationTargetException ignored) {
                // exception ignored
            }
        }
        return null;
    }

    private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName) {
        Field field = null;
        try {
            field = throwable.getClass().getField(fieldName);
        } catch (NoSuchFieldException ignored) {
            // exception ignored
        } catch (SecurityException ignored) {
            // exception ignored
        }

        if (field != null && Throwable.class.isAssignableFrom(field.getType())) {
            try {
                return (Throwable) field.get(throwable);
            } catch (IllegalAccessException ignored) {
                // exception ignored
            } catch (IllegalArgumentException ignored) {
                // exception ignored
            }
        }
        return null;
    }

    /**
     * <p>getRootCauseMessage.</p>
     *
     * @param th a {@link java.lang.Throwable} object
     * @return a {@link java.lang.String} object
     */
    public static String getRootCauseMessage(Throwable th) {
        Throwable root = ExceptionUtils.getRootCause(th);
        root = (root == null ? th : root);
        return getMessage(root);
    }

    /**
     * <p>getMessage.</p>
     *
     * @param th a {@link java.lang.Throwable} object
     * @return a {@link java.lang.String} object
     */
    public static String getMessage(Throwable th) {
        if (th == null) {
            return "";
        }
        return th.getMessage();
    }

    /**
     * <p>wrap.</p>
     *
     * @param source a T object
     * @param thrownType a {@link java.lang.Class} object
     * @param <T> a T class
     * @param <TT> a TT class
     * @return a TT object
     */
    public static <T extends Throwable, TT extends Throwable> TT wrap(T source, Class<TT> thrownType) {
        if (ClassUtils.isAssignableFrom(thrownType, source.getClass())) {
            return (TT) source;
        }
        Object[] args = resolveArguments(source);
        return ClassUtils.newInstance(thrownType, args);
    }

    private static <T extends Throwable> Object[] resolveArguments(T source) {
        String message = source.getMessage();
        Throwable cause = source.getCause() == null ? source : source.getCause();
        return message == null ? new Object[]{cause} : new Object[]{message, cause};
    }
}
