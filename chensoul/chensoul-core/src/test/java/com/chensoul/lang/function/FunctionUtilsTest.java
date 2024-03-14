package com.chensoul.lang.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This is {@link FunctionUtilsTest}.
 */
class FunctionUtilsTest {

    @Test
    void doIf() {
        //Function
        Predicate<Integer> predicate = t -> t > 0;
        Function<Integer, Integer> f1 = FunctionUtils.doIf(predicate, t -> t + 1, t -> t - 1);
        Assertions.assertEquals(2, f1.apply(1));
        Assertions.assertEquals(-2, f1.apply(-1));

        f1 = FunctionUtils.doIf(predicate, t -> t + 1);
        Assertions.assertEquals(2, f1.apply(1));
        Assertions.assertEquals(null, f1.apply(-1));

        Function<Integer, Integer> f2 = FunctionUtils.doIf(true, t -> t + 1, t -> t - 1);
        Assertions.assertEquals(2, f2.apply(1));

        f2 = FunctionUtils.doIf(true, t -> t + 1);
        Assertions.assertEquals(2, f2.apply(1));

        Function<Integer, Integer> f3 = FunctionUtils.doIf(false, t -> t + 1, t -> t - 1);
        Assertions.assertEquals(0, f3.apply(1));

        f3 = FunctionUtils.doIf(false, t -> t + 1);
        Assertions.assertEquals(null, f3.apply(1));

        Function<Integer, Integer> f5 = FunctionUtils.doIf(predicate, t -> t + 1);
        Assertions.assertEquals(2, f5.apply(1));
        Assertions.assertEquals(null, f5.apply(-1));

        //Consumer
        Consumer<String> trueConsumer = s -> System.out.println("Condition is true: " + s);
        Consumer<String> falseConsumer = s -> System.out.println("Condition is false: " + s);
        FunctionUtils.doIf(true, trueConsumer, falseConsumer).accept("test");
        FunctionUtils.doIf(false, trueConsumer, falseConsumer).accept("test");

        FunctionUtils.doIf(true, trueConsumer).accept("test");
        FunctionUtils.doIf(false, trueConsumer).accept("test");

        //Supplier
        Supplier<String> trueSupplier = () -> "Condition is true";
        Supplier<String> falseSupplier = () -> "Condition is false";
        Supplier<String> s1 = FunctionUtils.doIf(true, trueSupplier, falseSupplier);
        Supplier<String> s2 = FunctionUtils.doIf(false, trueSupplier, falseSupplier);
        Assertions.assertEquals("Condition is true", s1.get());
        Assertions.assertEquals("Condition is false", s2.get());

        s1 = FunctionUtils.doIf(true, trueSupplier);
        s2 = FunctionUtils.doIf(false, trueSupplier);
        Assertions.assertEquals("Condition is true", s1.get());
        Assertions.assertEquals(null, s2.get());
    }

    @Test
    void tryApply() {
        CheckedFunction<Integer, String> function = num -> {
            if (num % 2 == 0) {
                return "Even";
            } else {
                throw new IllegalArgumentException("Odd number not allowed");
            }
        };

        CheckedFunction<Throwable, String> errorHandler = throwable -> "Error: " + throwable.getMessage();
        CheckedConsumer<Integer> finalConsumer = num -> System.out.println("Final consumer: " + num);

        Function<Integer, String> f1 = FunctionUtils.tryApply(function, errorHandler, finalConsumer);
        Assertions.assertEquals("Even", f1.apply(4));
        Assertions.assertEquals("Error: Odd number not allowed", f1.apply(3));

        f1 = FunctionUtils.tryApply(function, errorHandler);
        Assertions.assertEquals("Even", f1.apply(4));
        Assertions.assertEquals("Error: Odd number not allowed", f1.apply(3));


    }

    @Test
    void tryAccept() {
        CheckedConsumer<String> consumer = str -> {
            if (str.length() > 5) {
                throw new IllegalArgumentException("String length exceeds limit");
            } else {
                System.out.println("Valid string: " + str);
            }
        };

        CheckedFunction<Throwable, String> errorHandler = throwable -> {
            System.out.println("Error occurred: " + throwable.getMessage());
            return null;
        };

        CheckedConsumer<String> finalConsumer = str -> System.out.println("Final consumer: " + str);

        Consumer<String> resultConsumer = FunctionUtils.tryAccept(consumer, errorHandler, finalConsumer);
        resultConsumer.accept("Hello");
    }

    @Test
    void tryGet() {
        CheckedSupplier<Integer> supplier = () -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("Random error occurred");
            } else {
                return 42;
            }
        };

        CheckedFunction<Throwable, Integer> errorHandler = throwable -> {
            System.out.println("Error occurred: " + throwable.getMessage());
            return -1;
        };

        CheckedConsumer<Integer> finalConsumer = num -> System.out.println("Final consumer: " + num);

        Supplier<Integer> resultSupplier = FunctionUtils.tryGet(supplier, errorHandler, finalConsumer);
        Integer result = resultSupplier.get();

        System.out.println("Result: " + result);
    }
}
