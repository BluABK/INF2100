package no.uio.ifi.pascal2100.parser;


import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * {@link Statement} [ ';' {@link Statement} ]...
 */
public class StatmList extends PascalSyntax {
    public ArrayList<Statement> statements;

    StatmList(int n, int c) {
        super(n, c);
        statements = new ArrayList<>();
    }

    @Override
    public void check(Block scope, Library lib) {
        for(Statement st: statements)
            st.check(scope, lib);
    }

    public static StatmList parse(Scanner s, PascalSyntax context) {
        enterParser("StatmList");

        StatmList p = new StatmList(s.curLineNum(), s.curColNum());
        p.context = context;

        while (s.curToken.kind == TokenKind.nameToken ||
                s.curToken.kind == TokenKind.beginToken ||
                s.curToken.kind == TokenKind.semicolonToken ||
                s.curToken.kind == TokenKind.ifToken ||
                s.curToken.kind == TokenKind.whileToken) {

            /* This will skip empty statements instead of parsing them further */
            if (s.curToken.kind != TokenKind.semicolonToken) {
                p.statements.add(Statement.parse(s, p));
            }

            if (s.curToken.kind != TokenKind.semicolonToken) break;
            s.skip(TokenKind.semicolonToken);
        }


        leaveParser("StatmList");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        for (int i = 0; i < statements.size(); i++) {
            statements.get(i).prettyPrint();
            if (i < statements.size() - 1)
                Main.log.prettyPrint(";");
            Main.log.prettyPrintLn("");
        }
    }
}
