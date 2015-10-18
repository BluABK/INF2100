package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

public class FuncCall extends Factor {
    public String name;
    public ArrayList<Expression> expressions;

    FuncCall(int n, int c) {
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

    public static FuncCall parse(Scanner s, PascalSyntax context) {
        enterParser("FuncCall");

        FuncCall f = new FuncCall(s.curLineNum(), s.curColNum());
        f.context = context;

        s.test(TokenKind.nameToken);
        f.name = s.curToken.id;
        s.readNextToken();

        if(s.curToken.kind == TokenKind.leftParToken) {
            f.expressions = new ArrayList<Expression>();
            s.skip(TokenKind.leftParToken);

            while(s.curToken.kind != TokenKind.rightParToken) {
                f.expressions.add(Expression.parse(s, f));

                if(s.curToken.kind != TokenKind.commaToken) {
                    break;
                }
                s.readNextToken();
            }
            s.skip(TokenKind.rightParToken);
        } else {
            f.expressions = null;
        }

        leaveParser("FuncCall");
        return f;
    }
}
