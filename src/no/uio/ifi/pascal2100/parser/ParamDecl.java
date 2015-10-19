package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name ':' Name
 */
public class ParamDecl extends PascalDecl {
    public String type;

    ParamDecl(String name, int n, int c) {
        super(name, n, c);
    }

    public static ParamDecl parse(Scanner s, PascalSyntax context) {
        enterParser("ParamDecl");

        s.test(TokenKind.nameToken);
        ParamDecl p = new ParamDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        p.context = context;
        s.readNextToken();

        s.skip(TokenKind.colonToken);

        s.test(TokenKind.nameToken);
        p.type = s.curToken.id;
        s.readNextToken();

        leaveParser("ParamDecl");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(stringCast());
    }

    public String stringCast() {
        return name + " : " + type;
    }
}
