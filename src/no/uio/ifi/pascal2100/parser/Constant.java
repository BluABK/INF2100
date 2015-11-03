package no.uio.ifi.pascal2100.parser;


import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

/**
 * {@link ConstantName} | {@link ConstantInt} | {@link ConstantStr}
 */
public abstract class Constant extends Factor {
    Constant(int n, int c) {
        super(n, c);
    }

    public static Constant parse(Scanner s, PascalSyntax context) {
        enterParser("Constant");

        Constant c = null;

        if (s.curToken.kind == TokenKind.nameToken) {
            c = ConstantName.parse(s, context);
        } else if (s.curToken.kind == TokenKind.intValToken) {
            c = ConstantInt.parse(s, context);
        } else if (s.curToken.kind == TokenKind.stringValToken) {
            c = ConstantStr.parse(s, context);
        } else {
            s.testError(TokenKind.nameToken.toString() + ", " +
                    TokenKind.intValToken.toString() + ", or " +
                    TokenKind.stringValToken.toString());
        }

        leaveParser("Constant");
        return c;
    }

    public abstract String toString();

    abstract void checkType(Constant cmp, PascalSyntax where, String message);

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(toString());
    }
}
