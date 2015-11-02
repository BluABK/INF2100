package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * {@link Factor} [ {@link FactorOpr} {@link Factor} ]...
 */
public class Term extends PascalSyntax {
    // See SimpleExpr, same thing there.
    public ArrayList<Factor> factors;
    public ArrayList<FactorOpr> factorOprs;

    Term(int n, int c) {
        super(n, c);
        factors = new ArrayList<>();
        factorOprs = new ArrayList<>();
    }

    @Override
    public void check(Block scope, Library lib) {
        for(FactorOpr f: factorOprs)
            f.check(scope, lib);
        for(Factor f: factors)
            f.check(scope, lib);
    }

    public static Term parse(Scanner s, PascalSyntax context) {
        enterParser("Term");

        Term t = new Term(s.curLineNum(), s.curColNum());
        t.context = context;

        while (true) {
            t.factors.add(Factor.parse(s, t));
            if (s.curToken.kind == TokenKind.multiplyToken ||
                    s.curToken.kind == TokenKind.divToken ||
                    s.curToken.kind == TokenKind.modToken ||
                    s.curToken.kind == TokenKind.andToken) {
                t.factorOprs.add(FactorOpr.parse(s, t));
            } else break;
        }

        leaveParser("Term");
        return t;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        for (int i = 0; i < factors.size(); i++) {
            factors.get(i).prettyPrint();
            if (i < factorOprs.size()) {
                Main.log.prettyPrint(" ");
                factorOprs.get(i).prettyPrint();
                Main.log.prettyPrint(" ");
            }
        }
    }
}
