package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * [ {@link PrefixOpr} ] {@link Term} [ {@link TermOpr} {@link Term} ]...
 */
public class SimpleExpr extends PascalSyntax {
    public PrefixOpr prefix;
    ArrayList<Term> terms;
    public ArrayList<TermOpr> termOprs;

    /* a op1 b op2 c op3 d
     *           0   1   2   3
     * terms:    a   b   c   d
     * termops:  op1 op2 op3
     *
     * So:
     * term[0] termops[0] term[1] termops[1] ...
     *
     * terms.size() = termops.size() + 1
     */

    SimpleExpr(int n, int c) {
        super(n, c);
        terms = new ArrayList<>();
        termOprs = new ArrayList<>();
    }

    public boolean testString() {
        // Does not support concatenation
        if(prefix != null)
            return false;
        if(termOprs.size() != 0)
            return false;
        return terms.get(0).testString();
    }
    public boolean testChar() {
        if(prefix != null)
            return false;
        if(termOprs.size() != 0)
            return false;
        return terms.get(0).testChar();
    }

    @Override
    public void check(Block scope, Library lib) {
        for(Term t: terms)
            t.check(scope, lib);
        for(TermOpr t: termOprs)
            t.check(scope, lib);
        if(prefix != null)
            prefix.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        terms.get(0).genCode(f);
        if(prefix != null)
            prefix.genCode(f);

        int i;
        for(i=0;i<termOprs.size();i++) {
            f.genInstr("push", "%eax");
            terms.get(i+1).genCode(f);
            f.genInstr("pop", "%ecx");
            termOprs.get(i).genCode(f);
        }
    }

    public static SimpleExpr parse(Scanner s, PascalSyntax context) {
        enterParser("SimpleExpr");

        SimpleExpr e = new SimpleExpr(s.curLineNum(), s.curColNum());
        e.context = context;

        if (s.curToken.kind == TokenKind.addToken ||
                s.curToken.kind == TokenKind.subtractToken) {
            e.prefix = PrefixOpr.parse(s, e);
        } else {
            e.prefix = null;
        }

        while (true) {
            e.terms.add(Term.parse(s, e));
            if (s.curToken.kind == TokenKind.addToken ||
                    s.curToken.kind == TokenKind.subtractToken ||
                    s.curToken.kind == TokenKind.orToken) {
                e.termOprs.add(TermOpr.parse(s, e));
            } else break;
        }

        leaveParser("SimpleExpr");
        return e;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        if (prefix != null) {
            prefix.prettyPrint();
            Main.log.prettyPrint(" ");
        }
        for (int i = 0; i < terms.size(); i++) {
            terms.get(i).prettyPrint();
            if (i < termOprs.size()) {
                Main.log.prettyPrint(" ");
                termOprs.get(i).prettyPrint();
                Main.log.prettyPrint(" ");
            }
        }
    }
}
