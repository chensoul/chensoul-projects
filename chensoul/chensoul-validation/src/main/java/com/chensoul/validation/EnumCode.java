package com.chensoul.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.util.ReflectionUtils;


/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumCode.EnumCodeValidator.class})
public @interface EnumCode {

    /**
     * 允许的枚举
     *
     * @return
     */
    Class<?> enumClass();

    /**
     * @return
     */
    String fieldName() default "code";

    /**
     * @return
     */
    String message() default "枚举值不正确";

    /**
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * @return
     */
    Class<? extends Payload>[] payload() default {};

    class EnumCodeValidator implements ConstraintValidator<EnumCode, Integer> {
        private List<Integer> enumValues;

        @Override
        public void initialize(final EnumCode constraintAnnotation) {
            this.enumValues = Optional.ofNullable(constraintAnnotation)
                .map(EnumCode::enumClass)
                .filter(Class::isEnum)
                .map(Class::getEnumConstants)
                .map(arr -> Arrays.stream(arr)
                    .map(t -> (Integer) getFieldValue(t, constraintAnnotation.fieldName()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        }

        private Object getFieldValue(Object enumConstant, String filedName) {
            AtomicReference<Object> o = new AtomicReference<>();
            ReflectionUtils.doWithFields(enumConstant.getClass(), field -> {
                if (field.getName().equals(filedName)) {
                    field.setAccessible(true);
                    o.set(field.get(enumConstant));
                }
            });
            return o.get();
        }

        @Override
        public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
            return enumValues.contains(value);
        }

    }
}
