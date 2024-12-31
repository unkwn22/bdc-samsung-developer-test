package com.example.bdcsamsungdevelopertest.common.util;

public class StringUtilExtension {

    public static String subStringEmail(String email) {
        return email.substring(0, email.indexOf('@'));
    }

    public static boolean validateIfBothContentMatches(
        String requestContent,
        String targetContent
    ) {
        return requestContent.equals(targetContent);
    }

    public static String subStringDomainFromUri(
        String uri
    ) {
        int firstSlash = uri.indexOf("/");
        int secondSlash = uri.indexOf("/", firstSlash + 1);
        return uri.substring(0, secondSlash);
    }
}
