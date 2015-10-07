package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class Program extends PascalDecl {
    public Block child;

    Program(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    public String identify() {
        return "<program> "+name+" on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        System.out.println("Fancy! Print! Wow!");
    }

    public static Program parse(Scanner s) {
        enterParser("program");
        s.skip(TokenKind.programToken);
        s.test(TokenKind.nameToken);

        Program p = new Program(s.curToken.id, s.curLineNum(), s.curColNum());
        s.readNextToken();
        s.skip(TokenKind.semicolonToken);
        p.child = Block.parse(s);
        p.child.context = p;
        s.skip(TokenKind.dotToken);

        leaveParser("program");
        return p;
    }
}