package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * 'type' {@link TypeDecl}...
 */
public class TypeDeclPart extends PascalSyntax {
    public ArrayList<TypeDecl> types;

    TypeDeclPart(int n, int c) {
        super(n, c);
        types = new ArrayList<>();
    }

    @Override
    public void check(Block scope, Library lib) {
        for(TypeDecl t: types)
            t.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        Main.TODO();
    }


    public static TypeDeclPart parse(Scanner s, PascalSyntax context) {
        enterParser("TypeDeclPart");

        TypeDeclPart t = new TypeDeclPart(s.curLineNum(), s.curColNum());
        t.context = context;

        s.skip(TokenKind.typeToken);

        while (s.curToken.kind == TokenKind.nameToken &&
                s.nextToken.kind == TokenKind.equalToken) {
            t.types.add(TypeDecl.parse(s, t));
        }

        leaveParser("TypeDeclPart");
        return t;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrintLn("type");

        Main.log.prettyIndent();
        for (TypeDecl t : types) {
            t.prettyPrint();
        }
        Main.log.prettyOutdent();
    }
}
