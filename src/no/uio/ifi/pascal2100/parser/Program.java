package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class Program extends PascalDecl {
    public Block child;

    Program(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrintLn("Program "+name+";");
        child.prettyPrint();
        Main.log.prettyPrintLn(".");
    }

    public static Program parse(Scanner s, PascalSyntax context) {
        enterParser("Program");

        // Program
        s.skip(TokenKind.programToken);

        // <name>
        s.test(TokenKind.nameToken);
        Program p = new Program(s.curToken.id, s.curLineNum(), s.curColNum());
        p.context = context;
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
}
