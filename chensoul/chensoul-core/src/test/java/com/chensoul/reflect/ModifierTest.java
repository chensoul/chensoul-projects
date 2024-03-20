package com.chensoul.reflect;

/*-
 * #%L
 * Chensoul :: Core
 * %%
 * Copyright (C) 2023 - 2024 chensoul.cc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * {@link Modifier} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ModifierTest {

    @Test
    public void testGetValue() {
        for (Modifier modifier : Modifier.values()) {
            assertEquals(modifier.getValue(), findModifierValue(modifier.name()));
        }
    }

    @Test
    public void testMatches() {
        for (Modifier modifier : Modifier.values()) {
            assertTrue(modifier.matches(modifier.getValue()));
        }
    }

    private int findModifierValue(String name) {
        return FieldUtils.getStaticFieldValue(java.lang.reflect.Modifier.class, name);
    }
}
