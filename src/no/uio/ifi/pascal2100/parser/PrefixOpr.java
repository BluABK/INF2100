package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;

public class PrefixOpr extends Opr {


    PrefixOpr(int n, int c) {
        super(n, c);
    }

    public static PrefixOpr parse(Scanner s, PascalSyntax context) {
        enterParser("PrefixOpr");

        PrefixOpr p = new PrefixOpr(s.curLineNum(), s.curColNum());
        p.context = context;

        switch (s.curToken.kind) {
            case addToken:
                p.op = Op.add;
                break;
            case subtractToken:
                p.op = Op.subtract;
                break;
            default:
                s.testError("+ or -");
        }
        s.readNextToken();

        leaveParser("PrefixOpr");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }
}
