package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;

public abstract class PascalSyntax {
    public PascalSyntax context;

    public int lineNum;
    public int colNum;

    public PascalSyntax(int n, int c) {
        lineNum = n; colNum = c;
    }

    static void enterParser(String nonTerm) {
        Main.log.enterParser(nonTerm);
    }

    static void leaveParser(String nonTerm) {
        Main.log.leaveParser(nonTerm);
    }

    boolean isInLibrary() {
        return lineNum < 0;
    }

    //Del 3: abstract void check(Block curScope, Library lib);
    //Del 4: abstract void genCode(CodeFile f);

    abstract public String identify();

    public String identifyTemplate() {
        return "<" + this.getClass().getSimpleName() + "> on line "
                + lineNum + ", col " + colNum;
    }

    abstract public void prettyPrint();

    void error(String message) {
        Main.error("Error at line " + lineNum + ", col " + colNum + ": " + message);
    }
}
