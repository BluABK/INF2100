package no.uio.ifi.pascal2100.scanner;

import no.uio.ifi.pascal2100.main.Main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Scanner {
    public Token curToken = null, nextToken = null;

    private LineNumberReader sourceFile = null;
    private String sourceFileName, sourceLine = "";
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
            if(c != ' ' && c != '\n' && c != '\t' && c != ' ' && c != '\r')
                break;
        }
        // i: number of chars cut, or offset to start from in sourceLine
        sourceCol += i;
        sourceLine = sourceLine.substring(i, sourceLine.length());
    }

    private void setToken(Token t){
        nextToken = t;
        Main.log.noteToken(t);
    }

    public void readNextToken() {
        // Covers the following:
        // 1: Cut whitespace,
        // 2: if string is empty, read a new line -> step 1
        // 3: If no new line is available, eof token -> step 1

        // A loop that goes to the beginning if any of the cases occur.
        // If no cases occur, the loop ends.
        while (true) {
            if (sourceLine.length() == 0){
                if (sourceFile == null) {
                    setToken(new Token(TokenKind.eofToken, getFileLineNum(), sourceCol));
                    return;
                }
                readNextLine();
            }

            // Eat whitespace and go back if line runs out
            trimLine();
            if(sourceLine.length() == 0) continue;

            // 3.9: Search for the end of an ongoing comment
            if(inComment > 0) {
                String tgt;
                if(inComment == 1)
                    tgt = "*/";
                else if(inComment == 2)
                    tgt = "}";
                // TODO: Search for tgt, reset inComment, modify sourceLine and sourceCol and do a continue;
                // If target is not found, set sourceLine="" and continue;
                // Some stuff goes here....

                inComment = 0;
                continue;
            }

            // 4: Check for comments
            // TODO: Pattern matching, set inComment and continue;

            // NOTE: To check if a comment went unclosed for the rest of the file, check if inComment != 0 on eof

            // TODO: Should we split some of this functionality into separate functions?


            break;
        }

        // At this stage:
        //  - Line is not empty
        //  - Line does not start with whitespace
        //  - There are no comments
        // Only part 5 remains.
        //
        // Part 5 subparts:
        //  - StringParser to find out if it is a string or not
        //  - RegexParser to find out if it is a number or name
        //  - StaticParser to find out if it matches a static string
        //  They should all be subclasses of Parser and should have
        //  - Parser(priority): In ties, higher priority wins
        //  - parse(String): Run it against a string to match
        //  - getPriority()
        //  - TokenKind getKind() - So we don't have to do it by ourselves
        //  - getString() - Get the token data
        //  - charsClaimed: Return the number of chars it wants to consume of the input string. 0 if no match
        //  For StringParser:
        //  - checkError: We might not need it if it can do it by itself. Intend to use on unclosed strings.
        //
        // Run every one of the parsers. Keep the ones with the highest number of charsClaimed()
        // Then keep the ones with the highest priority.
        // If we end up with multiple here, there is a tie. If this happens,
        // we have probably made an implementation error.
        //
        // Then generate a token and do setToken()
        //
        //
        // TADAAAH! That's part 1!
    }


    private void readNextLine() {
        if (sourceFile != null) {
            try {
                sourceLine = sourceFile.readLine();
                if (sourceLine == null) {
                    sourceFile.close();
                    sourceFile = null;
                    sourceLine = "";
                } else {
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
    private boolean isLetterAZ(char c) {
        return 'A' <= c && c <= 'Z' || 'a' <= c && c <= 'z';
    }


    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
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
