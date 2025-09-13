package com.compiler.lexical.token;

public class Token {

    private TokenType type;
    private String content;
    private int startColumn;
    private int line;

    public Token(TokenType type, String content, int line, int startColumn) {
        this.type = type;
        this.content = content;
        this.line = line;
        this.startColumn = startColumn;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token {\n" +
                "  type: " + type + ",\n" +
                "  content: '" + content + "',\n" +
                "  line: " + line + ",\n" +
                "  column: " + startColumn + "\n" +
                "}";
    }
}
