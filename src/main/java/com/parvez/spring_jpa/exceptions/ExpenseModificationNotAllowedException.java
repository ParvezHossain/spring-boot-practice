package com.parvez.spring_jpa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExpenseModificationNotAllowedException extends RuntimeException {
    public ExpenseModificationNotAllowedException(String message) {
        super(message);
    }
}
