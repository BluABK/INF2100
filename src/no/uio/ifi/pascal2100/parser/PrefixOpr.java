package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.scanner.Scanner;

/**
 * '+' | '-'
 * <br />
 * Subset of {@link Opr}
 */
public class PrefixOpr extends Opr {
    PrefixOpr(int n, int c) {
        super(n, c);
    }

    // Not much to check
    @Override
    public void check(Block curScope, Library lib) {}

    @Override
    public void genCode(CodeFile f) {
        if(op == Op.subtract)
            f.genInstr("negl", "%eax");
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
