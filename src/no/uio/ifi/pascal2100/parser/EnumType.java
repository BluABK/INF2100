package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.main.Tools;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;

public class EnumType extends Type {
    public ArrayList<String> literals;

    EnumType(int n, int c) {
        super(n, c);
        literals = new ArrayList<>();
    }

    public static EnumType parse(Scanner s, PascalSyntax context) {
        enterParser("EnumType");

        EnumType e = new EnumType(s.curLineNum(), s.curColNum());
        e.context = context;

        s.skip(TokenKind.leftParToken);

        while (s.curToken.kind != TokenKind.rightParToken) {
            s.test(TokenKind.nameToken);
            e.literals.add(s.curToken.id);
            s.readNextToken();

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
        Main.log.prettyPrint(stringCast());
    }

    public String stringCast() {
        return "(" + Tools.implode(", ", literals) + ")";
    }
}
