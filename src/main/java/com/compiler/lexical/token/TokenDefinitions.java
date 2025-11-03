package com.compiler.lexical.token;

import java.util.HashMap;
import java.util.Map;

public final class TokenDefinitions {

    private static final Map<String, TokenType> RESERVED_WORDS = new HashMap<>();
    private static final Map<Character, TokenType> SINGLE_CHAR_TOKENS = new HashMap<>();

    static {
        RESERVED_WORDS.put("main", TokenType.MAIN);
        RESERVED_WORDS.put("var", TokenType.VAR);
        RESERVED_WORDS.put("int", TokenType.INT);
        RESERVED_WORDS.put("real", TokenType.REAL);
        RESERVED_WORDS.put("input", TokenType.INPUT);
        RESERVED_WORDS.put("print", TokenType.PRINT);
        RESERVED_WORDS.put("if", TokenType.IF);
        RESERVED_WORDS.put("then", TokenType.THEN);
        RESERVED_WORDS.put("else", TokenType.ELSE);
        RESERVED_WORDS.put("while", TokenType.WHILE);
        RESERVED_WORDS.put("E", TokenType.LOGICAL_AND);
        RESERVED_WORDS.put("OU", TokenType.LOGICAL_OR);
        RESERVED_WORDS.put("NAO", TokenType.LOGICAL_NOT);
        SINGLE_CHAR_TOKENS.put('*', TokenType.TIMES);
        SINGLE_CHAR_TOKENS.put('/', TokenType.DIVIDE);
        SINGLE_CHAR_TOKENS.put('(', TokenType.LEFT_PAREN);
        SINGLE_CHAR_TOKENS.put(')', TokenType.RIGHT_PAREN);
        SINGLE_CHAR_TOKENS.put('{', TokenType.LEFT_BRACE);
        SINGLE_CHAR_TOKENS.put('}', TokenType.RIGHT_BRACE);
        SINGLE_CHAR_TOKENS.put(':', TokenType.COLON);
        SINGLE_CHAR_TOKENS.put(';', TokenType.SEMICOLON);
    }

    private TokenDefinitions() {}

    public static TokenType getTokenType(String word) {
        return RESERVED_WORDS.get(word);
    }

    public static TokenType getTokenType(Character c) {
        return SINGLE_CHAR_TOKENS.get(c);
    }
}
