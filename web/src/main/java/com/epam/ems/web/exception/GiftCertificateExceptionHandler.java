package com.epam.ems.web.exception;

import com.epam.ems.service.exception.DuplicateEntityException;
import com.epam.ems.service.exception.NoSuchEntityException;
import com.epam.ems.web.hateoas.Hateoas;
import com.epam.ems.web.hateoas.exception.HateoasException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GiftCertificateExceptionHandler extends ResponseEntityExceptionHandler  {

    private static final String BUNDLE_NAME = "locale";

    private static final String MESSAGE_NOT_FOUND = "msg.error.not.found";
    private static final String MESSAGE_DUPLICATE = "msg.error.duplicate";
    private static final String MESSAGE_HATEOAS = "msg.error.hateoas";
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
                getLocalizedMessage(MESSAGE_DUPLICATE, locale)+"(id="+e.getId()+")");
    }

    @ExceptionHandler(HateoasException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleHateoasException(HateoasException e, WebRequest request){
        Locale locale = request.getLocale();
        return new ApiErrorResponse(400,getLocalizedMessage(MESSAGE_HATEOAS, locale));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale locale = request.getLocale();

        List<String> violationMessages= ex.getFieldErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        StringBuilder errorMessage=new StringBuilder();
        errorMessage.append(getLocalizedMessage(MESSAGE_INVALID_ENTITY, locale)).append(":");

        violationMessages.forEach(o-> errorMessage.append(" ").append(getLocalizedMessage(o, locale)).append(";"));

        return new ResponseEntity<>(
                new ApiErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        errorMessage.toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse pathVariableValidationFail(ConstraintViolationException e, WebRequest request){
        Locale locale = request.getLocale();

        List<String> violationMessages= e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        StringBuilder errorMessage=new StringBuilder();
        errorMessage.append(getLocalizedMessage(MESSAGE_INVALID_ENTITY, locale)).append(":");

        violationMessages.forEach(o-> errorMessage.append(" ").append(getLocalizedMessage(o, locale)).append(";"));

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage.toString());
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
