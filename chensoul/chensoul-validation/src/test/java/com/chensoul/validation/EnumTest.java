package com.chensoul.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

public class EnumTest {

    ApplicationContextRunner runner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(ValidationAutoConfiguration.class));

    @Test
    public void test() {
        this.runner.withUserConfiguration(Config.class)
            .run(context -> {
                final Handler bean = context.getBean(Handler.class);
                bean.handle("man");

                assertThatThrownBy(() -> bean.handle("error"))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessage("handle.sex: 枚举值不正确");
            });
    }

    @Validated
    static class Handler {
        public void handle(@Enum(enumClass = SexEnum.class) final String sex) {

        }
    }

    public enum SexEnum {
        man, woman
    }


    static class Config {
        @Bean
        public Handler handler() {
            return new Handler();
        }
    }


}
