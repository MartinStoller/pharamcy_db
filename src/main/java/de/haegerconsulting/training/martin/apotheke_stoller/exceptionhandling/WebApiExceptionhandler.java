package de.haegerconsulting.training.martin.apotheke_stoller.exceptionhandling;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;


@ControllerAdvice //allows the Exceptionhandler to be shared with @controller classes
public class WebApiExceptionhandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,
                                                            WebRequest request) {

        ApiError apiError = new ApiError();
        apiError.setMessage("Validation error");
        apiError.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}
