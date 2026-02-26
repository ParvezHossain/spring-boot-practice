package com.parvez.spring_jpa.exceptions;

public class DuplicateExpenseException extends RuntimeException {
    public DuplicateExpenseException(String message) {
        super(message);
    }
}

