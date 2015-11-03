package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name ':' Name
 */
public class ParamDecl extends PascalDecl {
    private NameType type;

    @Override
    public Type getType() {
        return type;
    }

    ParamDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {}

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Parameter " + name + " is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Parameter " + name + " is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {}


    public static ParamDecl parse(Scanner s, PascalSyntax context) {
        enterParser("ParamDecl");

        s.test(TokenKind.nameToken);
        ParamDecl p = new ParamDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        p.context = context;
        s.readNextToken();

        s.skip(TokenKind.colonToken);

        p.type = NameType.parse(s, p);

        leaveParser("ParamDecl");
        return p;
    }

    @Override
    public void check(Block scope, Library lib) {
        // NameType makes the binding
        type.check(scope, lib);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(stringCast());
    }

    public String stringCast() {
        return name + " : " + type;
    }
}
