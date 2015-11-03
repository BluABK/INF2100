package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.main.Tools;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

/**
 * '(' Name [ ',' Name ]... ')'
 */
public class EnumType extends Type {
    public ArrayList<Enum> literals;

    EnumType(int n, int c) {
        super(n, c);
        literals = new ArrayList<>();
    }

    @Override
    public void check(Block scope, Library lib) {}

    public static EnumType parse(Scanner s, PascalSyntax context) {
        enterParser("EnumType");

        EnumType e = new EnumType(s.curLineNum(), s.curColNum());
        e.context = context;

        s.skip(TokenKind.leftParToken);

        while (s.curToken.kind != TokenKind.rightParToken) {
            e.literals.add(Enum.parse(s, e));

            if (s.curToken.kind != TokenKind.commaToken)
                break;
            s.readNextToken();
        }
        s.skip(TokenKind.rightParToken);

        leaveParser("EnumType");
        return e;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("(");
        for(int i = 0; i < literals.size(); i++) {
            Main.log.prettyPrint(literals.get(i).toString());
            if(i < literals.size()-1)
                Main.log.prettyPrint(", ");
        }
        Main.log.prettyPrint(")");
    }

    @Override
    void checkType(Type cmp, PascalSyntax where, String message) {
        Main.log.noteTypeCheck(cmp, "match", this, where);
        if(!(cmp instanceof EnumType))
            where.error(message);
    }
}
