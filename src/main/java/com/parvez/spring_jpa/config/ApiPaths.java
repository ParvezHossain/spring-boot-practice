package com.parvez.spring_jpa.config;

public final class ApiPaths {

    private ApiPaths() {
    }

    /* ================= BASE ================= */
    public static final String API_BASE = "/api/v1";

    /* ================= AUTH ================= */
    public static final String AUTH = API_BASE + "/auth/**";
    public static final String LOGIN = API_BASE + "/auth/login";
    public static final String REGISTER = API_BASE + "/auth/register";
    public static final String FORGOT_PASSWORD = API_BASE + "/auth/forgot-password";
    public static final String RESET_PASSWORD = API_BASE + "/auth/reset-password";

    /* ================= HOME ================= */
    public static final String HOME = API_BASE + "/home";

    /* ================= CATEGORY ================= */
    public static final String CATEGORIES = API_BASE + "/categories/**";

    /* ================= EXPENSE ================= */
    public static final String EXPENSES = API_BASE + "/expenses/**";

    /* ================= EMPLOYEE ================= */
    public static final String EMPLOYEES = API_BASE + "/employees/**";
    public static final String SALARY_INCREMENT = API_BASE + "/employees/*/salary/increment";
}