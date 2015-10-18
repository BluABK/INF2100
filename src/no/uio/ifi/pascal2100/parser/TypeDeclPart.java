package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

public class TypeDeclPart extends PascalSyntax {
    public ArrayList<TypeDecl> types;

    TypeDeclPart(int n, int c) {
        super(n, c);
        types = new ArrayList<TypeDecl>();
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrintLn("type");

        Main.log.prettyIndent();
        for(TypeDecl t : types) {
            t.prettyPrint();
        }
        Main.log.prettyOutdent();
    }

    public static TypeDeclPart parse(Scanner s, PascalSyntax context) {
        enterParser("TypeDeclPart");

        TypeDeclPart t = new TypeDeclPart(s.curLineNum(), s.curColNum());
        t.context = context;

        s.skip(TokenKind.typeToken);


        while(s.curToken.kind == TokenKind.nameToken &&
              s.nextToken.kind == TokenKind.equalToken) {
            t.types.add(TypeDecl.parse(s, t));
        }

        leaveParser("TypeDeclPart");
        return t;
    }
}
