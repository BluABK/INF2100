package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name ':' {@link Type} ';'
 */
public class VarDecl extends PascalDecl {
    public Type child;

    VarDecl(String name, int n, int c) {
        super(name, n, c);
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
        t.child = Type.parse(s, t);

        // ;
        s.skip(TokenKind.semicolonToken);

        leaveParser("VarDecl");
        return t;
    }

    @Override
    public void check(Block scope, Library lib) {
        child.check(scope, lib);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name + " = ");
        child.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
