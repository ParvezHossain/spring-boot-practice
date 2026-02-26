package com.parvez.spring_jpa.dto;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        boolean active
) {}
