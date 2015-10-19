package no.uio.ifi.pascal2100.parser;


import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

public class CompoundStatm extends Statement {
    public ArrayList<Statement> statements;

    CompoundStatm(int n, int c) {
        super(n, c);
        statements = new ArrayList<>();
    }

    public static CompoundStatm parse(Scanner s, PascalSyntax context) {
        enterParser("CompoundStatm");

        CompoundStatm c = new CompoundStatm(s.curLineNum(), s.curColNum());
        c.context = context;

        s.skip(TokenKind.beginToken);

        while (s.curToken.kind == TokenKind.nameToken ||
                s.curToken.kind == TokenKind.beginToken ||
                s.curToken.kind == TokenKind.semicolonToken ||
                s.curToken.kind == TokenKind.ifToken ||
                s.curToken.kind == TokenKind.whileToken) {

            if (s.curToken.kind != TokenKind.semicolonToken) {
                c.statements.add(Statement.parse(s, c));
            }

            if (s.curToken.kind != TokenKind.semicolonToken) break;
            s.skip(TokenKind.semicolonToken);
        }

        s.skip(TokenKind.endToken);

        leaveParser("CompoundStatm");
        return c;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrintLn("begin");
        Main.log.prettyIndent();

        boolean first = true;
        for (Statement s : statements) {
            if (!first) {
                Main.log.prettyPrintLn(";");
            }

            s.prettyPrint();

            first = false;
        }
        Main.log.prettyPrintLn("");
        Main.log.prettyOutdent();
        Main.log.prettyPrint("end");
    }
}
