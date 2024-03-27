package com.chensoul.lang.function;

import java.util.function.Function;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CheckedFunctionTest {

    @Test
    public void testCheckedFunction() {
        CheckedFunction<Integer, Integer> divideByZero = x -> {
            if (x == 0) {
                throw new Exception("Cannot divide by zero");
            }
            return 10 / x;
        };

        try {
            Assertions.assertEquals(2, divideByZero.apply(5));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        Function<Integer, Integer> f1 = CheckedFunction.unchecked(divideByZero, FunctionUtils.SNEAKY_THROW);
        Assertions.assertEquals(2, f1.apply(5));

        Assertions.assertThrows(Exception.class, () -> f1.apply(0));

        Function<Integer, Integer> f2 = CheckedFunction.unchecked(divideByZero, FunctionUtils.CHECKED_THROW);
        Assertions.assertEquals(2, f2.apply(5));
        Assertions.assertThrows(UncheckedException.class, () -> f2.apply(0));
    }

}
