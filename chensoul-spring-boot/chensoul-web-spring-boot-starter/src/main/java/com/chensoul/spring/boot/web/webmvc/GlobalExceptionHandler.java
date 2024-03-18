package com.chensoul.spring.boot.web.webmvc;

import com.chensoul.exception.BadRequestException;
import com.chensoul.exception.BusinessException;
import com.chensoul.exception.NotFoundException;
import com.chensoul.exception.RequestNotPermittedException;
import com.chensoul.util.R;
import com.chensoul.util.ResultCode;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Global Exception Handler
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> handlerException(final HttpServletRequest request, final SQLIntegrityConstraintViolationException ex) {
        String localMessage = "系统异常";
        if (ex.getMessage().contains("Duplicate entry")) {
            final String[] split = ex.getMessage().split(" ");
            localMessage = split[2] + "已存在";
        }

        return createHttpErrorInfo(HttpStatus.OK, request, ex, localMessage);
    }

    @ExceptionHandler(RequestNotPermittedException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public R<String> handlerException(final HttpServletRequest request, final RequestNotPermittedException ex) {
        return createHttpErrorInfo(HttpStatus.TOO_MANY_REQUESTS, request, ex, ResultCode.TOO_MANY_REQUESTS.getName());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody R<String> handleBadRequestExceptions(final HttpServletRequest request, BadRequestException ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex, ResultCode.BAD_REQUEST.getName());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody R<String> handleNotFoundExceptions(
        HttpServletRequest request, NotFoundException ex) {

        return createHttpErrorInfo(NOT_FOUND, request, ex, ResultCode.NOT_FOUND.getName());
    }

    @ExceptionHandler(BusinessException.class)
    public R<String> handlerBusinessException(final HttpServletRequest request, final BusinessException ex) {
        return createHttpErrorInfo(HttpStatus.OK, request, ex, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<String> handlerException(final HttpServletRequest request, final Exception ex) {
        return createHttpErrorInfo(HttpStatus.OK, request, ex, ResultCode.SYSTEM_ERROR.getName());
    }

    private R<String> createHttpErrorInfo(HttpStatus httpStatus, HttpServletRequest request, Exception ex, String localMessage) {
        final String path = request.getRequestURI();
        final String message = ex.getMessage();

        log.error("Visit {}, return http status: {}, message: {}, localMessage: {}", path, httpStatus.value(), message, localMessage);

        return R.error(localMessage);
    }
}
