package com.compiler;

import java.io.IOException;

import com.compiler.lexical.Scanner;
import com.compiler.lexical.exception.LexicalErrorException;
import com.compiler.syntax.Parser;
import com.compiler.syntax.exception.SyntaxErrorException;

public class Main {
    public static void main(String[] args) {

        System.out.println("""
                Grupo composto por:
                - João Pedro Soares Figueiredo
                - João Victor Nunes
                - Luiz Filipe de Matos Ramos
                """);

        String filename = "main.jjl";
        if (args.length > 0) {
            filename = args[0];
            System.out.println("Compilando arquivo: " + filename);
        } else {
            System.out.println("Nenhum arquivo fornecido. Usando padrão: " + filename);
        }

        try {
            Scanner scanner = new Scanner(filename);
            Parser parser = new Parser(scanner);
            parser.parse();
            System.out.println("Programa compilado com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro de IO: Não foi possível ler o arquivo '" + filename + "'.");
            System.err.println(e.getMessage());
        } catch (LexicalErrorException | SyntaxErrorException e) {
            System.err.println("Falha na compilação:");
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado:");
            System.err.println(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                System.err.println("\tat " + ste);
            }
        }
    }
}