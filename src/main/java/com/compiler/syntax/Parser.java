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
        System.out.println("Iniciando análise sintática (fase 1 - leitura de tokens)...");

        do {
            currentToken = scanner.nextToken();
            if (currentToken != null) {
                System.out.println("[Parser] Token lido: " + currentToken);
            }
        } while (currentToken != null);

        System.out.println("Arquivo compilado com sucesso.");
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
            throw new SyntaxErrorException("Esperado token " + expected + " mas encontrado EOF (fim do arquivo)");
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
