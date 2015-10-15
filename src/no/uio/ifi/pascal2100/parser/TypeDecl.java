package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class TypeDecl extends PascalDecl {
    public Type child;

    TypeDecl(String name, int n, int c) {
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

    public static TypeDecl parse(Scanner s) {
        enterParser("TypeDecl");

        TypeDecl c = new TypeDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        s.readNextToken();
        s.skip(TokenKind.equalToken);
        c.child = Type.parse(s);
        s.skip(TokenKind.semicolonToken);

        leaveParser("TypeDecl");
        return c;
    }
}
