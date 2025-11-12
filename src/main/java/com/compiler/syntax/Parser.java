package com.compiler.syntax;

import com.compiler.lexical.Scanner;
import com.compiler.lexical.exception.LexicalErrorException;
import com.compiler.lexical.token.Token;
import com.compiler.lexical.token.TokenType;
import com.compiler.syntax.exception.SyntaxErrorException;

import java.io.IOException;

public class Parser {

    private final Scanner scanner;
    private Token currentToken;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public void parse() throws IOException {
        advance();

        while (!isEOF()) {
            parseProgram();
        }
    }

    private void parseProgram() {
        match(TokenType.IDENTIFIER); // main
        match(TokenType.LEFT_BRACE); // {

        parseBody();

        match(TokenType.RIGHT_BRACE); // }

        if (!isEOF()) {
            Token next = currentToken();
            if (next.getType() != TokenType.IDENTIFIER) {
                throw new SyntaxErrorException(
                        "Erro sintático: esperado fim de arquivo ou novo programa (IDENTIFIER), " +
                                "mas encontrado " + next.getType() +
                                " ('" + next.getContent() + "') na linha " +
                                next.getLine() + ", coluna " + next.getStartColumn()
                );
            }
        }
    }

    private void parseBody() {
        parseDeclarationSection();
    }

    private void parseDeclarationSection() {
        match(TokenType.VAR); // var
        match(TokenType.LEFT_BRACE); // {
        parseDeclarationList();
        match(TokenType.RIGHT_BRACE); // }
    }

    private void parseDeclarationList() {
        do {
            parseDeclaration();
        } while (currentToken() != null && currentToken().getType() == TokenType.IDENTIFIER);
    }

    private void parseDeclaration() {
        match(TokenType.IDENTIFIER); // x
        match(TokenType.COLON); // :
        parseType(); // int
        match(TokenType.SEMICOLON); // ;
    }

    private void parseType() {
        TokenType type = currentToken().getType();
        if (type.isType()) {
            advance();
        } else {
            throw new SyntaxErrorException(
                    "Erro sintático: tipo inválido '" + currentToken().getContent() +
                            "' na linha " + currentToken().getLine() +
                            ", coluna " + currentToken.getStartColumn()
            );
        }
    }


    private void parseCommandList() {
        // Implementação futura
    }

    private void parseCommand() {
        // Implementação futura
    }

    private void parseAssignment() {
        // Implementação futura
    }

    private void parseWrite() {
        // Implementação futura
    }

    private void parseConditional() {
        // Implementação futura
    }

    private void parseLoop() {
        // Implementação futura
    }

    private void parseBlock() {
        // Implementação futura
    }

    private void parseArithmeticExpression() {
        // Implementação futura
    }

    private void parseRelationalExpression() {
        // Implementação futura
    }

    private void advance() {
        try {
            currentToken = scanner.nextToken();
        } catch (LexicalErrorException e) {
            throw new SyntaxErrorException("Erro léxico: " + e.getMessage());
        }
    }

    private Token currentToken() {
        return currentToken;
    }

    private void match(TokenType expected) {
        if (currentToken == null) {
            throw new SyntaxErrorException("Erro sintático: esperado token " + expected + " mas encontrado EOF");
        }

        if (currentToken.getType() == expected) {
            advance();
        } else {
            throw new SyntaxErrorException(
                    "Erro sintático: esperado token " + expected +
                            " mas encontrado " + currentToken.getType() +
                            " ('" + currentToken.getContent() + "') na linha " +
                            currentToken.getLine() + ", coluna " + currentToken.getStartColumn()
            );
        }
    }

    private boolean isEOF() {
        return currentToken == null;
    }
}
