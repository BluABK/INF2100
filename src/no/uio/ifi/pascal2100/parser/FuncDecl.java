package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class FuncDecl extends PascalDecl {
    public Block child;
    public String returnType;

    public ParamDeclList params;

    FuncDecl(String name, int n, int c) {
        super(name, n, c);
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


        s.test(TokenKind.nameToken);
        f.returnType = s.curToken.id;
        s.readNextToken();

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
        Main.log.prettyPrintLn(" : " + returnType + ";");
        child.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
