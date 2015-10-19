package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name [ '[' {@link Expression} ']' ]
 */
public class Variable extends Factor {
    public String name;
    public Expression expr;

    Variable(int n, int c) {
        super(n, c);
    }

    public static Variable parse(Scanner s, PascalSyntax context) {
        enterParser("Variable");

        Variable v = new Variable(s.curLineNum(), s.curColNum());
        v.context = context;

        s.test(TokenKind.nameToken);
        v.name = s.curToken.id;
        s.readNextToken();

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
        Main.log.prettyPrint(name);
        if (expr != null) {
            Main.log.prettyPrint("[");
            expr.prettyPrint();
            Main.log.prettyPrint("]");
        }
    }
}
