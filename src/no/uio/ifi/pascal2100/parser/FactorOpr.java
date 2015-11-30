package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

/**
 * '*' | 'div' | 'mod' | 'and'
 * <br />
 * Subset of {@link Opr}
 */
public class FactorOpr extends Opr {
    FactorOpr(int n, int c) {
        super(n, c);
    }

    @Override
    public void check(Block scope, Library lib) {}

    @Override
    public void genCode(CodeFile f) {
        // eax = ecx <op> eax
        if(op == Op.multiply) {
            f.genInstr("imull %ecx, %eax");
        } else if(op == Op.div) {
            // edx = 0

            f.genInstr("xchgl %eax, %ecx"); // Swap
            f.genInstr("xorl %edx, %edx");
            f.genInstr("idivl %ecx");
        } else if(op == Op.mod) {
            f.genInstr("xchgl %eax, %ecx"); // Swap
            f.genInstr("xorl %edx, %edx");
            f.genInstr("idivl %ecx");
            f.genInstr("movl %edx, %eax");
        } else {
            f.genInstr("andl %ecx, %eax");
        }
    }

    public static FactorOpr parse(Scanner s, PascalSyntax context) {
        enterParser("FactorOpr");

        FactorOpr p = new FactorOpr(s.curLineNum(), s.curColNum());
        p.context = context;

        switch (s.curToken.kind) {
            case multiplyToken:
                p.op = Op.multiply;
                break;
            case divToken:
                p.op = Op.div;
                break;
            case modToken:
                p.op = Op.mod;
                break;
            case andToken:
                p.op = Op.and;
                break;
            default:
                s.testError("*, div, mod, and");
        }
        s.readNextToken();

        leaveParser("FactorOpr");
        return p;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }
}
