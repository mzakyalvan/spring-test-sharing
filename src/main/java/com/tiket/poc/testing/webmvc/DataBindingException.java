package com.tiket.poc.testing.webmvc;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author zakyalvan
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataBindingException extends RuntimeException {
    private final Errors errors;

    public DataBindingException(Errors errors) {
        super("Web request data binding error");
        this.errors = errors;
    }
}
