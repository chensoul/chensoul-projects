package com.chensoul.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import javax.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

public class EnumCodeTest {

    ApplicationContextRunner runner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(ValidationAutoConfiguration.class));

    @Test
    public void test() {
        this.runner.withUserConfiguration(Config.class)
            .run(context -> {
                final Handler bean = context.getBean(Handler.class);
                bean.handle(1);

                assertThatThrownBy(() -> bean.handle(3))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessage("handle.code: 枚举值不正确");
            });
    }

    @Validated
    static class Handler {
        public void handle(@EnumCode(enumClass = SexCodeEnum.class) final Integer code) {

        }
    }

    @Getter
    @AllArgsConstructor
    public enum SexCodeEnum {
        MAN(1, "男"),
        WOMAN(2, "女");

        private int code;
        private String name;
    }

    static class Config {
        @Bean
        public Handler handler() {
            return new Handler();
        }
    }


}
