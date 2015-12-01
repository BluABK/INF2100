package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

/**
 * '=' | '&lt;&gt;' | '&lt;' | '&lt;=' | '&gt;' | '&gt;='
 * <br />
 * Subset of {@link Opr}
 */
public class RelOpr extends Opr {
    RelOpr(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block curScope, Library lib) {}

    @Override
    public void genCode(CodeFile f) {
        f.genInstr("cmpl", "%eax,%ecx", op.toString());
        f.genInstr("xorl", "%eax,%eax", "^");
        // switch
        if(op == op.equal)
            f.genInstr("sete", "%al", "^");
        else if(op == Op.notEqual)
            f.genInstr("setne", "%al", "^");
        else if(op == Op.less)
            f.genInstr("setl", "%al", "^");
        else if(op == Op.lessEqual)
            f.genInstr("setle", "%al", "^");
        else if(op == Op.greater)
            f.genInstr("setg", "%al", "^");
        else //if(op == Op.greaterEqual)
            f.genInstr("setge", "%al", "^");
    }

    public static RelOpr parse(Scanner s, PascalSyntax context) {
        enterParser("RelOpr");

        RelOpr r = new RelOpr(s.curLineNum(), s.curColNum());
        r.context = context;

        switch (s.curToken.kind) {
            case equalToken:
                r.op = Op.equal;
                break;
            case notEqualToken:
                r.op = Op.notEqual;
                break;
            case lessEqualToken:
                r.op = Op.lessEqual;
                break;
            case lessToken:
                r.op = Op.less;
                break;
            case greaterEqualToken:
                r.op = Op.greaterEqual;
                break;
            case greaterToken:
                r.op = Op.greater;
                break;
            default:
                s.testError("=, <>, <, <=, >, <=");
        }
        s.readNextToken();

        leaveParser("RelOpr");
        return r;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }
}
