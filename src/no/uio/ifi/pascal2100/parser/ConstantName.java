package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name
 */
public class ConstantName extends Constant {
    public String name;

    PascalDecl decl;

    ConstantName(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block scope, Library lib) {
        // Check that name is defined
        PascalDecl d = scope.findDecl(name, this);
        if(!(d instanceof ConstDecl) && !(d instanceof Enum)) {
            error("Constant not declared as a constant");
            return;
        }
        decl = d;
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

    @Override
    void checkType(Constant cmp, PascalSyntax where, String message) {
        if(decl instanceof Enum) {
            where.error("Constant is an enum constant");
            return;
        }
        ((ConstDecl)decl).child.checkType(cmp, where, message);
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }
}
