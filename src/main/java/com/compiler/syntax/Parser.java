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
        System.out.println("Corpo do programa encontrado (validação detalhada será implementada depois)");

        // Por enquanto, apenas consome todos os tokens até encontrar a chave de fechamento (Vou remover esse comentário depois, é só pra acompanhar por hora)
        while (!isEOF() && currentToken().getType() != TokenType.RIGHT_BRACE) {
            advance();
        }
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
            throw new SyntaxErrorException("Erro sintático: esperado token " + expected + " mas encontrado EOF (fim do arquivo)");
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
