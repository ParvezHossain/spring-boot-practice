package com.parvez.spring_jpa.config;

public final class ApiPaths {

    private ApiPaths() {}

    public static final String API_ENTRY_PATH = "/api";

    // Use standard + for concatenation to keep them as compile-time constants
    public static final String EMPLOYEES = API_ENTRY_PATH + "/employees";
    public static final String AUTH = API_ENTRY_PATH + "/auth";

    // Auth endpoints
    public static final String LOGIN = AUTH + "/login";
    public static final String REGISTER = AUTH + "/register";
    public static final String LOGOUT = AUTH + "/logout";
    public static final String REFRESH = AUTH + "/refresh";
    public static final String LOGOUT_ALL = AUTH + "/logout-all";
    public static final String FORGOT_PASSWORD = AUTH + "/forgot-password";
    public static final String RESET_PASSWORD = AUTH + "/reset-password";

    // Employee endpoints
    public static final String SALARY_INCREMENT = EMPLOYEES + "/*/salary/increment";
    public static final String EMPLOYEE_READ = EMPLOYEES + "/**";
}