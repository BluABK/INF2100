package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class ConstDecl extends PascalDecl {
    public Constant child;

    ConstDecl(String name, int n, int c) {
        super(name, n, c);
    }

    public static ConstDecl parse(Scanner s, PascalSyntax context) {
        enterParser("ConstDecl");

        // <name>
        s.test(TokenKind.nameToken);
        ConstDecl c = new ConstDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        c.context = context;
        s.readNextToken();

        // =
        s.skip(TokenKind.equalToken);

        // <constant>
        c.child = Constant.parse(s, c);

        // ;
        s.skip(TokenKind.semicolonToken);

        leaveParser("ConstDecl");
        return c;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name + " = ");
        child.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
