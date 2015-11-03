package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * {@link Constant} | {@link Variable} | {@link FuncCall} | {@link InnerExpr} | {@link Negation}
 */
public abstract class Factor extends PascalSyntax {
    Factor(int n, int c) {
        super(n, c);
    }

    public static Factor parse(Scanner s, PascalSyntax context) {
        enterParser("Factor");

        Factor f = null;

        if (s.curToken.kind == TokenKind.notToken) {
            f = Negation.parse(s, context);
        } else if (s.curToken.kind == TokenKind.leftParToken) {
            f = InnerExpr.parse(s, context);
        } else if (s.curToken.kind == TokenKind.intValToken ||
                s.curToken.kind == TokenKind.stringValToken) {
            f = Constant.parse(s, context);
        } else if (s.curToken.kind == TokenKind.nameToken) {
            if (s.nextToken.kind == TokenKind.leftParToken) {
                f = FuncCall.parse(s, context);
            } else {
                f = Variable.parse(s, context);
            }
        } else {
            s.testError("constant, variable, func call, inner expr or negation");
        }

        leaveParser("Factor");
        return f;
    }
}
