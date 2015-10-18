package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class Negation extends Factor {
    public Factor factor;

    Negation(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("not ");
        factor.prettyPrint();
    }

    public static Negation parse(Scanner s, PascalSyntax context) {
        enterParser("Negation");

        Negation n = new Negation(s.curLineNum(), s.curColNum());
        n.context = context;

        s.skip(TokenKind.notToken);

        n.factor = Factor.parse(s, n);

        leaveParser("Negation");
        return n;
    }
}
