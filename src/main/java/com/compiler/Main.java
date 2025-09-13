package com.compiler;

import com.compiler.lexical.Scanner;
import com.compiler.lexical.token.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("""
                Grupo compostor por:
                - João Pedro Soares Figueiredo
                - João Victor Nunes
                - Luiz Filipe de Matos Ramos
                """);

        List<Token> tokens = new ArrayList<>();

        Scanner scanner = new Scanner("main.jjl");
        Token token;

        do {
            token = scanner.nextToken();
            if (token != null) {
                tokens.add(token);
            }
        } while (token != null);

        tokens.forEach(System.out::println);
    }
}
