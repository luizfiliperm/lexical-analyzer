package com.compiler.lexical;

import com.compiler.lexical.exception.LexicalErrorException;
import com.compiler.lexical.token.Token;
import com.compiler.lexical.token.TokenType;
import com.compiler.lexical.utils.CharUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Scanner {

    private final char[] sourceCode;
    private int position;
    private int line;
    private int column;

    private static final Map<Character, TokenType> SINGLE_CHAR_TOKENS;

    static {
        SINGLE_CHAR_TOKENS = new HashMap<>();
        SINGLE_CHAR_TOKENS.put('+', TokenType.PLUS);
        SINGLE_CHAR_TOKENS.put('-', TokenType.MINUS);
        SINGLE_CHAR_TOKENS.put('*', TokenType.TIMES);
        SINGLE_CHAR_TOKENS.put('/', TokenType.DIVIDE);
    }

    public Scanner(String filename) throws IOException {
        String content = Files.readString(Paths.get(filename));
        this.sourceCode = content.toCharArray();
        this.position = 0;
        this.line = 1;
        this.column = 1;
    }

    public Token nextToken() throws LexicalErrorException {
        ignoreWhiteSpaces();

        if (isEoF()) {
            return null;
        }

        char current = peekChar();

        if (Character.isLetter(current) || CharUtils.isUnderLine(current)) {
            return readIdentifier();
        }

        if (SINGLE_CHAR_TOKENS.containsKey(current)) {
            String content = String.valueOf(nextChar());
            TokenType type = SINGLE_CHAR_TOKENS.get(current);
            return new Token(type, content, this.line, this.column);
        }

        throw new LexicalErrorException("Token inesperado: '" + nextChar() + "' na linha " + this.line + " coluna " + this.column);
    }

    private Token readIdentifier() {
        StringBuilder content = new StringBuilder();
        int startLine = this.line;
        int startColumn = this.column;

        do {
            content.append(nextChar());
        } while (!isEoF() && (CharUtils.isLetter(peekChar()) || CharUtils.isDigit(peekChar()) || CharUtils.isUnderLine(peekChar())));

        return new Token(TokenType.IDENTIFIER, content.toString(), startLine, startColumn);
    }

    private void ignoreWhiteSpaces() {
        while (!isEoF() && CharUtils.isWhitespace(peekChar())) {
            nextChar();
        }
    }

    private char nextChar() {
        char c = this.sourceCode[this.position++];
        if (CharUtils.isLineBreak(c)) {
            this.line++;
            this.column = 1;
        } else {
            this.column++;
        }
        return c;
    }

    private char peekChar() {
        if (isEoF()) return '\0';
        return sourceCode[position];
    }

    private boolean isEoF() {
        return position >= this.sourceCode.length;
    }
}
