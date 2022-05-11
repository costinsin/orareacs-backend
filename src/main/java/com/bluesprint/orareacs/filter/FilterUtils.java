package com.bluesprint.orareacs.filter;

import java.util.List;

public class FilterUtils {
    public static final String SECRET = System.getenv("SECRET");
    public static final String ROLES = "roles";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ROLE = "role";
    public static long ACCESS_TIME_MILLIS = 20 * 60 * 1000;
    public static long REFRESH_TIME_MILLIS = 7200 * 60 * 1000;
    public static List<String> PUBLIC_URLS = List.of("/api/register", "/api", "/api/login", "/api/refreshToken",
            "/api/checkCredentials", "/api/getGroups");
    public static final String TOKEN_HEADER = "Bearer ";
    public static final String DATABASE = "orareACS";
    public static final String COLLECTION = "user";
    public static final String USERNAME_FIELD = "username";
    public static final String EMAIL = "lolconturi@gmail.com";
    public static final String TWO_FACTOR_SECRET_FIELD = "twoFactorSecret";
    public static final String DB_CONNECT_URL = System.getenv("SPRING_DATA_MONGODB_URI");

    public static final String STUDENT_ROLE = "student";
    public static final String ADMIN_ROLE = "admin";
}
