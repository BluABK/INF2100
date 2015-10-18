package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class ConstantStr extends Constant {
    public String str;

    ConstantStr(int n, int c) {
        super(n, c);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public String toString() {
        return "'"+str+"'";
    }

    public String getString() {
        return str;
    }

    public static ConstantStr parse(Scanner s, PascalSyntax context) {
        enterParser("ConstantStr");

        ConstantStr c = new ConstantStr(s.curLineNum(), s.curColNum());
        c.context = context;

        s.test(TokenKind.stringValToken);
        c.str = s.curToken.strVal;
        s.readNextToken();

        leaveParser("ConstantStr");
        return c;
    }
}
