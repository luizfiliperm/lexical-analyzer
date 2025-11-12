package com.compiler;

import com.compiler.lexical.Scanner;
import com.compiler.syntax.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("""
                Grupo composto por:
                - João Pedro Soares Figueiredo
                - João Victor Nunes
                - Luiz Filipe de Matos Ramos
                """);

        Parser parser = new Parser(new Scanner("main.jjl"));
        parser.parse();
    }
}
