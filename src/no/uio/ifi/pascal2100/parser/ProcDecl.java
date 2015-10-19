package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class ProcDecl extends PascalDecl {
    public Block child;

    public ParamDeclList params;

    ProcDecl(String name, int n, int c) {
        super(name, n, c);
    }

    public static ProcDecl parse(Scanner s, PascalSyntax context) {
        enterParser("ProcDecl");
        s.skip(TokenKind.procedureToken);

        s.test(TokenKind.nameToken);
        ProcDecl p = new ProcDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        p.context = context;
        s.readNextToken();

        if (s.curToken.kind != TokenKind.semicolonToken) {
            p.params = ParamDeclList.parse(s, p);
        } else {
            p.params = null;
        }

        s.skip(TokenKind.semicolonToken);

        p.child = Block.parse(s, p);
        s.skip(TokenKind.semicolonToken);

        leaveParser("ProcDecl");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("procedure " + name);
        if (params != null) {
            Main.log.prettyPrint(" ");
            params.prettyPrint();
        }
        Main.log.prettyPrintLn(";");
        child.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
