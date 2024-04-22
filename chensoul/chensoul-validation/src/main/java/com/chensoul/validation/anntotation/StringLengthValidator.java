/**
 * Copyright © 2016-2024 The Thingsboard Authors
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
package com.chensoul.validation.anntotation;

import com.fasterxml.jackson.databind.JsonNode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class StringLengthValidator implements ConstraintValidator<Length, Object> {
    private int max;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String stringValue;
        if (value instanceof CharSequence || value instanceof JsonNode) {
            stringValue = value.toString();
        } else {
            return true;
        }
        if (StringUtils.isEmpty(stringValue)) {
            return true;
        }
        return stringValue.length() <= max;
    }

    @Override
    public void initialize(Length constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }
}
