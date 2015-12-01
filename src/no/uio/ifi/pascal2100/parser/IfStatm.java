package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * 'if' {@link Expression} then {@link Statement} [ 'else' {@link Statement} ]
 */
public class IfStatm extends Statement {
    public Expression test;
    public Statement thenStatm;
    public Statement elseStatm;

    IfStatm(int n, int c) {
        super(n, c);
    }

    public static IfStatm parse(Scanner s, PascalSyntax context) {
        enterParser("IfStatm");

        IfStatm i = new IfStatm(s.curLineNum(), s.curColNum());
        i.context = context;

        s.skip(TokenKind.ifToken);
        i.test = Expression.parse(s, i);
        s.skip(TokenKind.thenToken);
        i.thenStatm = Statement.parse(s, i);
        if (s.curToken.kind == TokenKind.elseToken) {
            s.readNextToken();
            i.elseStatm = Statement.parse(s, i);
        } else {
            i.elseStatm = null;
        }

        leaveParser("IfStatm");
        return i;
    }

    @Override
    public void check(Block scope, Library lib) {
        test.check(scope, lib);
        thenStatm.check(scope, lib);
        if (elseStatm != null)
            elseStatm.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        String l1 = f.getLocalLabel();

        test.genCode(f);
        f.genInstr("cmpl", "$0,%eax", "if " + l1);
        f.genInstr("je", l1);
        thenStatm.genCode(f);
        if (elseStatm != null) {
            String l2 = f.getLocalLabel();
            f.genInstr("jmp", l2);
            f.genLabel(l1);
            elseStatm.genCode(f);
            f.genLabel(l2);
        } else {
            f.genLabel(l1, "/if " + l1);
        }
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("if ");
        test.prettyPrint();
        Main.log.prettyPrintLn(" then");
        Main.log.prettyIndent();
        thenStatm.prettyPrint();
        Main.log.prettyOutdent();
        Main.log.prettyPrintLn();
        if (elseStatm != null) {
            Main.log.prettyPrintLn("else");
            Main.log.prettyIndent();
            elseStatm.prettyPrint();
            Main.log.prettyOutdent();
        }
    }
}
