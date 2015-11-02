package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name '=' {@link Type} ';'
 */
public class TypeDecl extends PascalDecl {
    public Type child;

    TypeDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Types are not assignable");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Types are not functions");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Types are not procedures");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {
        where.error("Types are not values");
    }

    public static TypeDecl parse(Scanner s, PascalSyntax context) {
        enterParser("TypeDecl");

        // <type name> aka <name>
        s.test(TokenKind.nameToken);
        TypeDecl t = new TypeDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        t.context = context;
        s.readNextToken();

        // =
        s.skip(TokenKind.equalToken);

        // <type>
        t.child = Type.parse(s, t);

        // ;
        s.skip(TokenKind.semicolonToken);

        leaveParser("TypeDecl");
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
