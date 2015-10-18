package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class RelOpr extends Opr {
    RelOpr(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    public static RelOpr parse(Scanner s, PascalSyntax context) {
        enterParser("RelOpr");

        RelOpr r = new RelOpr(s.curLineNum(), s.curColNum());
        r.context = context;

        switch(s.curToken.kind) {
            case equalToken:        r.op = Op.equal;  break;
            case notEqualToken:     r.op = Op.notEqual; break;
            case lessEqualToken:    r.op = Op.lessEqual; break;
            case lessToken:         r.op = Op.less;  break;
            case greaterEqualToken: r.op = Op.greaterEqual; break;
            case greaterToken:      r.op = Op.greater;  break;
            default:
                s.testError("=, <>, <, <=, >, <=");
        }
        s.readNextToken();

        leaveParser("RelOpr");
        return r;
    }
}
