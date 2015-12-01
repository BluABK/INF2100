package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name
 */
public class NameType extends Type {
    public String name;

    TypeDecl decl;

    @Override
    public int getStackSize() {
        if(decl.name.equals("integer")
                || decl.name.equals("char")
                || decl.name.equals("boolean"))
            return 4;

        return decl.getType().getStackSize();
    }

    NameType(int n, int c) {
        super(n, c);
    }

    @Override
    public boolean testString() {
        if(name.equals("integer"))
            return false;

        System.out.println(name);
        return decl.testString();
    }

    @Override
    public boolean testChar() {
        if(name.equals("integer"))
            return false;

        System.out.println(name);
        return decl.testChar();
    }

    @Override
    public void check(Block scope, Library lib) {
        PascalDecl pd = scope.findDecl(name, this);
        if(!(pd instanceof TypeDecl)) {
            error("NameType " + name + " has a non-type declaration");
            return;
        }
        decl = (TypeDecl)pd;
    }

    @Override
    public void genCode(CodeFile f) {
        Main.TODO();
    }

    public static NameType parse(Scanner s, PascalSyntax context) {
        enterParser("NameType");

        NameType r = new NameType(s.curLineNum(), s.curColNum());
        r.context = context;

        s.test(TokenKind.nameToken);
        r.name = s.curToken.id;
        s.readNextToken();

        leaveParser("NameType");
        return r;
    }

    @Override
    public String identify() {
        return "<" + this.getClass().getSimpleName() + "> with type " + name
                + " on line " + lineNum + ", col " + colNum;
    }

    @Override
    void checkType(Type cmp, PascalSyntax where, String message) {
        Main.log.noteTypeCheck(cmp, "match", this, where);
        decl.getType().checkType(cmp, where, message);
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name);
    }
}
