package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Enum is a string-like constant associated with an EnumType
 *
 * Note: Enums are looked up twice, once in Variable and once in ConstantName because of the replacement logic.
 * Therefore, it causes two binding log lines for the same thing.
 */
public class Enum extends PascalDecl {
    Enum(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Enum is not assignable");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Enum is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Enum is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {}

    @Override
    public void check(Block scope, Library lib) {}

    public static Enum parse(Scanner s, PascalSyntax context) {
        enterParser("Enum");

        s.test(TokenKind.nameToken);
        Enum e = new Enum(s.curToken.id, s.curLineNum(), s.curColNum());
        e.context = context;
        s.readNextToken();

        leaveParser("Enum");
        return e;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name);
    }

    @Override
    public String toString() {
        return name;
    }
}