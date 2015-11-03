package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * 'program' Name ';' {@link Block} '.'
 */
public class Program extends PascalDecl {
    public Block child;

    @Override
    public Type getType() {
        return null;
    }

    Program(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Program " + name + " is not assignable");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Program " + name + " is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Program " + name + " is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {
        where.error("Program " + name + " is not a value");
    }

    public static Program parse(Scanner s) {
        enterParser("Program");

        // Program
        s.skip(TokenKind.programToken);

        // <name>
        s.test(TokenKind.nameToken);
        Program p = new Program(s.curToken.id, s.curLineNum(), s.curColNum());
        p.context = null;
        s.readNextToken();

        // ;
        s.skip(TokenKind.semicolonToken);

        // <block>
        p.child = Block.parse(s, p);

        // .
        s.skip(TokenKind.dotToken);

        leaveParser("Program");
        return p;
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
        Main.log.prettyPrintLn("Program " + name + ";");
        child.prettyPrint();
        Main.log.prettyPrintLn(".");
    }
}
