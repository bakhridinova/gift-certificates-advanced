package com.epam.esm.exception;

import com.epam.esm.hateoas.HateoasAdder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

/**
 * controller advice handling exceptions
 *
 * @author bakhridinova
 */

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @NonNull NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        String errorMessage = "no handler found for "
                + ex.getHttpMethod() + " " + ex.getRequestURL();
        CustomMessageHolder messageHolder = new CustomMessageHolder(
                HttpStatus.NOT_FOUND, errorMessage);
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return new ResponseEntity<>(messageHolder, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            @NonNull HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        StringBuilder errorMessage = new StringBuilder(ex.getMethod());
        errorMessage.append(" method is not supported for this request. supported methods are: ");
        Objects.requireNonNull(ex.getSupportedHttpMethods())
                .forEach(method -> errorMessage.append(method).append(" "));
        CustomMessageHolder messageHolder = new CustomMessageHolder(
                HttpStatus.METHOD_NOT_ALLOWED, errorMessage.toString());
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return new ResponseEntity<>(messageHolder, headers, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomMessageHolder> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String errorMessage = ex.getName() + " should be of type "
                + Objects.requireNonNull(ex.getRequiredType()).getName();
        CustomMessageHolder messageHolder =
                new CustomMessageHolder(HttpStatus.BAD_REQUEST, errorMessage);
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return new ResponseEntity<>(messageHolder, messageHolder.getStatus());
    }

    @ExceptionHandler({
            CustomValidationException.class,
            CustomEntityNotFoundException.class,
            CustomEntityAlreadyExistsException.class})
    public ResponseEntity<CustomMessageHolder> handleCustomException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "something went wrong :(";

        if (ex instanceof CustomValidationException
                || ex instanceof CustomEntityAlreadyExistsException) {
            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        }

        if (ex instanceof CustomEntityNotFoundException){
            status = HttpStatus.NOT_FOUND;
            message = ex.getMessage();
        }

        CustomMessageHolder messageHolder = new CustomMessageHolder(status, message);
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return new ResponseEntity<>(messageHolder, messageHolder.getStatus());
    }
}