package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name '=' {@link Type} ';'
 */
public class TypeDecl extends PascalDecl {
    public Type type;

    @Override
    public Type getType() {
        return type;
    }

    TypeDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    public boolean testString() {
        return type.testString();
    }

    @Override
    public boolean testChar() {
        return type.testChar();
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Type " + name + " is not assignable");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Type " + name + " is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Type " + name + " is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {
        where.error("Types are not values");
    }

    public static TypeDecl parse(Scanner s, PascalSyntax context) {
        enterParser("TypeDecl");

        // <type name> aka <name>
        s.test(TokenKind.nameToken);
        TypeDecl t = new TypeDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        t.context = context;
        s.readNextToken();

        // =
        s.skip(TokenKind.equalToken);

        // <type>
        t.type = Type.parse(s, t);

        // ;
        s.skip(TokenKind.semicolonToken);

        leaveParser("TypeDecl");
        return t;
    }

    @Override
    public void check(Block scope, Library lib) {
        type.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        Main.TODO();
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name + " = ");
        type.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
