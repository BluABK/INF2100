package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name [ '[' {@link Expression} ']' ]
 */
public class Variable extends Factor {
    public ConstantName name;
    public Expression expr;

    public VarDecl varDecl;

    Variable(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block scope, Library lib) {
        PascalDecl p = scope.findDecl(name.name, this);
        if(!(p instanceof VarDecl)) {
            error("Variable instance is not declared as a variable");
        }
        varDecl = (VarDecl)p;

        if(expr != null) {
            expr.check(scope, lib);
        }
    }

    public static Variable parse(Scanner s, PascalSyntax context) {
        enterParser("Variable");

        Variable v = new Variable(s.curLineNum(), s.curColNum());
        v.context = context;

        v.name = ConstantName.parse(s, v);

        if (s.curToken.kind == TokenKind.leftBracketToken) {
            v.expr = Expression.parse(s, v);
            s.skip(TokenKind.rightBracketToken);
        } else {
            v.expr = null;
        }

        leaveParser("Variable");
        return v;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        name.prettyPrint();
        if (expr != null) {
            Main.log.prettyPrint("[");
            expr.prettyPrint();
            Main.log.prettyPrint("]");
        }
    }
}
