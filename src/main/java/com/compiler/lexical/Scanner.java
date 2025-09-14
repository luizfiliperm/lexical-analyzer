package com.compiler.lexical;

import com.compiler.lexical.exception.LexicalErrorException;
import com.compiler.lexical.token.Token;
import com.compiler.lexical.token.TokenType;
import com.compiler.lexical.token.TokenDefinitions;
import com.compiler.lexical.utils.CharUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Scanner {

    private final char[] sourceCode;
    private int position;
    private int line;
    private int column;

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

        if (peekChar() == '#') {
            ignoreSingleLineComment();
            return nextToken();
        }

        if (peekChar() == '/') {
            if (peekNextChar() == '*') {
                ignoreMultiLineComment();
                return nextToken();
            }
        }

        char current = peekChar();

        if (Character.isLetter(current) || CharUtils.isUnderLine(current)) {
            return readIdentifier();
        }

        TokenType tokenType = TokenDefinitions.getTokenType(current);
        if (tokenType != null) {
            String content = String.valueOf(current);
            nextChar();
            return new Token(tokenType, content, this.line, this.column);
        }

        throw new LexicalErrorException("Token inesperado: '" + nextChar() + "' na linha " + this.line + " coluna " + this.column);
    }

    private void ignoreMultiLineComment() throws LexicalErrorException {
        nextChar();
        nextChar();

        while (!isEoF()) {
            if (peekChar() == '*' && peekNextChar() == '/') {
                nextChar();
                nextChar();
                return;
            } else {
                nextChar();
            }
        }

        throw new LexicalErrorException("Comentário multi-linha não fechado na linha " + this.line + " coluna " + this.column);
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

    private void ignoreSingleLineComment() {
        do {
            nextChar();
        } while (!isEoF() && !CharUtils.isLineBreak(peekChar()));
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

    private char peekNextChar() {
        if (this.position + 1 >= this.sourceCode.length) return '\0';
        return this.sourceCode[position + 1];
    }

    private boolean isEoF() {
        return position >= this.sourceCode.length;
    }
}
