package com.chensoul.spring.boot.web.webmvc;

import com.chensoul.exception.BadRequestException;
import com.chensoul.exception.InvalidInputException;
import com.chensoul.exception.NotFoundException;
import com.chensoul.exception.RequestNotPermittedException;
import com.chensoul.util.R;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
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
 * @since 1.0.0
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

    @ExceptionHandler({IllegalArgumentException.class})
    public R<String> handlerException(final HttpServletRequest request, final IllegalArgumentException ex) {
        return createHttpErrorInfo(HttpStatus.OK, request, ex, ex.getMessage());
    }

    @ExceptionHandler(RequestNotPermittedException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public R<String> handlerException(final HttpServletRequest request, final RequestNotPermittedException ex) {
        return createHttpErrorInfo(HttpStatus.TOO_MANY_REQUESTS, request, ex, "请求限流");
    }

    @ExceptionHandler(Exception.class)
    public R<String> handlerException(final HttpServletRequest request, final Exception ex) {
        return createHttpErrorInfo(HttpStatus.OK, request, ex, "系统异常");
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody R<String> handleBadRequestExceptions(final HttpServletRequest request, BadRequestException ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex, "请求参数错误");
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody R<String> handleInvalidInputException(
        HttpServletRequest request, InvalidInputException ex) {

        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex, "请求参数错误");
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody R<String> handleNotFoundExceptions(
        HttpServletRequest request, NotFoundException ex) {

        return createHttpErrorInfo(NOT_FOUND, request, ex, "资源不存在");
    }

    private R<String> createHttpErrorInfo(HttpStatus httpStatus, HttpServletRequest request, Exception ex, String localMessage) {
        final String path = request.getRequestURI();
        final String message = ex.getMessage();

        log.error("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return R.error(localMessage);
    }
}
