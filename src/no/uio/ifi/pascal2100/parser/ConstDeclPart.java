package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * 'const' {@link ConstDecl}...
 */
public class ConstDeclPart extends PascalSyntax {
    public ArrayList<ConstDecl> constants;

    @Override
    public void check(Block scope, Library lib) {
        for(ConstDecl c: constants) {
            c.check(scope, lib);
        }
    }

    @Override
    public void genCode(CodeFile f) {
        // TODO:
    }

    ConstDeclPart(int n, int c) {
        super(n, c);
        constants = new ArrayList<>();
    }

    public static ConstDeclPart parse(Scanner s, PascalSyntax context) {
        enterParser("ConstDeclPart");

        ConstDeclPart c = new ConstDeclPart(s.curLineNum(), s.curColNum());
        c.context = context;

        s.skip(TokenKind.constToken);

        while (s.curToken.kind == TokenKind.nameToken &&
                s.nextToken.kind == TokenKind.equalToken) {
            c.constants.add(ConstDecl.parse(s, c));
        }

        leaveParser("ConstDeclPart");
        return c;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrintLn("const");

        Main.log.prettyIndent();
        for (ConstDecl c : constants) {
            c.prettyPrint();
        }
        Main.log.prettyOutdent();
    }
}
