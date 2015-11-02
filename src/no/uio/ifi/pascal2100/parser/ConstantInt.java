package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Numeric literal
 */
public class ConstantInt extends Constant {
    public int integer;

    ConstantInt(int n, int c) {
        super(n, c);
    }

    // Nothing to check
    @Override
    public void check(Block scope, Library lib) {}

    public static ConstantInt parse(Scanner s, PascalSyntax context) {
        enterParser("ConstantInt");

        ConstantInt c = new ConstantInt(s.curLineNum(), s.curColNum());
        c.context = context;

        s.test(TokenKind.intValToken);
        c.integer = s.curToken.intVal;
        s.readNextToken();

        leaveParser("ConstantInt");
        return c;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public String toString() {
        return Integer.toString(integer);
    }

    /**
     * @return The numeric value
     */
    public int getInt() {
        return integer;
    }
}