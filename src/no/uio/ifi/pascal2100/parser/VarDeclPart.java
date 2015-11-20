package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * 'var' {@link VarDecl}...
 */
public class VarDeclPart extends PascalSyntax {
    public ArrayList<VarDecl> vars;
    // Bytes of variables used in the stack, including the 32 first bytes
    int totalStackSize;

    VarDeclPart(int n, int c) {
        super(n, c);
        vars = new ArrayList<>();
    }

    /**
     * Calculate where on the stack the variables will exist
     */
    public void generateStackSize() {
        int offset = -32;
        for(VarDecl v: vars) {
            int size = v.getType().getStackSize();
            offset -= size;
            v.stackOffset = offset;
        }
        totalStackSize = -offset;
    }

    @Override
    public void check(Block scope, Library lib) {
        for(VarDecl v: vars)
            v.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        /* Generate stack sizes */
        generateStackSize();
    }

    public static VarDeclPart parse(Scanner s, PascalSyntax context) {
        enterParser("VarDeclPart");

        VarDeclPart v = new VarDeclPart(s.curLineNum(), s.curColNum());
        v.context = context;

        s.skip(TokenKind.varToken);

        while (s.curToken.kind == TokenKind.nameToken &&
                s.nextToken.kind == TokenKind.colonToken) {
            v.vars.add(VarDecl.parse(s, v));
        }

        leaveParser("VarDeclPart");
        return v;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrintLn("var");

        Main.log.prettyIndent();
        for (VarDecl v : vars) {
            v.prettyPrint();
        }
        Main.log.prettyOutdent();
    }
}
