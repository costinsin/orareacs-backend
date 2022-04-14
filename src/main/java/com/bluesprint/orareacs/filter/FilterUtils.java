package com.bluesprint.orareacs.filter;

import java.util.List;

public class FilterUtils {
    public static final String SECRET = System.getenv("SECRET");
    public static final String ROLES = "roles";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static long ACCESS_TIME_MILIS = 20 * 60 * 1000;
    public static long REFRESH_TIME_MILIS = 50 * 60 * 1000;
    public static List<String> PUBLIC_URLS = List.of("/api/register", "/api", "/api/login");
}
