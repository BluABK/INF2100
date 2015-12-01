package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
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
    public boolean testString() {
        return !name.equals("eol") && decl.testString();

    }

    @Override
    public boolean testChar() {
        return name.equals("eol") || decl.testChar();

    }

    @Override
    public void check(Block scope, Library lib) {
        // Check that name is defined
        PascalDecl d = scope.findDecl(name, this);
        if (!(d instanceof ConstDecl) && !(d instanceof Enum)) {
            error("Name " + name + " needs to be declared as a constant");
            return;
        }
        decl = d;
    }

    @Override
    public void genCode(CodeFile f) {
        if (name.equals("eol"))
            f.genInstr("movl", "$10,%eax");
        else
            decl.genCode(f);
    }

    @Override
    public PascalSyntax getNonName() {
        if (decl instanceof Enum)
            return decl;

        return ((ConstDecl) decl).child;
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
        if (decl instanceof Enum) {
            where.error("Constant " + name + " is an enum constant. " + message);
            return;
        }
        ((ConstDecl) decl).child.checkType(cmp, where, message);
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }
}
