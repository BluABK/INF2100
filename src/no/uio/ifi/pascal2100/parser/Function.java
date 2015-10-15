package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class Function extends PascalDecl {
    public Block child;
    public String returntype;

    public ArrayList<ParamDecl> params;


    Function(String name, int n, int c) {
        super(name, n, c);
        params = new ArrayList<ParamDecl>();
    }

    @Override
    public String identify() {
        return "<constant> "+name+" on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        System.out.println("Fancy! Print! Wow!");
    }

    public static Function parse(Scanner s) {
        enterParser("Function");

        Function f = new Function(s.curToken.id, s.curLineNum(), s.curColNum());
        s.readNextToken();

        if(s.curToken.kind == TokenKind.leftParToken) {
            s.readNextToken();
            while(s.curToken.kind == TokenKind.nameToken) {
                ParamDecl p = ParamDecl.parse(s);
                p.context = f;
                f.params.add(p);

                if(s.curToken.kind == TokenKind.semicolonToken) {
                    s.readNextToken();
                } else break;
            }
            s.skip(TokenKind.rightParToken);
        }

        s.skip(TokenKind.colonToken);

        s.test(TokenKind.nameToken);
        f.returntype = s.curToken.id;
        s.readNextToken();

        s.skip(TokenKind.semicolonToken);

        f.child = Block.parse(s);
        f.child.context = f;
        s.skip(TokenKind.semicolonToken);

        leaveParser("Function");
        return f;
    }
}
