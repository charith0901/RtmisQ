package com.example.rtmisq.StaticData;

import java.util.jar.Attributes;

public class CurrentUser {
    private static String  LoggedUserName;
    private static long time;
    private static String UserFirstName;
    private static String UserLanguage;


    public static String getLoggedUserName() {
        return LoggedUserName;
    }

    public static void setLoggedUserName(String loggedUserName) {
        LoggedUserName = loggedUserName;
    }

    public static long getTime() {
        return time;
    }

    public static void setTime(long time) {
        CurrentUser.time = time;
    }

    public static String getUserFirstName() {
        return UserFirstName;
    }

    public static void setUserFirstName(String userFirstName) {
        UserFirstName = userFirstName;
    }

    public static String getUserLanguage() {
        return UserLanguage;
    }

    public static void setUserLanguage(String userLanguage) {
        UserLanguage = userLanguage;
    }
}
