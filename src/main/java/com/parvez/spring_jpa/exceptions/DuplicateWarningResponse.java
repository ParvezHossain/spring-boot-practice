package com.parvez.spring_jpa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateWarningResponse {
    public DuplicateWarningResponse(String message) {
        super();
    }
}
