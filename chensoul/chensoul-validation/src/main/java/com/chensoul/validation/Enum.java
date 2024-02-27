package com.chensoul.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Constraint(validatedBy = {Enum.EnumValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum {

    Class<?> enumClass();

    String message() default "枚举值不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EnumValidator implements ConstraintValidator<Enum, String> {
        private List<String> enumValues;

        @Override
        public void initialize(final Enum constraintAnnotation) {
            this.enumValues = Optional.ofNullable(constraintAnnotation)
                .map(Enum::enumClass)
                .filter(Class::isEnum)
                .map(Class::getEnumConstants)
                .map(arr -> Arrays.stream(arr)
                    .map(Object::toString)
                    .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext context) {
            return this.enumValues.contains(value);
        }
    }
}
