package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;

/**
 * Superclass of all the operator classes
 */
public abstract class Opr extends PascalSyntax {
    public Op op;

    public Opr(int n, int c) {
        super(n, c);
    }

    @Override
    public String identifyTemplate() {
        return "<" + this.getClass().getSimpleName() + "> (" + op + ") on line " + lineNum + ", col " + colNum;
    }

    @Override
    public void prettyPrint() {
        Main.log.prettyPrint(op.toString());
    }

    /**
     * Available operators
     */
    public enum Op {
        multiply("*"),
        div("div"),
        mod("mod"),
        and("and"),
        add("+"),
        subtract("-"),
        equal("="),
        notEqual("<>"),
        lessEqual("<="),
        less("<"),
        greaterEqual(">="),
        greater(">"),
        or("or");

        private final String name;

        Op(String s) {
            name = s;
        }

        // If you want to do op.equalsName("="):
        /* public boolean equalsName(String other) { ... } */

        public String toString() {
            return this.name;
        }
    }

}
