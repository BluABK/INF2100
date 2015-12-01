package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * Name [ '[' {@link Expression} ']' ]
 */
public class Variable extends Factor {
    public String name;
    public Expression expr;

    public PascalDecl decl;

    Variable(int n, int c) {
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
        // Feedback from part 3: We are aware that conversions end up in two log lines for the same variable.
        // We could make a silent switch for findDecl, but this seems a bit stupid
        PascalDecl p = scope.findDecl(name, this);

        // Correct variables that should be constants or function calls, but only if context has factor
        // The only classes storing Variable as a Factor are Negation and Term
        if(context instanceof Negation || context instanceof Term && expr == null) {
            if(p instanceof ConstDecl || (p instanceof Enum)) {
                Main.log.noteBinding2("Converting Variable to ConstantName", this);
                // Enums: These are constants
                ConstantName n = new ConstantName(lineNum, colNum);
                n.context = context;
                n.name = name;

                if(context instanceof Negation)
                    ((Negation)context).factor = n;
                else if(context instanceof Term) {
                    Term c = (Term)context;
                    for(int i = 0; i < c.factors.size(); i++) {
                        if(c.factors.get(i) == this) {
                            c.factors.set(i, n);
                            break;
                        }
                    }
                }
                n.check(scope, lib);
                return;
            }
            else if(p instanceof FuncDecl) {
                Main.log.noteBinding2("Converting Variable to FuncCall", this);
                FuncCall f = new FuncCall(lineNum, colNum);
                f.context = context;
                f.name = name;
                f.expressions = null;

                if(context instanceof Negation)
                    ((Negation)context).factor = f;
                else if(context instanceof Term) {
                    Term c = (Term)context;
                    for(int i = 0; i < c.factors.size(); i++) {
                        if(c.factors.get(i) == this) {
                            c.factors.set(i, f);
                            break;
                        }
                    }
                }
                f.check(scope, lib);
                return;
            }
        }


        if(!(p instanceof VarDecl) && !(p instanceof ParamDecl) && !(p instanceof FuncDecl)) {
            error("Variable instance " + name + " is not declared as a variable, parameter or function");
        }
        decl = p;

        if(expr != null) {

            expr.check(scope, lib);

            decl.getType().checkType(new ArrayType(lineNum, colNum), this, "When using v[...], v needs to be an array");
        } else {
            // whole array can assign to whole array, so it can still be an array type
        }
    }


    public void genCode(CodeFile f) {
        f.genInstr("movl", (-4 * decl.declLevel) + "(%ebp),%edx");
        f.genInstr("movl", decl.declOffset + "(%edx),%eax");
    }

    public static Variable parse(Scanner s, PascalSyntax context) {
        enterParser("Variable");

        Variable v = new Variable(s.curLineNum(), s.curColNum());
        v.context = context;

        s.test(TokenKind.nameToken);
        v.name = s.curToken.id;
        s.readNextToken();

        if (s.curToken.kind == TokenKind.leftBracketToken) {
            s.skip(TokenKind.leftBracketToken);
            v.expr = Expression.parse(s, v);
            s.skip(TokenKind.rightBracketToken);
        } else {
            v.expr = null;
        }

        leaveParser("Variable");
        return v;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(name);
        if (expr != null) {
            Main.log.prettyPrint("[");
            expr.prettyPrint();
            Main.log.prettyPrint("]");
        }
    }
}
