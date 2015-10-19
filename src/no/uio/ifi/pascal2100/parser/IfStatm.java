package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class IfStatm extends Statement {
    public Expression test;
    public Statement thenStatm;
    public Statement elseStatm;

    IfStatm(int n, int c) {
        super(n, c);
    }

    public static IfStatm parse(Scanner s, PascalSyntax context) {
        enterParser("IfStatm");

        IfStatm i = new IfStatm(s.curLineNum(), s.curColNum());
        i.context = context;

        s.skip(TokenKind.ifToken);
        i.test = Expression.parse(s, i);
        s.skip(TokenKind.thenToken);
        i.thenStatm = Statement.parse(s, i);
        if (s.curToken.kind == TokenKind.elseToken) {
            s.readNextToken();
            i.elseStatm = Statement.parse(s, i);
        } else {
            i.elseStatm = null;
        }

        leaveParser("IfStatm");
        return i;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("if ");
        test.prettyPrint();
        Main.log.prettyPrintLn(" then");
        Main.log.prettyIndent();
        thenStatm.prettyPrint();
        Main.log.prettyOutdent();
        Main.log.prettyPrintLn();
        if (elseStatm != null) {
            Main.log.prettyPrintLn("else");
            Main.log.prettyIndent();
            elseStatm.prettyPrint();
            Main.log.prettyOutdent();
        }
    }
}
