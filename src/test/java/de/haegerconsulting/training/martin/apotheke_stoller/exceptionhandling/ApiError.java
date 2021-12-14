package de.haegerconsulting.training.martin.apotheke_stoller.exceptionhandling;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data //adds getter/setter/equals etc.
public class ApiError{ //class needed for custom Error exception handling, which is needed fot testing if the @Min annotation for the request param works
    private String message;
    private HttpStatus status;
}
