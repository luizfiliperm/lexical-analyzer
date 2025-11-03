package com.compiler.syntax;

import com.compiler.lexical.Scanner;
import com.compiler.lexical.exception.LexicalErrorException;
import com.compiler.lexical.token.Token;
import com.compiler.lexical.token.TokenType;
import com.compiler.syntax.exception.SyntaxErrorException;

public class Parser {

    private final Scanner scanner;
    private Token currentToken;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public void parse() {
        advance();
        parsePrograma();

        if (!isEOF()) {
            throw new SyntaxErrorException("Erro sintático: código encontrado após o fim do programa na linha " +
                    currentToken.getLine() + ", coluna " + currentToken.getStartColumn());
        }
    }

    private void parsePrograma() {
        match(TokenType.MAIN);
        match(TokenType.LEFT_BRACE);
        parseCorpo();
        match(TokenType.RIGHT_BRACE);
    }

    private void parseCorpo() {
        if (currentToken != null && currentToken.getType() == TokenType.VAR) {
            parseSecaoDeclaracoes();
        }
        parseListaComandos();
    }

    private void parseSecaoDeclaracoes() {
        match(TokenType.VAR);
        match(TokenType.LEFT_BRACE);
        parseListaDeclaracoes();
        match(TokenType.RIGHT_BRACE);
    }

    private void parseListaDeclaracoes() {
        parseDeclaracao();
        while (currentToken != null && currentToken.getType() == TokenType.IDENTIFIER) {
            parseDeclaracao();
        }
    }

    // declaracao : ID ':' tipo ';'
    private void parseDeclaracao() {
        match(TokenType.IDENTIFIER);
        match(TokenType.COLON);
        parseTipo();
        match(TokenType.SEMICOLON);
    }

    private void parseTipo() {
        if (currentToken != null && currentToken.getType().isType()) {
            advance();
        } else {
            throw createError("tipo ('int' ou 'real')");
        }
    }

    private void parseListaComandos() {
        parseComando();
        while (currentToken != null && isFirstOfComando()) {
            parseComando();
        }
    }

    private void parseComando() {
        if (currentToken == null) {
            throw createError("comando");
        }
        switch (currentToken.getType()) {
            case IDENTIFIER -> parseAtribuicao();
            case INPUT -> parseLeitura();
            case PRINT -> parseEscrita();
            case IF -> parseCondicional();
            case WHILE -> parseRepeticao();
            case LEFT_BRACE -> parseBloco();
            default -> throw createError("início de comando (ID, input, print, if, while, {)");
        }
    }

    private void parseAtribuicao() {
        match(TokenType.IDENTIFIER);
        match(TokenType.ASSIGN);
        parseExpressaoAritmetica();
        match(TokenType.SEMICOLON);
    }

    private void parseLeitura() {
        match(TokenType.INPUT);
        match(TokenType.LEFT_PAREN);
        match(TokenType.IDENTIFIER);
        match(TokenType.RIGHT_PAREN);
        match(TokenType.SEMICOLON);
    }

    private void parseEscrita() {
        match(TokenType.PRINT);
        match(TokenType.LEFT_PAREN);

        if (currentToken != null && (currentToken.getType() == TokenType.IDENTIFIER || currentToken.getType() == TokenType.STRING_LITERAL)) {
            advance();
        } else {
            throw createError("ID ou CADEIA (string literal)");
        }

        match(TokenType.RIGHT_PAREN);
        match(TokenType.SEMICOLON);
    }

    private void parseCondicional() {
        match(TokenType.IF);
        parseExpressaoRelacional();
        match(TokenType.THEN);
        parseComando();

        if (currentToken != null && currentToken.getType() == TokenType.ELSE) {
            advance();
            parseComando();
        }
    }

    private void parseRepeticao() {
        match(TokenType.WHILE);
        parseExpressaoRelacional();
        parseComando();
    }

    private void parseBloco() {
        match(TokenType.LEFT_BRACE);
        parseListaComandos();
        match(TokenType.RIGHT_BRACE);
    }

