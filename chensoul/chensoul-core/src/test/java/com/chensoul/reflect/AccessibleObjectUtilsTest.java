package com.chensoul.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * AccessibleObjectUtils Test
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
class AccessibleObjectUtilsTest {

    @Test
    void execute() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException {
        Constructor<MyClass> constructor = MyClass.class.getDeclaredConstructor(int.class, String.class);

        MyClass myClass = AccessibleObjectUtils.execute(constructor, () -> constructor.newInstance(42, "Hello"));
        Assertions.assertTrue(myClass instanceof MyClass);

        Field field = MyClass.class.getDeclaredField("value");

        // CheckedSupplier
        Integer value = AccessibleObjectUtils.execute(field, () -> (Integer) field.get(myClass));
        Assertions.assertEquals(42, value);

        // CheckedFunction
        value = AccessibleObjectUtils.execute(field, () -> (Integer) field.get(myClass));
        Assertions.assertEquals(42, value);
        AccessibleObjectUtils.execute(field, t -> (Integer) t.get(myClass));

        //CheckedSupplier
        Method method = MyClass.class.getDeclaredMethod("getMessage");
        String message = AccessibleObjectUtils.execute(method, () -> (String) method.invoke(myClass));
        Assertions.assertEquals("Hello", message);

        message = AccessibleObjectUtils.execute(method, m -> (String) m.invoke(myClass));
        Assertions.assertEquals("Hello", message);


        AccessibleObjectUtils.execute(method, m -> {
            String msg = (String) m.invoke(myClass);
            Assertions.assertEquals("Hello", msg);
        });
    }
}

class MyClass {
    private int value;
    private String message;

    private MyClass(int value, String message) {
        this.value = value;
        this.message = message;
    }

    private String getMessage() {
        return message;
    }
}
