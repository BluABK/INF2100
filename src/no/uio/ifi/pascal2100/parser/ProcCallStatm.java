package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * Name [ '(' {@link Expression} [ ',' {@link Expression} ].. ')' ]
 */
public class ProcCallStatm extends Statement {
    public String name;
    public ArrayList<Expression> expressions;

    public ProcDecl decl;

    ProcCallStatm(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block scope, Library lib) {
        PascalDecl pd = scope.findDecl(name, this);
        if(!(pd instanceof ProcDecl)) {
            error("ProcCall tried to call something which is not a ProcDecl");
            return;
        }
        decl = (ProcDecl)pd;

        if(expressions != null)
            for(Expression e: expressions) {
                e.check(scope, lib);
            }
    }

    @Override
    public void genCode(CodeFile f) {

        int numArgs = 0;
        if(expressions != null)
            numArgs = expressions.size();

        int numExpected = 0;
        if(decl.params != null)
            numExpected = decl.params.parameters.size();

        if(decl.name.equals("write")) {
            if(expressions != null) for(Expression e: expressions) {
                e.genCode(f);

                // Depending on the type of e, we have to decide between write_* (see library)
                // string: expression has only one simpleexpression, only one term, factor etc and that is a string
                // char:
                // TODO more type checking for expression
                f.genInstr("push", "%eax");
                if(e.testString())
                    f.genInstr("call", "write_string");
                else if(e.testChar())
                    f.genInstr("call", "write_char");
                else
                    f.genInstr("call", "write_int");
                f.genInstr("addl", "$4,%esp");
            }
            return;
        }

        if(numArgs != numExpected)
            Main.error("Incorrect number of arguments in call to "+decl.name);

        if(expressions == null) {
            // No arguments
            f.genInstr("call", decl.progProcFuncName);
            return;
        }

        // push in reverse order
        int i;
        for(i=expressions.size()-1; i>=0;i--) {
            Expression e = expressions.get(i);
            e.genCode(f);
            f.genInstr("push", "%eax");
        }
        f.genInstr("call", decl.progProcFuncName);
        if(numExpected > 0)
            f.genInstr("addl", "$" + decl.params.totalArgSize + ",%esp");
    }

    public static ProcCallStatm parse(Scanner s, PascalSyntax context) {
        enterParser("ProcCallStatm");

        ProcCallStatm p = new ProcCallStatm(s.curLineNum(), s.curColNum());
        p.context = context;

        s.test(TokenKind.nameToken);
        p.name = s.curToken.id;
        s.readNextToken();

        if (s.curToken.kind == TokenKind.leftParToken) {
            p.expressions = new ArrayList<>();
            s.skip(TokenKind.leftParToken);

            while (s.curToken.kind != TokenKind.rightParToken) {
                p.expressions.add(Expression.parse(s, p));

                if (s.curToken.kind != TokenKind.commaToken)
                    break;
                s.readNextToken();
            }
            s.skip(TokenKind.rightParToken);
        } else {
            p.expressions = null;
        }

        leaveParser("ProcCallStatm");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name);
        if (expressions != null) {
            Main.log.prettyPrint("(");
            boolean first = true;
            for (Expression e : expressions) {
                if (!first) {
                    Main.log.prettyPrint(", ");
                }

                e.prettyPrint();

                first = false;
            }
            Main.log.prettyPrint(")");
        }
    }
}
