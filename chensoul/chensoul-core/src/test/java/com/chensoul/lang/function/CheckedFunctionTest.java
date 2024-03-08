/**
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
