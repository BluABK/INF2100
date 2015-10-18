package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class NameType extends Type {
    public String name;

    NameType(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return "<" + this.getClass().getSimpleName() + "> with type " + name
                 + " on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name);
    }

    public static NameType parse(Scanner s, PascalSyntax context) {
        enterParser("NameType");

        NameType r = new NameType(s.curLineNum(), s.curColNum());
        r.context = context;

        s.test(TokenKind.nameToken);
        r.name = s.curToken.id;
        s.readNextToken();

        leaveParser("NameType");
        return r;
    }
}
