package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * 'function' Name [ {@link ParamDeclList} ] ':' Name ';' {@link Block} ';'
 */
public class FuncDecl extends PascalDecl {
    public Block child;
    private NameType type;

    public ParamDeclList params;

    @Override
    public Type getType() {
        return type;
    }

    FuncDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {}

    @Override
    void checkWhetherFunction(PascalSyntax where) {}

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Function " + name + " is not a procedure");
    }

    // Return value:
    @Override
    void checkWhetherValue(PascalSyntax where) {
        where.error("Function " + name + " is not a value");
    }

    @Override
    public void check(Block scope, Library lib) {
        params.check(scope, lib);

        // Params.addDecls adds the parameters to the Block of the function
        params.addDecls(child);

        type.check(scope,  lib);

        child.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile code) {
        // assumes declLevel to be set


        if(type.getStackSize() != 4) {
            // Can only return 4 bytes at a time, char is going to be casted to 4 bytes.
            error("Function cannot return arrays or other > 4 byte types");
        }
        declOffset = -32;
        // Params are to be labeled 8, 12, 16...
        //    params.parameters.get(i).stackOffset;
        //    params.totalArgSize
        params.parentDeclLevel = declLevel;
        params.genCode(code);
        // We know return value is stored in -32(%ebp), block does this


        // The declLevel is always one step outside the parent block
        child.parentDeclLevel = declLevel;
        child.mangledName = code.getLabel("func$"+name.toLowerCase());
        child.genCode(code);
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

        f.type = NameType.parse(s, f);

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
        Main.log.prettyPrintLn(" : " + type.name + ";");
        child.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
