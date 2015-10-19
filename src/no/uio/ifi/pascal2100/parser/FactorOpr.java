package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;

public class FactorOpr extends Opr {
    FactorOpr(int n, int c) {
        super(n, c);
    }

    public static FactorOpr parse(Scanner s, PascalSyntax context) {
        enterParser("FactorOpr");

        FactorOpr p = new FactorOpr(s.curLineNum(), s.curColNum());
        p.context = context;

        switch (s.curToken.kind) {
            case multiplyToken:
                p.op = Op.multiply;
                break;
            case divToken:
                p.op = Op.div;
                break;
            case modToken:
                p.op = Op.mod;
                break;
            case andToken:
                p.op = Op.and;
                break;
            default:
                s.testError("*, div, mod, and");
        }
        s.readNextToken();

        leaveParser("FactorOpr");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }
}
