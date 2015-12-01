package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * String literal
 */
public class ConstantStr extends Constant {
    public String str;

    ConstantStr(int n, int c) {
        super(n, c);
    }

    @Override
    public boolean testString() {
        return true;
    }

    @Override
    public boolean testChar() {
        return false;
    }

    @Override
    public void check(Block scope, Library lib) {
        // Strings are strings..
    }

    @Override
    public void genCode(CodeFile f) {
        String l = f.getLocalLabel();

        f.genString(l, str);
        f.genInstr("leal", l+",%eax");
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

    @Override
    public PascalSyntax getNonName() {
        return this;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public String toString() {
        return "'" + str + "'";
    }

    @Override
    void checkType(Constant cmp, PascalSyntax where, String message) {
        if(!(cmp instanceof ConstantStr))
            where.error(message);
    }

    /**
     * @return The value of the string
     */
    public String getString() {
        return str;
    }
}
