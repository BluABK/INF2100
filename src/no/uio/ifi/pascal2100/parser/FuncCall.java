package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * Name [ '(' {@link Expression} [ ',' {@link Expression} ]... ')' ]
 */
public class FuncCall extends Factor {
    public String name;
    public ArrayList<Expression> expressions;

    FuncDecl decl;

    FuncCall(int n, int c) {
        super(n, c);
    }

    @Override
    public boolean testString() {
        return decl.testString();
    }

    @Override
    public boolean testChar() {
        return decl.testChar();
    }

    @Override
    public void check(Block scope, Library lib) {
        PascalDecl func = scope.findDecl(name, this);

        // As a factor:
        //   constant / variable + innerExpr: Would be illegal because it requires two factors in place of one.
        //   Therefore, function is always the correct interpretation

        if(!(func instanceof FuncDecl)) {
            error(name + " is not a function");
            return;
        }
        decl = (FuncDecl)func;
        for(Expression e: expressions)
            e.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
        if(expressions.size() != decl.params.parameters.size())
            Main.error("Incorrect number of arguments in call to "+decl.name);

        // push the desired amount of things to the stack before the call in reverse order
        int i;
        for(i=expressions.size()-1; i>=0;i--) {
            Expression e = expressions.get(i);
            e.genCode(f);
            f.genInstr("push", "%eax");
        }
        f.genInstr("call", decl.progProcFuncName);
        if(decl.params.totalArgSize > 0)
            f.genInstr("addl", "$"+(4*decl.params.totalArgSize)+",%esp");
    }

    public static FuncCall parse(Scanner s, PascalSyntax context) {
        enterParser("FuncCall");

        FuncCall f = new FuncCall(s.curLineNum(), s.curColNum());
        f.context = context;

        s.test(TokenKind.nameToken);
        f.name = s.curToken.id;
        s.readNextToken();

        if (s.curToken.kind == TokenKind.leftParToken) {
            f.expressions = new ArrayList<>();
            s.skip(TokenKind.leftParToken);

            while (s.curToken.kind != TokenKind.rightParToken) {
                f.expressions.add(Expression.parse(s, f));

                if (s.curToken.kind != TokenKind.commaToken) {
                    break;
                }
                s.readNextToken();
            }
            s.skip(TokenKind.rightParToken);
        } else {
            f.expressions = null;
        }

        leaveParser("FuncCall");
        return f;
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
