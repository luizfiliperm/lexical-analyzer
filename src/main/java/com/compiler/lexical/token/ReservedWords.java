package com.compiler.lexical.token;

import java.util.HashMap;
import java.util.Map;

public final class ReservedWords {

    private static final Map<String, TokenType> RESERVED_WORDS = new HashMap<>();

    static {
        RESERVED_WORDS.put("int", TokenType.INT);
        RESERVED_WORDS.put("float", TokenType.FLOAT);
        RESERVED_WORDS.put("print", TokenType.PRINT);
        RESERVED_WORDS.put("if", TokenType.IF);
        RESERVED_WORDS.put("else", TokenType.ELSE);
    }

    private ReservedWords() {}

    public static TokenType getTokenType(String word) {
        return RESERVED_WORDS.get(word);
    }
}
