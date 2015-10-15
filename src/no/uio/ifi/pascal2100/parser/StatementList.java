package no.uio.ifi.pascal2100.parser;


import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class StatementList extends PascalSyntax {

    StatementList(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return "<StatementList> on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        System.out.println("Fancy! Print! Wow!");
    }

    public static StatementList parse(Scanner s) {
        enterParser("StatementList");

        do {
            if(s.curToken.kind == TokenKind.beginToken) {
                s.readNextToken();
                // TODO: self in self
                s.skip(TokenKind.endToken);
            } else if(s.curToken.kind == TokenKind.ifToken) {
                // TODO: if type
            } else if(s.curToken.kind == TokenKind.whileToken) {
                // TODO: while
            } else if(s.curToken.kind == TokenKind.nameToken) {
                // Get the name and s.readNextToken, then figure out
            }

            if(s.curToken.kind == TokenKind.semicolonToken) {
                s.readNextToken();
            } else break;
        } while(true);

        StatementList st = new StatementList(s.curLineNum(), s.curColNum());
        if(s.curToken.kind == TokenKind.semicolonToken) {
            // empty stmt
            s.readNextToken();
        } else if(s.curToken.kind == TokenKind.nameToken) {
            // assign statm / variable
            // proc call
            //
        }

        leaveParser("StatementList");
        return st;
    }
}
