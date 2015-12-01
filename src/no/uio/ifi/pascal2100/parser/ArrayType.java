package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;


/**
 * 'array [' {@link Type} '] of' {@link Type}
 */
public class ArrayType extends Type {
    // array[type] of type - Does not specify the need for the [type] to be a number
    public Type number;
    public RangeType numberR;

    public Type type;

    ArrayType(int n, int c) {
        super(n, c);
    }

    /**
     * @param s       : Scanner object
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
    public int getStackSize() {
        int start = numberR.startI;
        int stop = numberR.stopI;
        int size = stop - start + 1;
        return 4 * size;
    }

    @Override
    public boolean testString() {
        return type.testString();
    }

    @Override
    public boolean testChar() {
        return type.testChar();
    }

    @Override
    public Type getNonName() {
        return this;
    }

    @Override
    public void check(Block scope, Library lib) {
        number.check(scope, lib);
        number.checkType(new RangeType(lineNum, colNum), this, "In array[number], number needs to be a range type");
        // Cast it, because the line above guarantees its success.
        numberR = (RangeType) number.getNonName();
        type.check(scope, lib);
    }

    @Override
    public void genCode(CodeFile f) {
    }

    @Override
    void checkType(Type cmp, PascalSyntax where, String message) {
        Main.log.noteTypeCheck(cmp, "match", this, where);
        if (!(cmp instanceof ArrayType))
            where.error(message);
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
