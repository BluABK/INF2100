package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

public class ParamDeclList extends PascalSyntax {
    public ArrayList<ParamDecl> parameters;

    ParamDeclList(int n, int c) {
        super(n, c);
        parameters = new ArrayList<>();
    }

    public static ParamDeclList parse(Scanner s, PascalSyntax context) {
        enterParser("ParamDeclList");

        ParamDeclList p = new ParamDeclList(s.curLineNum(), s.curColNum());
        p.context = context;

        s.skip(TokenKind.leftParToken);

        while (s.curToken.kind != TokenKind.rightParToken) {
            p.parameters.add(ParamDecl.parse(s, p));
        }
        s.skip(TokenKind.rightParToken);

        leaveParser("ParamDeclList");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("(");
        boolean first = true;
        for (ParamDecl p : parameters) {
            if (!first) {
                Main.log.prettyPrint("; ");
            }

            p.prettyPrint();
            first = false;
        }
        Main.log.prettyPrint(")");
    }
}
