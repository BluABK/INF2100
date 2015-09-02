package no.uio.ifi.pascal2100.scanner;

public class StringMatcher {
    private int consumed;
    private int colNum, lineNum;
    private String stringVal;
    private String line;
    private boolean terminated;

    StringMatcher(String l, int lNum, int cNum) {
        lineNum = lNum;
        colNum = cNum;
        line = l;
        // Immediately run
        parse();
    }

    public void parse() {
        consumed = 0;
        terminated = true;
        stringVal = "";

        int i;
        if (line.charAt(0) != '\'')
            return;

        // Set 1 if we discover the actual end of the string
        boolean closed = false;
        for (i = 1; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c != '\'') {
                stringVal += c;
            } else {
                if (line.length() > i + 1 && line.charAt(i + 1) == '\'') {
                    // Two ' beside each other
                    stringVal += '\'';
                    i++;
                    // The second i++ will be done at the end of the loop. Skips over the next char
                } else {
                    // We have observed the last char of the string, and should return now
                    closed = true;
                    i++;
                    break;
                }
            }
        }
        consumed = i;
        if (!closed)
            terminated = false;
    }

    public boolean getTerminated() {
        return terminated;
    }

    public int getConsumed() {
        return consumed;
    }

    /* Assumes that it actually parsed a valid string.. Otherwise.. I don't know... */
    public Token getToken() {
        return new Token("", stringVal, lineNum, colNum);
    }
}
