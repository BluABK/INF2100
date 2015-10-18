package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

public class ProcCallStatm extends Statement {
    public String name;
    public ArrayList<Expression> expressions;

    ProcCallStatm(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name);
        if(expressions != null) {
            Main.log.prettyPrint("(");
            boolean first = true;
            for (Expression e : expressions) {
                if (!first) {
                    Main.log.prettyPrint(", ");
                }

                e.prettyPrint();

                first = false;
            }
            Main.log.prettyPrint(")");
        }
    }

    public static ProcCallStatm parse(Scanner s, PascalSyntax context) {
        enterParser("ProcCallStatm");

        ProcCallStatm p = new ProcCallStatm(s.curLineNum(), s.curColNum());
        p.context = context;

        s.test(TokenKind.nameToken);
        p.name = s.curToken.id;
        s.readNextToken();

        if(s.curToken.kind == TokenKind.leftParToken) {
            p.expressions = new ArrayList<Expression>();
            s.skip(TokenKind.leftParToken);

            while(s.curToken.kind != TokenKind.rightParToken) {
                p.expressions.add(Expression.parse(s, p));

                if(s.curToken.kind != TokenKind.commaToken)
                    break;
                s.readNextToken();
            }
            s.skip(TokenKind.rightParToken);
        } else {
            p.expressions = null;
        }

        leaveParser("ProcCallStatm");
        return p;
    }
}
