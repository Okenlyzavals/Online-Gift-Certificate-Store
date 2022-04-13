package com.epam.ems.web.exception;

import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.ResourceBundle;

@RestControllerAdvice
public class GiftCertificateExceptionHandler extends ResponseEntityExceptionHandler  {

    private static final String BUNDLE_NAME = "locale";

    private static final String MESSAGE_NOT_FOUND = "msg.error.not.found";
    private static final String MESSAGE_DUPLICATE = "msg.error.duplicate";
    private static final String MESSAGE_INTERNAL_ERROR = "msg.error.internal";
    private static final String MESSAGE_INVALID_ENTITY = "msg.error.invalid";
    private static final String MESSAGE_METHOD_NOT_SUPPORTED = "msg.error.unsupported.method";

    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse missingEntityHandler(NoSuchEntityException e, WebRequest request){
        Locale locale = request.getLocale();
        return new ApiErrorResponse(
                e.getErrorCode(),
                getLocalizedMessage(MESSAGE_NOT_FOUND, locale));
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse duplicateEntityHandler(DuplicateEntityException e, WebRequest request){
        Locale locale = request.getLocale();
        return new ApiErrorResponse(
                e.getErrorCode(),
                getLocalizedMessage(MESSAGE_DUPLICATE+"(id="+e.getId()+")", locale));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();

        return new ResponseEntity<>(
                new ApiErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        getLocalizedMessage(MESSAGE_INVALID_ENTITY, locale)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse validationFail(WebRequest request){
        Locale locale = request.getLocale();
        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                getLocalizedMessage(MESSAGE_INVALID_ENTITY, locale));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        HttpStatus.METHOD_NOT_ALLOWED.value(),
                        getLocalizedMessage(MESSAGE_METHOD_NOT_SUPPORTED, locale)),
                HttpStatus.METHOD_NOT_ALLOWED);

    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        getLocalizedMessage(MESSAGE_INTERNAL_ERROR, locale)),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        getLocalizedMessage(MESSAGE_NOT_FOUND, locale)),
                HttpStatus.NOT_FOUND);
    }

    private static String getLocalizedMessage(String messageKey, Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(messageKey);
    }
}
