package com.chensoul.constant;

import com.chensoul.util.Enumerable;
import com.chensoul.util.ResultCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
class ResultCodeTest {

    @Test
    void getCode() {
        final ResultCode resultCode = Enumerable.getByCode(ResultCode.class, 0);
        assertEquals(resultCode, ResultCode.SUCCESS);

        final ResultCode nullResultCode = Enumerable.getByCode(ResultCode.class, -1);
        assertNull(nullResultCode);
    }

    @Test
    void getName() {
        final ResultCode resultCode = Enumerable.getByName(ResultCode.class, "SUCCESS");
        assertEquals(resultCode, ResultCode.SUCCESS);

        final ResultCode nullResultCode = Enumerable.getByName(ResultCode.class, "NULL");
        assertNull(nullResultCode);
    }
}
