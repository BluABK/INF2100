package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * {@link Constant} '..' {@link Constant}
 */
public class RangeType extends Type {
    public Constant start;
    public Constant stop;

    RangeType(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block curScope, Library lib) {
        start.checkType(new ConstantInt(lineNum, colNum), this, "In range a..b, a needs to be an integer");
        stop.checkType(new ConstantInt(lineNum, colNum),  this, "In range a..b, b needs to be an integer");
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

    @Override
    void checkType(Type cmp, PascalSyntax where, String message) {
        Main.log.noteTypeCheck(cmp, "match", this, where);
        if(!(cmp instanceof RangeType))
            where.error(message);
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
}
