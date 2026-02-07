package com.parvez.spring_jpa.config;

public final class ApiPaths {

    private ApiPaths() {
    }

    public static final String API_ENTRY_PATH = "/api";

    public static final String EMPLOYEES = STR."\{API_ENTRY_PATH}/employees";
    public static final String AUTH = STR."\{API_ENTRY_PATH}/auth";

    // Auth endpoints
    public static final String LOGIN = STR."\{AUTH}/login";
    public static final String REGISTER = STR."\{AUTH}/register";
    public static final String LOGOUT = STR."\{AUTH}/logout";
    public static final String REFRESH = STR."\{AUTH}/refresh";
    public static final String LOGOUT_ALL = STR."\{AUTH}/logout-all";
    public static final String FORGOT_PASSWORD = STR."\{AUTH}/forgot-password";
    public static final String RESET_PASSWORD = STR."\{AUTH}/reset-password";

    // Employee endpoints
    public static final String SALARY_INCREMENT = STR."\{EMPLOYEES}/*/salary/increment";
    public static final String EMPLOYEE_READ = STR."\{EMPLOYEES}/**";
}
