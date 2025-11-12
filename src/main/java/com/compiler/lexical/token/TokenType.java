package com.compiler.lexical.token;

public enum TokenType {
    IDENTIFIER,
    PLUS,
    MINUS,
    TIMES,
    DIVIDE,
    ASSIGN,
    GREATER_THAN,
    GREATER_EQUAL,
    LESS_THAN,
    LESS_EQUAL,
    NOT_EQUAL,
    EQUALS,
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    COLON,
    SEMICOLON,
    NUMBER,
    INT,
    REAL,
    PRINT,
    IF,
    ELSE,
    VAR,
    MAIN,
    INPUT,
    THEN,
    WHILE,
    RETURN,
    FUNCTION,
    COMMA,
    EOF,
    NUMINT,
    NUMREAL,
    STRING_LITERAL,
    INCREMENT,
    DECREMENT,
    LOGICAL_AND,
    LOGICAL_OR, 
    LOGICAL_NOT;

    public boolean isType() {
        return this == TokenType.REAL || this == TokenType.INT;
    }

    public boolean isRelationalOperator() {
        return this == TokenType.GREATER_THAN || this == TokenType.GREATER_EQUAL ||
               this == TokenType.LESS_THAN || this == TokenType.LESS_EQUAL ||
               this == TokenType.EQUALS || this == TokenType.NOT_EQUAL;
    }
}
