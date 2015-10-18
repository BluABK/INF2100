package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class Expression extends PascalSyntax {
    public SimpleExpr lhs;
    public RelOpr op; // if op != null, then rhs != null
    public SimpleExpr rhs;

    Expression(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        lhs.prettyPrint();
        if(op != null || rhs != null) {
            Main.log.prettyPrint(" ");
            op.prettyPrint();
            Main.log.prettyPrint(" ");
            rhs.prettyPrint();
        }
    }

    public static Expression parse(Scanner s, PascalSyntax context) {
        enterParser("Expression");

        Expression e = new Expression(s.curLineNum(), s.curColNum());
        e.context = context;

        e.lhs = SimpleExpr.parse(s, e);

        if(s.curToken.kind == TokenKind.equalToken ||
           s.curToken.kind == TokenKind.notEqualToken ||
           s.curToken.kind == TokenKind.lessEqualToken ||
           s.curToken.kind == TokenKind.lessToken ||
           s.curToken.kind == TokenKind.greaterToken ||
           s.curToken.kind == TokenKind.greaterEqualToken) {
            e.op = RelOpr.parse(s, e);
            e.rhs = SimpleExpr.parse(s, e);
        } else {
            e.op = null;
            e.rhs = null;
        }

        leaveParser("Expression");
        return e;
    }
}
