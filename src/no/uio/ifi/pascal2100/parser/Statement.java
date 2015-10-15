package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class Statement extends PascalSyntax {
    public Block child;

    Statement(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return "<Statement> on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        System.out.println("Fancy! Print! Wow!");
    }

    public static Statement parse(Scanner s) {
        enterParser("Statement");

        Statement st = new Statement(s.curLineNum(), s.curColNum());
        if(s.curToken.kind == TokenKind.semicolonToken) {
            // empty stmt
            s.readNextToken();
        } else if(s.curToken.kind == TokenKind.nameToken) {
            // assign statm / variable
            // proc call
            //
        }

        leaveParser("Statement");
        return f;
    }
}
