package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

public class Block extends PascalSyntax {
    public ArrayList<ConstantDecl> constants;
    public ArrayList<TypeDecl> types;
    public ArrayList<VariableDecl> vars;
    public ArrayList<Function> functions;
    public ArrayList<Procedure> procedures;
    public StatementList statements;


    Block(int n, int c) {
        super(n, c);
        constants  = new ArrayList<ConstantDecl>();
        types      = new ArrayList<TypeDecl>();
        vars       = new ArrayList<VariableDecl>();
        functions  = new ArrayList<Function>();
        procedures = new ArrayList<Procedure>();
    }

    @Override
    public String identify() {
        return "<block> on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        System.out.println("Fancy! Print! Wow!");
    }

    public static Block parse(Scanner s) {
        enterParser("Block");

        Block b = new Block(s.curLineNum(), s.curColNum());

        if(s.curToken.kind == TokenKind.constToken) {
            s.readNextToken();
            while(s.curToken.kind == TokenKind.nameToken) {
                ConstantDecl c = ConstantDecl.parse(s);
                c.context = b;
                b.constants.add(c);
            }
        }
        if(s.curToken.kind == TokenKind.typeToken) {
            while(s.curToken.kind == TokenKind.nameToken) {
                TypeDecl t = TypeDecl.parse(s);
                t.context = b;
                b.types.add(t);
            }
        }
        if(s.curToken.kind == TokenKind.varToken) {
            while(s.curToken.kind == TokenKind.nameToken) {
                VariableDecl v = VariableDecl.parse(s);
                v.context = b;
                b.vars.add(v);
            }
        }

        while(s.curToken.kind == TokenKind.functionToken || s.curToken.kind == TokenKind.procedureToken) {
            if(s.curToken.kind==TokenKind.functionToken) {
                Function f = Function.parse(s);
                f.context = b;
                b.functions.add(f);
            } else {
                Procedure p = Procedure.parse(p);
                p.context = b;
                b.procedures.add(p);
            }
        }

        s.skip(TokenKind.beginToken);
        b.statements = StatementList.parse(s);
        b.statements.context = b;
        // fill with statements
        s.skip(TokenKind.endToken);

        leaveParser("Block");
        return b;
    }
}
