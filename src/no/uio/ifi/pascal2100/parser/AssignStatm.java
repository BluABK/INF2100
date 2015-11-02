package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * {@link Variable} ':=' {@link Expression}
 */
public class AssignStatm extends Statement {
    public Variable var;
    public Expression expr;

    AssignStatm(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block scope, Library lib) {
        var.check(scope, lib);
        var.varDecl.checkWhetherAssignable(this);
        expr.check(scope, lib);
    }

    public static AssignStatm parse(Scanner s, PascalSyntax context) {
        enterParser("AssignStatm");

        AssignStatm a = new AssignStatm(s.curLineNum(), s.curColNum());
        a.context = context;

        a.var = Variable.parse(s, a);
        s.skip(TokenKind.assignToken);
        a.expr = Expression.parse(s, a);

        leaveParser("AssignStatm");
        return a;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        var.prettyPrint();
        Main.log.prettyPrint(" := ");
        expr.prettyPrint();
    }
}