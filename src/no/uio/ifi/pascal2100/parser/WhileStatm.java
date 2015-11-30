package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * 'while' {@link Expression} 'do' {@link Statement}
 */
public class WhileStatm extends Statement {
    public Expression test;
    public Statement statm;

    WhileStatm(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block scope, Library lib) {
        test.check(scope, lib);
        statm.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        Main.TODO();
    }

    public static WhileStatm parse(Scanner s, PascalSyntax context) {
        enterParser("WhileStatm");

        WhileStatm w = new WhileStatm(s.curLineNum(), s.curColNum());
        w.context = context;

        s.skip(TokenKind.whileToken);
        w.test = Expression.parse(s, w);
        s.skip(TokenKind.doToken);
        w.statm = Statement.parse(s, w);

        leaveParser("WhileStatm");
        return w;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("while ");
        test.prettyPrint();
        Main.log.prettyPrintLn(" do");
        Main.log.prettyIndent();
        statm.prettyPrint();
        Main.log.prettyOutdent();
    }
}
