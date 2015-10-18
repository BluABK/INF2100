package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

public class Block extends PascalSyntax {
    public ConstDeclPart constants;
    public TypeDeclPart  types;
    public VarDeclPart   variables;
    public ArrayList<FuncDecl> functions;
    public ArrayList<ProcDecl> procedures;

    public StatmList statements;


    Block(int n, int c) {
        super(n, c);
        functions  = new ArrayList<FuncDecl>();
        procedures = new ArrayList<ProcDecl>();
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        if(constants != null)
            constants.prettyPrint();
        if(types != null)
            types.prettyPrint();
        if(variables != null)
            variables.prettyPrint();

        for(FuncDecl f : functions) {
            f.prettyPrint();
        }
        for(ProcDecl p : procedures) {
            p.prettyPrint();
        }
        Main.log.prettyPrintLn("begin");
        Main.log.prettyIndent();
        statements.prettyPrint();
        Main.log.prettyOutdent();
        Main.log.prettyPrint("end");
    }

    public static Block parse(Scanner s, PascalSyntax context) {
        enterParser("Block");

        Block b = new Block(s.curLineNum(), s.curColNum());
        b.context = context;

        if(s.curToken.kind == TokenKind.constToken) {
            b.constants = ConstDeclPart.parse(s, b);
        }

        if(s.curToken.kind == TokenKind.typeToken) {
            b.types = TypeDeclPart.parse(s, b);
        }
        if(s.curToken.kind == TokenKind.varToken) {
            b.variables = VarDeclPart.parse(s, b);
        }

        while(s.curToken.kind == TokenKind.functionToken || s.curToken.kind == TokenKind.procedureToken) {
            if(s.curToken.kind==TokenKind.functionToken)
                b.functions.add(FuncDecl.parse(s, b));
            else
                b.procedures.add(ProcDecl.parse(s, b));
        }

        s.skip(TokenKind.beginToken);
        b.statements = StatmList.parse(s, b);
        s.skip(TokenKind.endToken);

        leaveParser("Block");
        return b;
    }
}
