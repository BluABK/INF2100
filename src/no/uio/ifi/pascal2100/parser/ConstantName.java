package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name
 */
public class ConstantName extends Constant {
    public String name;

    ConstantName(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block scope, Library lib) {
        // Check that name is defined
        scope.findDecl(name, this);
    }

    public static ConstantName parse(Scanner s, PascalSyntax context) {
        enterParser("ConstantName");

        ConstantName c = new ConstantName(s.curLineNum(), s.curColNum());
        c.context = context;

        s.test(TokenKind.nameToken);
        c.name = s.curToken.id;
        s.readNextToken();

        leaveParser("ConstantName");
        return c;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }
}
