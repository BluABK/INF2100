package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
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
    public void check(Block scope, Library lib) {
        var.check(scope, lib);

        expr.check(scope, lib);

        // By being a variable, it should inherently be assignable. var.check() checks this relationship.
        // The following is therefore not strictly necessary as it always returns the same result:
        var.decl.checkWhetherAssignable(this);
    }

    @Override
    public void genCode(CodeFile f) {
        expr.genCode(f);
        var.genCodeSet(f);
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
