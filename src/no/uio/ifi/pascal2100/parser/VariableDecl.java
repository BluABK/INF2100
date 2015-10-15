package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class VariableDecl extends PascalDecl {
    public Type child;

    VariableDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    public String identify() {
        return "<constant> "+name+" on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        System.out.println("Fancy! Print! Wow!");
    }

    public static VariableDecl parse(Scanner s) {
        enterParser("VariableDecl");

        VariableDecl c = new VariableDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        s.readNextToken();
        s.skip(TokenKind.colonToken);
        c.child = Type.parse(s);
        s.skip(TokenKind.semicolonToken);

        leaveParser("VariableDecl");
        return c;
    }
}
