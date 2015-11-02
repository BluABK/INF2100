package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * 'procedure' Name [ {@link ParamDeclList} ] ';' {@link Block} ';'
 */
public class ProcDecl extends PascalDecl {
    public Block child;

    public ParamDeclList params;

    ProcDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Procedure is not assignable");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Procedure is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {

    }

    @Override
    void checkWhetherValue(PascalSyntax where) {
        where.error("Procedure has no return value");
    }

    public static ProcDecl parse(Scanner s, PascalSyntax context) {
        enterParser("ProcDecl");
        s.skip(TokenKind.procedureToken);

        s.test(TokenKind.nameToken);
        ProcDecl p = new ProcDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        p.context = context;
        s.readNextToken();

        if (s.curToken.kind != TokenKind.semicolonToken) {
            p.params = ParamDeclList.parse(s, p);
        } else {
            p.params = null;
        }

        s.skip(TokenKind.semicolonToken);

        p.child = Block.parse(s, p);
        s.skip(TokenKind.semicolonToken);

        leaveParser("ProcDecl");
        return p;
    }

    @Override
    public void check(Block scope, Library lib) {
        if(params != null) {
            params.check(scope, lib);
            // Params.addDecls adds the parameters to the Block of the function
            params.addDecls(child);
        }

        child.check(scope, lib);
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("procedure " + name);
        if (params != null) {
            Main.log.prettyPrint(" ");
            params.prettyPrint();
        }
        Main.log.prettyPrintLn(";");
        child.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
