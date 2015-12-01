package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
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

    public boolean testString() {
        if(factorOprs.size() != 0)
            return false;
        return factors.get(0).testString();
    }
    public boolean testChar() {
        if(factorOprs.size() != 0)
            return false;
        return factors.get(0).testChar();
    }

    @Override
    public void check(Block scope, Library lib) {
        for(FactorOpr f: factorOprs)
            f.check(scope, lib);
        for(Factor f: factors)
            f.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        System.out.println(factors.get(0));
        factors.get(0).genCode(f);
        int i;
        for(i=0;i<factorOprs.size();i++) {
            f.genInstr("push", "%eax");
            factors.get(i+1).genCode(f);
            f.genInstr("pop", "%ecx");
            factorOprs.get(i).genCode(f);
        }
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
