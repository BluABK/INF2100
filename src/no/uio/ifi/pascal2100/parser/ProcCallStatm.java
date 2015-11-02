package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * Name [ '(' {@link Expression} [ ',' {@link Expression} ].. ')' ]
 */
public class ProcCallStatm extends Statement {
    public ConstantName name;
    public ArrayList<Expression> expressions;

    ProcCallStatm(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block scope, Library lib) {
        name.check(scope, lib);
        PascalDecl pd = scope.findDecl(name.name, this);
        if(!(pd instanceof ProcDecl))
            error("ProcCall tried to call something which is not a ProcDecl");

        if(expressions != null)
            for(Expression e: expressions) {
                e.check(scope, lib);
            }
    }

    public static ProcCallStatm parse(Scanner s, PascalSyntax context) {
        enterParser("ProcCallStatm");

        ProcCallStatm p = new ProcCallStatm(s.curLineNum(), s.curColNum());
        p.context = context;

        p.name = ConstantName.parse(s, p);

        if (s.curToken.kind == TokenKind.leftParToken) {
            p.expressions = new ArrayList<>();
            s.skip(TokenKind.leftParToken);

            while (s.curToken.kind != TokenKind.rightParToken) {
                p.expressions.add(Expression.parse(s, p));

                if (s.curToken.kind != TokenKind.commaToken)
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

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        name.prettyPrint();
        Main.log.prettyPrint(name.name);
        if (expressions != null) {
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
}
