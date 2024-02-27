package com.chensoul.util.text;

import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
class SanitizersTest {
    @Test
    void test() {
        log.info(Sanitizers.address("湖北省武汉市江岸区后湖大道华森家园1栋1001"));
        log.info(Sanitizers.address("湖北省武汉市江岸区后湖大道华森家园"));
        log.info(Sanitizers.address("湖北省武汉市"));
        log.info(Sanitizers.address("湖北省"));

        log.info(Sanitizers.chineseName("张三"));
        log.info(Sanitizers.chineseName("张三丰"));
        log.info(Sanitizers.chineseName("张三丰收"));

        log.info(Sanitizers.carNumber("鄂WJ8888"));
        log.info(Sanitizers.carNumber("鄂WJ8"));
        log.info(Sanitizers.carNumber("鄂WJ"));
        log.info(Sanitizers.carNumber("鄂WJ88888"));

        log.info(Sanitizers.idCard("420117198601018710"));

        log.info(Sanitizers.email("aaa@163.com"));

        log.info(Sanitizers.password("12345678"));

        log.info(Sanitizers.phone("12345678901"));
        log.info(Sanitizers.phone("08612345678901"));

        assertEquals("湖北**************园", Sanitizers.address("湖北省武汉市江岸区后湖大道华森家园"));
    }
}
