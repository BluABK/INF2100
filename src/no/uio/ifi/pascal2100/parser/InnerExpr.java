package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class InnerExpr extends Factor {
    public Expression expr;

    InnerExpr(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("(");
        expr.prettyPrint();
        Main.log.prettyPrint(")");
    }

    public static InnerExpr parse(Scanner s, PascalSyntax context) {
        enterParser("InnerExpr");

        InnerExpr i = new InnerExpr(s.curLineNum(), s.curColNum());
        i.context = context;

        s.skip(TokenKind.leftParToken);

        i.expr = Expression.parse(s, i);

        s.skip(TokenKind.rightParToken);

        leaveParser("InnerExpr");
        return i;
    }
}
