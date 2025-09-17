# Lexical Analyzer

Projeto desenvolvido para a disciplina **Projeto de Linguagens de Programação**.  
O objetivo é implementar um analisador léxico em Java, baseado no código fornecido em sala, com suporte a identificadores, operadores, números decimais, palavras reservadas, comentários e tratamento de erros.

---

## Funcionalidades implementadas

1. **Reconhecimento de identificadores**
   - Padrão: `(a-z | A-Z | _)(a-z | A-Z | _ | 0-9)*`.

2. **Operadores matemáticos**
   - Soma `+`
   - Subtração `-`
   - Multiplicação `*`
   - Divisão `/`

3. **Operador de atribuição**
   - `=`

4. **Operadores relacionais**
   - `>`  
   - `>=`  
   - `<`  
   - `<=`  
   - `!=`  
   - `==`

5. **Parênteses**
   - `(`  
   - `)`

6. **Constantes numéricas com ponto decimal**
   - Válidos: `123`, `123.456`, `.456`  
   - Inválidos: `1.`, `12.`, `156.`  
   - Expressão regular: `((0-9)*\.)?(0-9)+`

7. **Palavras reservadas**
   - `int`  
   - `float`  
   - `print`  
   - `if`  
   - `else`  
   - Utilização de **tabela de palavras reservadas** para diferenciação de identificadores.

8. **Comentários**
   - Linha única: iniciados por `#`, válidos até o fim da linha.  
   - Múltiplas linhas: delimitados por `/*` e `*/`.  
   - Comentários são ignorados pelo analisador (não geram tokens).

9. **Tratamento de erros léxicos**
   - Ao encontrar um erro, o analisador informa **linha** e **coluna** do símbolo inválido.  
   - Exemplos de caracteres não permitidos: `@`, `` ` ``, `´`, `ç`, `¨`.

---

## Como executar

1. Clone o repositório:  
   ```bash
   git clone https://github.com/luizfiliperm/lexical-analyzer.git
   cd lexical-analyzer
    ```
 2. compile o projeto com maven
    ```
    mvn clean install
    ```
 3. Insira o código fonte a ser analisado dentro do arquivo Main.jjl.
    
  - Esse arquivo funciona como a entrada para o analisador.
  - O programa principal (Main.java) faz a leitura do conteúdo desse arquivo e envia para o Scanner.
  4. Resultado
   - A saída exibirá os tokens reconhecidos ou as mensagens de erro léxico








   

Grupo compostor por:
- João Pedro Soares Figueiredo
- João Victor Nunes
- Luiz Filipe de Matos Ramos
                
