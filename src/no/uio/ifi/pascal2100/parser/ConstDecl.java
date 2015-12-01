package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name '=' {@link Constant} ';'
 */
public class ConstDecl extends PascalDecl {
    public Constant child;

    @Override
    public Type getType() {
        return null;
    }

    ConstDecl(String name, int n, int c) {
        super(name, n, c);
    }

    @Override
    public boolean testString() {
        return child.testString();
    }

    @Override
    public boolean testChar() {
        return child.testChar();
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Constant " + name + " is not assignable");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("Constant " + name + " is not function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("Constant " + name + " is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {}

    @Override
    public void check(Block scope, Library lib) {
        child.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        if(name.equals("eol"))
            f.genInstr("movl", "$10,%eax");
        else
            Main.TODO();
    }

    public static ConstDecl parse(Scanner s, PascalSyntax context) {
        enterParser("ConstDecl");

        // <name>
        s.test(TokenKind.nameToken);
        ConstDecl c = new ConstDecl(s.curToken.id, s.curLineNum(), s.curColNum());
        c.context = context;
        s.readNextToken();

        // =
        s.skip(TokenKind.equalToken);

        // <constant>
        c.child = Constant.parse(s, c);

        // ;
        s.skip(TokenKind.semicolonToken);

        leaveParser("ConstDecl");
        return c;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name + " = ");
        child.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}
