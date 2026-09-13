package com.ai.rag.util;

public final class TextUtils {

    private TextUtils() {
        // Utility class
    }

    public static String cleanText(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replace("\t", " ")
                .replaceAll("[ ]{2,}", " ")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }

    public static boolean isEmpty(String text) {

        return text == null || text.trim().isEmpty();
    }

    public static boolean isNotEmpty(String text) {

        return !isEmpty(text);
    }

    public static String normalizeWhitespace(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static String truncate(String text, int maxLength) {

        if (text == null) {
            return "";
        }

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength) + "...";
    }
}