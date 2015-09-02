// TODO: ALL CLASSES MUST HAVE AN public String identify() METHOD RETURNING SOME DEBUG INFO ABOUT SELF

package no.uio.ifi.pascal2100.scanner;

import no.uio.ifi.pascal2100.main.Main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Scanner {
    public Token curToken = null, nextToken = null;

    private LineNumberReader sourceFile = null;
    private String sourceFileName, sourceLine = "", originalSourceLine = "";
    private int sourceCol = 1;

    /* inComment: Indicates if we are currently looking for the end of a comment.
     *  0 - Normal operation
     *  1 - Looking for * / (without the space)
     *  2 - Looking for }
     */
    private int inComment = 0;

    public Scanner(String fileName) {
        sourceFileName = fileName;
        try {
            sourceFile = new LineNumberReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            Main.error("Cannot read " + fileName + "!");
        }

        readNextToken();
        readNextToken();
    }


    public String identify() {
        return "Scanner reading " + sourceFileName;
    }


    public int curLineNum() {
        return curToken.lineNum;
    }


    private void error(String message) {
        Main.error("Scanner error on line " + curLineNum() + ": " + message);
    }

    /* Modifies sourceLine and sourceCol.
     * Trims whitespace from the beginning of sourceLine. Updates sourceCol with the current
     * position in the source file.
     *
     * If the line is empty, it does nothing.
     */
    private void trimLine() {
        int i;
        for (i = 0; i < sourceLine.length(); i++) {
            char c = sourceLine.charAt(i);
            // PS: ' ' is not a normal space. It has the
            // hexadecimal value 0xa0c2 (stored in Big Endian)
            if (c != ' ' && c != '\n' && c != '\t' && c != ' ' && c != '\r')
                break;
        }
        // i: number of chars cut, or offset to start from in sourceLine
        sourceCol += i;
        sourceLine = sourceLine.substring(i, sourceLine.length());
    }

    private void setToken(Token t) {
        nextToken = t;
        Main.log.noteToken(t);
    }

    private void checkCommentEnd() {
        String target;
        if (inComment == 1)
            target = "*/";
        else
            target = "}";

        int pos = sourceLine.indexOf(target);
        if (pos >= 0) {
            sourceLine = sourceLine.substring(pos + target.length(), sourceLine.length());
            sourceCol += pos + target.length();
            inComment = 0;
        } else {
            sourceCol = -1;
            sourceLine = "";
        }
    }

    /* Returns: true if there was a positive match, false otherwise */
    private boolean checkComment() {
        if (sourceLine.startsWith("/*")) {
            inComment = 1;
            sourceLine = sourceLine.substring(2, sourceLine.length());
            sourceCol += 2;
            return true;
        } else if (sourceLine.startsWith("{")) {
            inComment = 2;
            sourceLine = sourceLine.substring(1, sourceLine.length());
            sourceCol += 1;
            return true;
        }
        return false;
    }

    public void readNextToken() {
        // Covers the following:
        // 1: Cut whitespace,
        // 2: if string is empty, read a new line -> step 1
        // 3: If no new line is available, eof token -> step 1

        // A loop that goes to the beginning if any of the cases occur.
        // If no cases occur, the loop ends.
        while (true) {
            if (sourceLine.length() == 0) {
                if (sourceFile == null) {
                    setToken(new Token(TokenKind.eofToken, getFileLineNum(), sourceCol));
                    return;
                }
                readNextLine();
            }

            // Eat whitespace and go back if line runs out
            trimLine();
            if (sourceLine.length() == 0)
                continue;

            // 3.9: Search for the end of an ongoing comment
            if (inComment > 0) {
                checkCommentEnd();
                continue;
            }

            if (checkComment())
                continue;

            // NOTE: To check if a comment went unclosed for the rest of the file, check if inComment != 0 on eof
            // TODO: Should we split some of this functionality into separate methods?
            break;
        }
        // At this stage:
        //  - Line is not empty
        //  - Line does not start with whitespace
        //  - There are no comments
        // Only part 5 remains.


        // 1: Run string matcher. If this matches, actually return
        // Takes care of text strings, they always start with '.
        // If it is a match, it cannot be anything else.
        StringMatcher stringMatch = new StringMatcher(sourceLine, getFileLineNum(), sourceCol);
        if (stringMatch.getConsumed() > 0) {
            if (!stringMatch.getTerminated()) {
                Main.panic(getFileLineNum(), sourceCol, originalSourceLine, "Unterminated string");
            } else {
                setToken(stringMatch.getToken());
                sourceCol += stringMatch.getConsumed();
                sourceLine = sourceLine.substring(stringMatch.getConsumed(), sourceLine.length());
                return;
            }
        }

        // Numeric matcher
        int numConsumed = scanDigit();
        if (numConsumed > 0) {
            int n = Integer.parseInt(sourceLine.substring(0, numConsumed));
            setToken(new Token(n, getFileLineNum(), sourceCol));
            sourceLine = sourceLine.substring(numConsumed, sourceLine.length());
            sourceCol += numConsumed;
            return;
        }

        numConsumed = scanName();
        if (numConsumed > 0) {
            String s = sourceLine.substring(0, numConsumed).toLowerCase();
            // Token does the work of splitting this into the static tags
            setToken(new Token(s, getFileLineNum(), sourceCol));
            sourceLine = sourceLine.substring(numConsumed, sourceLine.length());
            sourceCol += numConsumed;
            return;
        }
        // Match all of the small things

        Main.panic(getFileLineNum(), sourceCol, originalSourceLine, "Unexpected data found");
    }

    private void readNextLine() {
        if (sourceFile != null) {
            try {
                originalSourceLine = sourceLine = sourceFile.readLine();
                if (sourceLine == null) {
                    sourceFile.close();
                    sourceFile = null;
                    sourceLine = "";
                } else {
                    // TODO why do you do this? If it affects unterminated string recovery, it has to go away.
                    sourceLine += " ";
                }
                sourceCol = 1;
            } catch (IOException e) {
                Main.error("Scanner error: unspecified I/O error!");
            }
        }
        if (sourceFile != null)
            Main.log.noteSourceLine(getFileLineNum(), sourceLine);
    }

    private int getFileLineNum() {
        return (sourceFile != null ? sourceFile.getLineNumber() : 0);
    }

    // Character test utilities:
    private int scanName() {
        int i;
        for (i = 0; i < sourceLine.length(); i++) {
            char c = Character.toLowerCase(sourceLine.charAt(i));
            if ((c < '0' || c > '9') &&
                    (c < 'a' || c > 'z'))
                break;
        }
        return i;
    }

    /*
    * Return number of characters from start of sourceLine are digit characters.
    * Anything above zero is a digit
    * */
    private int scanDigit() {
        int i;
        for (i = 0; i < sourceLine.length(); i++) {
            char c = sourceLine.charAt(i);
            if (c < '0' || c > '9')
                break;
        }
        return i;
    }


    // Parser tests:

    public void test(TokenKind t) {
        if (curToken.kind != t)
            testError(t.toString());
    }

    public void testError(String message) {
        Main.error(curLineNum(), "Expected a " + message + " but found a " + curToken.kind + "!");
    }

    public void skip(TokenKind t) {
        test(t);
        readNextToken();
    }
}
