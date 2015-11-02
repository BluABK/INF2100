package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * 'array [' {@link Type} '] of' {@link Type}
 */
public class ArrayType extends Type {
    // array[type] of type - Does not specify the need for the [type] to be a number
    public Type number;
    public Type type;

    ArrayType(int n, int c) {
        super(n, c);
    }


    @Override
    public void check(Block scope, Library lib) {
        number.check(scope, lib);
        type.check(scope, lib);
    }

    /**
     * @param s : Scanner object
     * @param context : Parent object
     * @return ArrayType node
     */
    public static ArrayType parse(Scanner s, PascalSyntax context) {
        enterParser("ArrayType");

        ArrayType t = new ArrayType(s.curLineNum(), s.curColNum());
        t.context = context;

        s.skip(TokenKind.arrayToken);
        s.skip(TokenKind.leftBracketToken);
        t.number = Type.parse(s, t);
        s.skip(TokenKind.rightBracketToken);
        s.skip(TokenKind.ofToken);
        t.type = Type.parse(s, t);

        leaveParser("ArrayType");
        return t;
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint("array[");
        number.prettyPrint();
        Main.log.prettyPrint("] of ");
        type.prettyPrint();
    }
}
