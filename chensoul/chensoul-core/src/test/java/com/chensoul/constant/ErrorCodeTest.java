package com.chensoul.constant;

import com.chensoul.util.Enumerable;
import com.chensoul.exception.ErrorCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
class ErrorCodeTest {

    @Test
    void getCode() {
        final ErrorCode errorCode = Enumerable.getByCode(ErrorCode.class, 0);
        assertEquals(errorCode, ErrorCode.SUCCESS);

        final ErrorCode nullErrorCode = Enumerable.getByCode(ErrorCode.class, -1);
        assertNull(nullErrorCode);
    }

    @Test
    void getName() {
        final ErrorCode errorCode = Enumerable.getByName(ErrorCode.class, "SUCCESS");
        assertEquals(errorCode, ErrorCode.SUCCESS);

        final ErrorCode nullErrorCode = Enumerable.getByName(ErrorCode.class, "NULL");
        assertNull(nullErrorCode);
    }
}
