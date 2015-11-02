package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * 'function' Name [ {@link ParamDeclList} ] ':' Name ';' {@link Block} ';'
 */
public class FuncDecl extends PascalDecl {
    public Block child;
    public NameType returnType;

    public ParamDeclList params;

    FuncDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Function is not assignable");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Function is not a procedure");
    }

    // Return value:
    @Override
    void checkWhetherValue(PascalSyntax where) {
        where.error("Function must be called to return a value");
    }

    @Override
    public void check(Block scope, Library lib) {
        params.check(scope, lib);

        // Params.addDecls adds the parameters to the Block of the function
        params.addDecls(child);

        PascalDecl type = scope.findDecl(returnType.name, this);
        // TODO: check that type is an actual Type

        child.check(scope, lib);
    }

    public static FuncDecl parse(Scanner s, PascalSyntax context) {
        enterParser("FuncDecl");

        s.skip(TokenKind.functionToken);

        s.test(TokenKind.nameToken);
        FuncDecl f = new FuncDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        f.context = context;
        s.readNextToken();

        if (s.curToken.kind != TokenKind.colonToken) {
            f.params = ParamDeclList.parse(s, f);
        } else {
            f.params = null;
        }

        s.skip(TokenKind.colonToken);

        f.returnType = NameType.parse(s, f);

        s.skip(TokenKind.semicolonToken);

        f.child = Block.parse(s, f);
        s.skip(TokenKind.semicolonToken);

        leaveParser("FuncDecl");
        return f;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("function " + name);
        if (params != null) {
            Main.log.prettyPrint(" ");
            params.prettyPrint();
        }
        Main.log.prettyPrintLn(" : " + returnType.name + ";");
        child.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
