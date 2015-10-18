package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public abstract class Type extends PascalSyntax {

    Type(int n, int c) {
        super(n, c);
    }

    public static Type parse(Scanner s, PascalSyntax context) {
        enterParser("Type");

        Type t = null;

        if(s.curToken.kind == TokenKind.arrayToken) {
            t = ArrayType.parse(s, context);
        } else if(s.curToken.kind == TokenKind.leftParToken) {
            t = EnumType.parse(s, context);
        } else if(s.nextToken.kind == TokenKind.rangeToken) {
            t = RangeType.parse(s, context);
        } else if(s.curToken.kind == TokenKind.nameToken) {
            t = NameType.parse(s, context);
        } else {
            s.testError("name, range, enum or array");
        }

        leaveParser("Type");
        return t;
    }
}
