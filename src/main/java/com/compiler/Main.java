package com.compiler;

import java.io.IOException;

import com.compiler.lexical.Scanner;
import com.compiler.lexical.exception.LexicalErrorException;
import com.compiler.lexical.token.Token;


public class Main {
    public static void main(String[] args) {

        System.out.println("--- Testando Scanner (PR 2) ---");
        String filename = "main.jjl";

        try {
            Scanner scanner = new Scanner(filename);
            Token token;
            
            // Loop que pede tokens ao scanner até o fim do arquivo (null)
            System.out.println("Iniciando análise léxica...");
            while ( (token = scanner.nextToken()) != null ) {
                
                // Imprime o token reconhecido
                System.out.printf("Token: %-15s | Conteúdo: '%-25s' | Linha: %d Col: %d%n",
                        token.getType(), 
                        token.getContent(), 
                        token.getLine(), 
                        token.getStartColumn());
            }
            
            System.out.println("\n--- Fim do Teste do Scanner ---");
            System.out.println("Analise léxica concluída com sucesso!");

        } catch (LexicalErrorException e) {
            // Se o scanner falhar (ex: string não fechada, '=' sozinho)
            System.err.println("\n!!! Erro Léxico encontrado !!!");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de IO: Não foi possível ler o arquivo '" + filename + "'.");
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado:");
            e.printStackTrace();
        }
    }
}