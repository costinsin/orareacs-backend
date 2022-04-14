package com.bluesprint.orareacs.filter;

import java.util.List;

public class FilterUtils {
    public static final String SECRET = "?gCFnFP2r9=9@DT$Rj8}dgmy#q4zUpec%BHKT]8i,e#3/J[QiR/=YG;/2.Q3hYz-CkgUn=4" +
            "#H!4(:%H%yq=T(E]tAi5Rx;v4,mrX3xWLg/6V-#KJ[r(_%Fc4LA:GtmF)_@vi@&u!v@:jY{dBdZJq-Spd[=i38k5eP#+y?82ihp-N" +
            "Rxcq?RNX!2-w9G8jP$(m=edBuS{jLG@%6C?KTRS5bnGp8PC_)rPJbV**3Vim*9kUrx,#W%;c[Fd*8!Q)K9W)";
    public static final String ROLES = "roles";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static long ACCESS_TIME_MILIS = 20 * 60 * 1000;
    public static long REFRESH_TIME_MILIS = 50 * 60 * 1000;
    public static List<String> PUBLIC_URLS = List.of("/api/register", "/api", "/api/login");
}
