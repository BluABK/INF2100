package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;

/**
 * '+' | '-' | 'or'
 * <br />
 * Subset of {@link Opr}
 */
public class TermOpr extends Opr {
    TermOpr(int n, int c) {
        super(n, c);
    }

    public static TermOpr parse(Scanner s, PascalSyntax context) {
        enterParser("TermOpr");

        TermOpr p = new TermOpr(s.curLineNum(), s.curColNum());
        p.context = context;

        switch (s.curToken.kind) {
            case addToken:
                p.op = Op.add;
                break;
            case subtractToken:
                p.op = Op.subtract;
                break;
            case orToken:
                p.op = Op.or;
                break;
            default:
                s.testError("+, -, or");
        }
        s.readNextToken();

        leaveParser("TermOpr");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }
}
