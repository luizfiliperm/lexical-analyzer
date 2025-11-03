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

        int startColumn = this.column;
        if (current == '>') {
            nextChar();
            if (peekChar() == '=') {
                nextChar();
                return new Token(TokenType.GREATER_EQUAL, ">=", this.line, startColumn);
            }
            return new Token(TokenType.GREATER_THAN, ">", this.line, startColumn);
        }

        if (current == '<') {
            nextChar();
            if (peekChar() == '-') {
                nextChar();
                return new Token(TokenType.ASSIGN, "<-", this.line, startColumn);
            }
            if (peekChar() == '=') {
                nextChar();
                return new Token(TokenType.LESS_EQUAL, "<=", this.line, startColumn);
            }
            return new Token(TokenType.LESS_THAN, "<", this.line, startColumn);
        }

        if (current == '=') {
            nextChar();
            if (peekChar() == '=') {
                nextChar();
                return new Token(TokenType.EQUALS, "==", this.line, startColumn);
            }
            throw new LexicalErrorException("Token inesperado: '=' na linha " + this.line + " coluna " + startColumn + ". Operador de atribuição é '<-' e de igualdade é '=='.");
        }

        if (current == '!') {
            nextChar();
            if (peekChar() == '=') {
                nextChar();
                return new Token(TokenType.NOT_EQUAL, "!=", this.line, startColumn);
            }
            throw new LexicalErrorException("Token inesperado: '!' na linha " + this.line + " coluna " + startColumn + ". O '!' deve ser seguido por '='.");
        }

        if (current == '+') {
            nextChar();
            if (peekChar() == '+') {
                nextChar();
                return new Token(TokenType.INCREMENT, "++", this.line, startColumn);
            }
            return new Token(TokenType.PLUS, "+", this.line, startColumn);
        }

        if (current == '-') {
            nextChar();
            if (peekChar() == '-') {
                nextChar();
                return new Token(TokenType.DECREMENT, "--", this.line, startColumn);
            }
            return new Token(TokenType.MINUS, "-", this.line, startColumn);
        }

        if (current == '"') {
            return readStringLiteral(startColumn);
        }


        if (CharUtils.isLetter(current) || CharUtils.isUnderLine(current)) {
            return readIdentifier(startColumn);
        }

        if (CharUtils.isDigit(current) || (current == '.' && CharUtils.isDigit(peekNextChar()))) {
            return readNumber(startColumn);
        }

        TokenType tokenType = TokenDefinitions.getTokenType(current);
        if (tokenType != null) {
            String content = String.valueOf(current);
            nextChar();
            return new Token(tokenType, content, this.line, startColumn);
        }

        throw new LexicalErrorException("Token inesperado: '" + nextChar() + "' na linha " + this.line + " coluna " + startColumn);
    }

    private Token readStringLiteral(int startColumn) {
        nextChar();
        StringBuilder content = new StringBuilder();
        int startLine = this.line;

        while (!isEoF() && peekChar() != '"') {
            if (CharUtils.isLineBreak(peekChar())) {
                throw new LexicalErrorException("String literal (CADEIA) não fechada na linha " + startLine);
            }
            content.append(nextChar());
        }

        if (isEoF()) {
            throw new LexicalErrorException("String literal (CADEIA) não fechada na linha " + startLine);
        }

        nextChar();
        return new Token(TokenType.STRING_LITERAL, content.toString(), startLine, startColumn);
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

    private Token readIdentifier(int startColumn) {
        StringBuilder content = new StringBuilder();
        int startLine = this.line;

        do {
            content.append(nextChar());
        } while (!isEoF() && (CharUtils.isLetter(peekChar()) || CharUtils.isDigit(peekChar()) || CharUtils.isUnderLine(peekChar())));

        String word = content.toString();
        TokenType reservedType = TokenDefinitions.getTokenType(word);

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
        if (isEoF()) return '\0';
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

    private Token readNumber(int startColumn) {
        StringBuilder content = new StringBuilder();
        int startLine = this.line;
        boolean hasDot = false;

        while (CharUtils.isDigit(peekChar())) {
            content.append(nextChar());
        }

        if (peekChar() == '.') {
            hasDot = true;
            content.append(nextChar());

            if (!CharUtils.isDigit(peekChar())) {
                throw new LexicalErrorException("Número inválido: caractere inesperado '" + peekChar() + "' após '.' na linha " + line + " coluna " + column);
            }

            while (CharUtils.isDigit(peekChar())) {
                content.append(nextChar());
            }
        }

        if (peekChar() == '.') {
            throw new LexicalErrorException("Número inválido: múltiplos pontos detectados, caractere '" + peekChar() + "' na linha " + line + " coluna " + column);
        }
        
        if (hasDot) {
            return new Token(TokenType.NUMREAL, content.toString(), startLine, startColumn);
        } else {
            return new Token(TokenType.NUMINT, content.toString(), startLine, startColumn);
        }
    }

}
