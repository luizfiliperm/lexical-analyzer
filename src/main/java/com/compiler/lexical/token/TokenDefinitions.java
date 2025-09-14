package com.compiler.lexical.token;

import java.util.HashMap;
import java.util.Map;

public final class TokenDefinitions {

    private static final Map<Character, TokenType> SINGLE_CHAR_TOKENS;

    static {
        SINGLE_CHAR_TOKENS = new HashMap<>();
        SINGLE_CHAR_TOKENS.put('+', TokenType.PLUS);
        SINGLE_CHAR_TOKENS.put('-', TokenType.MINUS);
        SINGLE_CHAR_TOKENS.put('*', TokenType.TIMES);
        SINGLE_CHAR_TOKENS.put('/', TokenType.DIVIDE);
        SINGLE_CHAR_TOKENS.put('=', TokenType.ASSIGN);
        SINGLE_CHAR_TOKENS.put('(', TokenType.LEFT_PAREN);
        SINGLE_CHAR_TOKENS.put(')', TokenType.RIGHT_PAREN);
    }

    private TokenDefinitions() {}

    public static TokenType getTokenType(Character c) {
        return SINGLE_CHAR_TOKENS.get(c);
    }
}