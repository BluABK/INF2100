package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Statement
 * Note: Does not include or support empty statement, these are skipped by StatmList.
 */
public abstract class Statement extends PascalSyntax {
    Statement(int n, int c) {
        super(n, c);
    }

    public static Statement parse(Scanner s, PascalSyntax context) {
        enterParser("Statement");

        Statement st = null;

        if (s.curToken.kind == TokenKind.beginToken) {
            st = CompoundStatm.parse(s, context);
        } else if (s.curToken.kind == TokenKind.ifToken) {
            st = IfStatm.parse(s, context);
        } else if (s.curToken.kind == TokenKind.whileToken) {
            st = WhileStatm.parse(s, context);
        } else if (s.curToken.kind == TokenKind.nameToken) {
            if (s.nextToken.kind == TokenKind.assignToken ||
                    s.nextToken.kind == TokenKind.leftBracketToken) {
                st = AssignStatm.parse(s, context);
            } else {
                st = ProcCallStatm.parse(s, context);
            }
        } else {
            s.testError("any valid statement");
        }

        leaveParser("Statement");
        return st;
    }
}
