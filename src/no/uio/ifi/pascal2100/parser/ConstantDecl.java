package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class ConstantDecl extends PascalDecl {
    public Constant child;

    ConstantDecl(String name, int n, int c) {
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

    public static ConstantDecl parse(Scanner s) {
        enterParser("ConstantDecl");

        ConstantDecl c = new ConstantDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        s.readNextToken();
        s.skip(TokenKind.equalToken);
        c.child = Constant.parse(s);
        s.skip(TokenKind.semicolonToken);

        leaveParser("ConstantDecl");
        return c;
    }
}
