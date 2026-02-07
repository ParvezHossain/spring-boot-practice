package com.parvez.spring_jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class EmailEvent implements Serializable {
    private String to;
    private String subject;
    private String body;
}
