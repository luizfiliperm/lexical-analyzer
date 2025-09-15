package com.compiler.lexical;

import com.compiler.lexical.exception.LexicalErrorException;
import com.compiler.lexical.token.ReservedWords;
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

        if (current == '>') {
            nextChar();
            if (peekChar() == '=') {
                nextChar();
                return new Token(TokenType.GREATER_EQUAL, ">=", this.line, this.column);
            }
            return new Token(TokenType.GREATER_THAN, ">", this.line, this.column);
        }

        if (current == '<') {
            nextChar();
            if (peekChar() == '=') {
                nextChar();
                return new Token(TokenType.LESS_EQUAL, "<=", this.line, this.column);
            }
            return new Token(TokenType.LESS_THAN, "<", this.line, this.column);
        }

        if (current == '=') {
            nextChar();
            if (peekChar() == '=') {
                nextChar();
                return new Token(TokenType.EQUALS, "==", this.line, this.column);
            }
            return new Token(TokenType.ASSIGN, "=", this.line, this.column);
        }

        if (current == '!') {
            nextChar();
            if (peekChar() == '=') {
                nextChar();
                return new Token(TokenType.NOT_EQUAL, "!=", this.line, this.column);
            }
            throw new LexicalErrorException("Token inesperado: '!' na linha " + this.line + " coluna " + this.column
                    + ". O '!' deve ser seguido por '='.");
        }

        if (Character.isLetter(current) || CharUtils.isUnderLine(current)) {
            return readIdentifier();
        }

        TokenType tokenType = TokenDefinitions.getTokenType(current);
        if (tokenType != null) {
            String content = String.valueOf(current);
            nextChar();
            return new Token(tokenType, content, this.line, this.column);
        }
        if (CharUtils.isDigit(current) || current == '.') {
            return readNumber();
        }

        throw new LexicalErrorException(
                "Token inesperado: '" + nextChar() + "' na linha " + this.line + " coluna " + this.column);
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

        throw new LexicalErrorException(
                "Comentário multi-linha não fechado na linha " + this.line + " coluna " + this.column);
    }

    private Token readIdentifier() {
        StringBuilder content = new StringBuilder();
        int startLine = this.line;
        int startColumn = this.column;

        do {
            content.append(nextChar());
        } while (!isEoF() && (CharUtils.isLetter(peekChar()) || CharUtils.isDigit(peekChar())
                || CharUtils.isUnderLine(peekChar())));

        String word = content.toString();
        TokenType reservedType = ReservedWords.getTokenType(word);

        if (reservedType != null) {
            return new Token(reservedType, word, startLine, startColumn);
        }

        return new Token(TokenType.IDENTIFIER, word, startLine, startColumn);
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
        if (isEoF())
            return '\0';
        return sourceCode[position];
    }

    private char peekNextChar() {
        if (this.position + 1 >= this.sourceCode.length)
            return '\0';
        return this.sourceCode[position + 1];
    }

    private boolean isEoF() {
        return position >= this.sourceCode.length;
    }

    private Token readNumber() {
        StringBuilder content = new StringBuilder();
        int startLine = this.line;
        int startColumn = this.column;
        boolean hasDigitsBeforeDot = false;
        boolean hasDot = false;

        while (CharUtils.isDigit(peekChar())) {
            hasDigitsBeforeDot = true;
            content.append(nextChar());
        }

        if (peekChar() == '.') {
            hasDot = true;
            content.append(nextChar());

            if (!CharUtils.isDigit(peekChar())) {
                throw new LexicalErrorException("Número inválido na linha " + line + " coluna " + column);
            }

            while (CharUtils.isDigit(peekChar())) {
                content.append(nextChar());
            }
        }

        if (!hasDigitsBeforeDot && !hasDot) {
            throw new LexicalErrorException("Número inválido na linha " + line + " coluna " + column);
        }

        return new Token(TokenType.NUMBER, content.toString(), startLine, startColumn);
    }

}
