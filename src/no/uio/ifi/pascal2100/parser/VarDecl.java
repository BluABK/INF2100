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

    // Part4, use type.getStackSize() to figure out how much space is required. Field is in bytes from -36 and downwards
    int stackOffset;

    @Override
    public Type getType() {
        return type;
    }

    VarDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {}

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Variable is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Variable is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {}


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
