package com.chensoul.spring.boot.web.webmvc;

import com.chensoul.exception.BadRequestException;
import com.chensoul.exception.BusinessException;
import com.chensoul.exception.NotFoundException;
import com.chensoul.exception.TooManyRequestException;
import com.chensoul.util.RestResponse;
import com.chensoul.exception.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.sql.BatchUpdateException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

/**
 * Global Exception Handler
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {
    // Process @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull final MethodArgumentNotValidException ex, @NonNull final HttpHeaders headers, @NonNull final HttpStatus status, @NonNull final WebRequest request) {
        logException(ex, status);

        String message = null;
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError field = (FieldError) error;
                message = field.getDefaultMessage();
                break;
            }
        }

        ProblemDetail problemDetail = ProblemDetail.build(ex, message);
        return ResponseEntity.status(BAD_REQUEST).body(RestResponse.error(ResultCode.BAD_REQUEST.getCode(), message, problemDetail));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logException(ex, status);

        String message = null;
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError field = (FieldError) error;
                message = field.getDefaultMessage();
                break;
            }
        }

        ProblemDetail problemDetail = ProblemDetail.build(ex, message);
        return ResponseEntity.status(BAD_REQUEST).body(RestResponse.error(ResultCode.BAD_REQUEST.getCode(), message, problemDetail));
    }

    // Process @Validated
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public RestResponse handleJakartaConstraintViolationException(final ConstraintViolationException e, final @NonNull HttpStatus status, final @NonNull WebRequest request) {
        logException(e, status);

        String message = null;
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            message = constraintViolation.getMessage();
        }
        ProblemDetail problemDetail = ProblemDetail.build(e, message);
        return RestResponse.error(ResultCode.BAD_REQUEST.getCode(), message, problemDetail);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, DuplicateKeyException.class, BatchUpdateException.class, PersistenceException.class})
    public RestResponse handlePersistenceException(final Exception e, final @NonNull HttpStatus status, final @NonNull WebRequest request) {
        logException(e, status);

        final Throwable cause = NestedExceptionUtils.getMostSpecificCause(e);

        String message = "系统异常";
        if (cause.getMessage().contains("Duplicate entry")) {
            final String[] split = cause.getMessage().split(" ");
            message = split[2] + "已存在";
        }

        ProblemDetail problemDetail = ProblemDetail.build(e, message);
        return RestResponse.error(ResultCode.BAD_REQUEST.getCode(), message, problemDetail);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public RestResponse handleAccessDeniedException(final Exception ex, final @NonNull HttpStatus status, final @NonNull WebRequest request) {
        logException(ex, status);
        ProblemDetail problemDetail = ProblemDetail.build(ex);
        return RestResponse.error(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getName(), problemDetail);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BusinessException.class})
    public RestResponse handleBusinessException(final BusinessException ex, final @NonNull HttpStatus status, final @NonNull WebRequest request) {
        logException(ex, status);

        ProblemDetail problemDetail = ProblemDetail.build(ex);
        return RestResponse.error(ResultCode.INTERNAL_ERROR.getCode(), ex.getMessage(), problemDetail);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public RestResponse handleException(final Exception ex, final @NonNull HttpStatus status, final @NonNull WebRequest request) {
        logException(ex, status);

        ProblemDetail problemDetail = ProblemDetail.build(ex);
        return RestResponse.error(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getName(), problemDetail);
    }

    @ExceptionHandler(TooManyRequestException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public RestResponse handlerException(final TooManyRequestException ex, final @NonNull HttpStatus status, final @NonNull WebRequest request) {
        logException(ex, status);
        ProblemDetail problemDetail = ProblemDetail.build(ex);
        return RestResponse.error(ResultCode.TOO_MANY_REQUESTS.getCode(), ResultCode.TOO_MANY_REQUESTS.getName(), problemDetail);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public RestResponse handleBadRequestExceptions(BadRequestException ex, final @NonNull HttpStatus status, final @NonNull WebRequest request) {
        logException(ex, status);
        ProblemDetail problemDetail = ProblemDetail.build(ex);
        return RestResponse.error(ResultCode.BAD_REQUEST.getCode(), ResultCode.TOO_MANY_REQUESTS.getName(), problemDetail);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public RestResponse handleNotFoundExceptions(NotFoundException ex, final @NonNull HttpStatus status, final @NonNull WebRequest request) {
        logException(ex, status);
        ProblemDetail problemDetail = ProblemDetail.build(ex);
        return RestResponse.error(ResultCode.BAD_REQUEST.getCode(), ResultCode.TOO_MANY_REQUESTS.getName(), problemDetail);
    }

    @Data
    @AllArgsConstructor
    public static class ProblemDetail {
        private String throwable;
        private String throwTime;
        private String message;
        private String traceId;

        public static ProblemDetail build(final Throwable e, String traceId) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            return new ProblemDetail(rootCause.getClass().getName(), LocalDateTime.now().toString(), rootCause.getMessage(), traceId);
        }

        public static ProblemDetail build(final Throwable e) {
            return build(e, null);
        }
    }

    /**
     * @param exception
     * @param status
     * @see <a href="https://github.com/jhipster/jhipster-lite/blob/main/src/main/java/tech/jhipster/lite/shared/error/infrastructure/primary/GeneratorErrorsHandler.java">GeneratorErrorsHandler.java</a>
     */
    private void logException(Throwable exception, HttpStatus status) {
        if (status.is4xxClientError()) {
            log.info(exception.getMessage(), exception);
        } else {
            log.error(exception.getMessage(), exception);
        }
    }
}