    private void parseExpressaoAritmetica() {
        parseTermo();
        parseExpressaoAritmeticaLinha();
    }

    private void parseExpressaoAritmeticaLinha() {
        while (currentToken != null && (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS)) {
            advance();
            parseTermo();
        }
    }

    private void parseTermo() {
        parseFator();
        parseTermoLinha();
    }

    private void parseTermoLinha() {
        while (currentToken != null && (currentToken.getType() == TokenType.TIMES || currentToken.getType() == TokenType.DIVIDE)) {
            advance();
            parseFator();
        }
    }

    private void parseFator() {
        if (currentToken == null) {
            throw createError("fator (numero, ID ou (expressao))");
        }
        switch (currentToken.getType()) {
            case NUMINT, NUMREAL -> advance();
            case IDENTIFIER -> {
                advance();
                if (currentToken != null && (currentToken.getType() == TokenType.INCREMENT || currentToken.getType() == TokenType.DECREMENT)) {
                    advance();
                }
            }
            case LEFT_PAREN -> {
                advance();
                parseExpressaoAritmetica();
                match(TokenType.RIGHT_PAREN);
            }
            default -> throw createError("fator (numero, ID ou (expressao))");
        }
    }

    private void parseExpressaoRelacional() {
        parseTermoRelacional();
        parseExpressaoRelacionalLinha();
    }

    private void parseExpressaoRelacionalLinha() {
        while (currentToken != null && isFirstOfOperadorLogico()) {
            parseOperadorLogico();
            parseTermoRelacional();
        }
    }

    private void parseTermoRelacional() {
        if (currentToken != null && currentToken.getType() == TokenType.LEFT_PAREN) {
            advance();
            parseExpressaoRelacional();
            match(TokenType.RIGHT_PAREN);
        } else {
            parseExpressaoAritmetica();
            parseOperadorRelacional();
            parseExpressaoAritmetica();
        }
    }

    private void parseOperadorLogico() {
        if (currentToken != null && isFirstOfOperadorLogico()) {
            advance();
        } else {
            throw createError("operador lógico ('E', 'OU', 'NAO')");
        }
    }

    private void match(TokenType expected) {
        if (isEOF()) {
            throw new SyntaxErrorException("Erro sintático: esperado " + expected + " mas encontrou fim de arquivo (EOF).");
        }
        if (currentToken.getType() == expected) {
            advance();
        } else {
            throw createError(expected.toString());
        }
    }

    private void advance() {
        try {
            currentToken = scanner.nextToken();
        } catch (LexicalErrorException e) {
            throw new SyntaxErrorException("Erro Léxico: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado no scanner: " + e.getMessage(), e);
        }
    }

    private boolean isFirstOfComando() {
        if (isEOF()) return false;
        TokenType type = currentToken.getType();
        return type == TokenType.IDENTIFIER || type == TokenType.INPUT ||
               type == TokenType.PRINT || type == TokenType.IF ||
               type == TokenType.WHILE || type == TokenType.LEFT_BRACE;
    }

    private boolean isFirstOfOperadorLogico() {
        if (isEOF()) return false;
        TokenType type = currentToken.getType();
        return type == TokenType.LOGICAL_AND || type == TokenType.LOGICAL_OR || type == TokenType.LOGICAL_NOT;
    }

    private void parseOperadorRelacional() {
        if (currentToken != null && currentToken.getType().isRelationalOperator()) {
            advance();
        } else {
            throw createError("operador relacional (>, <, >=, <=, ==, !=)");
        }
    }
    
    private boolean isEOF() {
        return currentToken == null;
    }

    private SyntaxErrorException createError(String expected) {
        if (isEOF()) {
            return new SyntaxErrorException("Erro sintático: esperado " + expected + " mas encontrou fim de arquivo (EOF).");
        }
        return new SyntaxErrorException(
                "Erro sintático: esperado " + expected +
                        " mas encontrado " + currentToken.getType() +
                        " ('" + currentToken.getContent() + "') na linha " +
                        currentToken.getLine() + ", coluna " + currentToken.getStartColumn()
        );
    }
}