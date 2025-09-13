package com.compiler.lexical.utils;

public class CharUtils {

    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isUnderLine(char c) {
        return c == '_';
    }

    public static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    public static boolean isWhitespace(char c) {
        return isLineBreak(c) || c == ' ' || c == '\t' || c == '\r';
    }

    public static boolean isLineBreak(char c) {
        return c == '\n';
    }
}
