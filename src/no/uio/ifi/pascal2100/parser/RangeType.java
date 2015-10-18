package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class RangeType extends Type {
    public Constant start;
    public Constant stop;

    RangeType(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return "<" + this.getClass().getSimpleName() + "> from " +
                start + " to " + stop + " on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        start.prettyPrint();
        Main.log.prettyPrint("..");
        stop.prettyPrint();
    }

    public static RangeType parse(Scanner s, PascalSyntax context) {
        enterParser("RangeType");

        RangeType r = new RangeType(s.curLineNum(), s.curColNum());
        r.context = context;
        r.start = Constant.parse(s, r);
        s.skip(TokenKind.rangeToken);
        r.stop = Constant.parse(s, r);

        leaveParser("RangeType");
        return r;
    }
}
