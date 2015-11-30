package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * '(' {@link ParamDecl} [ ';' {@link ParamDecl} ]... ')'
 */
public class ParamDeclList extends PascalSyntax {
    public ArrayList<ParamDecl> parameters;
    int totalArgSize;

    int parentDeclLevel;

    ParamDeclList(int n, int c) {
        super(n, c);
        parameters = new ArrayList<>();
    }

    /**
     * Calculate where on the stack the variables will exist
     */
    public void generateStackSize() {
        int offset = 8;
        for(ParamDecl p: parameters) {
            int size = p.getType().getStackSize();
            p.declOffset = offset;
            p.declLevel = parentDeclLevel;

            offset += size;
        }
        offset -= 8;
        totalArgSize = offset;
    }

    public void addDecls(Block container) {
        for(ParamDecl p: parameters) {
            container.addDecl(p.name, p);
        }
    }

    @Override
    public void check(Block scope, Library lib) {
        for(ParamDecl p: parameters)
            p.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        generateStackSize();
    }

    public static ParamDeclList parse(Scanner s, PascalSyntax context) {
        enterParser("ParamDeclList");

        ParamDeclList p = new ParamDeclList(s.curLineNum(), s.curColNum());
        p.context = context;

        s.skip(TokenKind.leftParToken);

        while (s.curToken.kind != TokenKind.rightParToken) {
            p.parameters.add(ParamDecl.parse(s, p));
            if(s.curToken.kind == TokenKind.semicolonToken)
                s.skip(TokenKind.semicolonToken);
        }
        s.skip(TokenKind.rightParToken);

        leaveParser("ParamDeclList");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("(");
        boolean first = true;
        for (ParamDecl p : parameters) {
            if (!first) {
                Main.log.prettyPrint("; ");
            }

            p.prettyPrint();
            first = false;
        }
        Main.log.prettyPrint(")");
    }
}
