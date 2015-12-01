package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name ':' {@link Type} ';'
 */
public class VarDecl extends PascalDecl {
    private Type type;

    VarDecl(String name, int n, int c) {
        super(name, n, c);
    }

    public static VarDecl parse(Scanner s, PascalSyntax context) {
        enterParser("VarDecl");

        // <name>
        s.test(TokenKind.nameToken);
        VarDecl t = new VarDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        t.context = context;
        s.readNextToken();

        // :
        s.skip(TokenKind.colonToken);

        // <type>
        t.type = Type.parse(s, t);

        // ;
        s.skip(TokenKind.semicolonToken);

        leaveParser("VarDecl");
        return t;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean testString() {
        return type.testString();
    }

    @Override
    public boolean testChar() {
        return type.testChar();
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Variable is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Variable is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {
    }

    @Override
    public void check(Block scope, Library lib) {
        type.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile code) {
        // No relevant code
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name + " = ");
        type.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
