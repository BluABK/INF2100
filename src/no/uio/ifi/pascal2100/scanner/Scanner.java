// TODO: ALL CLASSES MUST HAVE AN public String identify() METHOD RETURNING SOME DEBUG INFO ABOUT SELF

package no.uio.ifi.pascal2100.scanner;

import no.uio.ifi.pascal2100.main.Main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Scanner takes a filename and parses it for tokens.
 * */
public class Scanner {
    /**
     * curToken stores the current token, nextToken stores the next token to be shifted in.
     * */
    public Token curToken = null, nextToken = null;

    private LineNumberReader sourceFile = null;
    private String sourceFileName, sourceLine = "", originalSourceLine = "";
    private int sourceCol = 1;

    /** inComment: Indicates if we are currently looking for the end of a comment.
     *  0 - Normal operation
     *  1 - Looking for * / (without the space)
     *  2 - Looking for }
     */
    private int inComment = 0;
    private int commentStartLine = 0;
    private int commentStartCol = 0;

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

    /**
     * Identifies the scanner
     * @return identifying string
     * */
    public String identify() {
        return "Scanner reading " + sourceFileName;
    }

    /**
     * @return Current line number
     * */
    public int curLineNum() {
        return curToken.lineNum;
    }

    /**
     * @return Current column number
     * */
    public int curColNum() {
        return curToken.colNum;
    }

    /**
     * Modifies sourceLine and sourceCol.
     * Trims whitespace from the beginning of sourceLine. Updates sourceCol with the current
     * position in the source file.
     *
     * If the line is empty, it does nothing.
     */
    private void trimLine() {
        int i;
        for (i = 0; i < sourceLine.length(); i++) {
            char c = sourceLine.charAt(i);
            if(Character.isWhitespace(c))
                continue;

            // PS: ' ' is not a normal space. It has the
            // hexadecimal value utf-8: 0xc2a0, utf-16: 0x00a0
            if(c == 0xa0)
                continue;

            break;
        }
        // i: number of chars cut, or offset to start from in sourceLine
        sourceCol += i;
        sourceLine = sourceLine.substring(i, sourceLine.length());
    }

    /**
     * Sets the next token on behalf of readNextToken
     * */
    private void setToken(Token t) {
        nextToken = t;
        Main.log.noteToken(t);
    }

    /**
     * Called if we are in a comment (inComment is positive)
     * Will skip forward until it finds the termination of the comment
     * */
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

    /**
     * Check if we are at a comment. If yes, sets inComment
     * @return true if there was a positive match, false otherwise
     * */
    private boolean checkComment() {
        if (sourceLine.startsWith("/*")) {
            commentStartLine = getFileLineNum();
            commentStartCol = sourceCol;

            inComment = 1;

            sourceLine = sourceLine.substring(2, sourceLine.length());
            sourceCol += 2;
        } else if (sourceLine.startsWith("{")) {
            commentStartLine = getFileLineNum();
            commentStartCol = sourceCol;

            inComment = 2;

            sourceLine = sourceLine.substring(1, sourceLine.length());
            sourceCol += 1;
        } else {
            return false;
        }
        return true;
    }

    /**
     * Match symbols of length 1.
     * @return TokenKind or null
     * */
    private TokenKind matchSym1(char c) {
        switch(c) {
            case '+': return TokenKind.addToken;
            case ':': return TokenKind.colonToken;
            case ',': return TokenKind.commaToken;
            case '.': return TokenKind.dotToken;
            case '=': return TokenKind.equalToken;
            case '>': return TokenKind.greaterToken;
            case '[': return TokenKind.leftBracketToken;
            case '(': return TokenKind.leftParToken;
            case '<': return TokenKind.lessToken;
            case '*': return TokenKind.multiplyToken;
            case ']': return TokenKind.rightBracketToken;
            case ')': return TokenKind.rightParToken;
            case ';': return TokenKind.semicolonToken;
            case '-': return TokenKind.subtractToken;
        }
        return null;
    }

    /**
     * Match symbols of length 2.
     * @return TokenKind or null
     * */
    private TokenKind matchSym2(String s) {
        char a = s.charAt(0), b = s.charAt(1);
        switch(a) {
            case ':':
                if(b == '=')
                    return TokenKind.assignToken;
            case '<':
                if(b == '=')
                    return TokenKind.lessEqualToken;
                else if(b == '>')
                    return TokenKind.notEqualToken;
            case '>':
                if(b == '=')
                    return TokenKind.greaterEqualToken;
            case '.':
                if(b == '.')
                    return TokenKind.rangeToken;
        }
        return null;
    }

    /**
     * Method to read the next token from the file. Will increment curToken.
     * */
    public void readNextToken() {
        // Covers the following:
        // 1: Cut whitespace,
        // 2: if string is empty, read a new line -> step 1
        // 3: If no new line is available, eof token -> step 1

        // A loop that goes to the beginning if any of the cases occur.
        // If no cases occur, the loop ends.
        curToken = nextToken;
        while (true) {
            if (sourceLine.length() == 0) {
                if (sourceFile == null) {
                    if(inComment > 0) {
                        Main.error(commentStartLine, commentStartCol, "Unclosed comment starts here");
                    }

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
            break;
        }
        // At this stage:
        //  - Line is not empty
        //  - Line does not start with whitespace
        //  - There are no comments.


        // Run string matcher.
        // Takes care of text strings, they always start with '.
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

        // Name matcher
        numConsumed = scanName();
        if (numConsumed > 0) {
            String s = sourceLine.substring(0, numConsumed).toLowerCase();
            // Token does the work of splitting this into the static tags
            setToken(new Token(s, getFileLineNum(), sourceCol));
            sourceLine = sourceLine.substring(numConsumed, sourceLine.length());
            sourceCol += numConsumed;
            return;
        }

        // Symbol matcher of length 2 and 1.
        TokenKind t;
        if(sourceLine.length() >= 2 && (t = matchSym2(sourceLine.substring(0, 2))) != null) {
            setToken(new Token(t, getFileLineNum(), sourceCol));
            sourceLine = sourceLine.substring(2, sourceLine.length());
            sourceCol += 2;
            return;
        }

        if((t = matchSym1(sourceLine.charAt(0))) != null) {
            setToken(new Token(t, getFileLineNum(), sourceCol));
            sourceLine = sourceLine.substring(1, sourceLine.length());
            sourceCol++;
            return;
        }

        Main.panic(getFileLineNum(), sourceCol, originalSourceLine, "Unexpected data found");
    }

    /**
     * Reads the next line from the file.
     * */
    private void readNextLine() {
        if (sourceFile != null) {
            try {
                originalSourceLine = sourceLine = sourceFile.readLine();
                if (sourceLine == null) {
                    sourceFile.close();
                    sourceFile = null;
                    sourceLine = "";
                }
                sourceCol = 1;
            } catch (IOException e) {
                Main.error("Scanner error: unspecified I/O error!");
            }
        }
        if (sourceFile != null)
            Main.log.noteSourceLine(getFileLineNum(), sourceLine);
    }

    /**
     * @return line number in input file. Applies to sourceLine.
     * */
    private int getFileLineNum() {
        return (sourceFile != null ? sourceFile.getLineNumber() : 0);
    }

    /**
     * @return number of chars in sourceLine matching a name.
     * */
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

    /**
    * @return number of characters from start of sourceLine matching a digit.
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

    public void test(TokenKind t) {
        if (curToken.kind != t)
            testError(t.toString());
    }


    public void testError(String message) {
        Main.error(curLineNum(), curColNum(), "Expected a " + message + " but found a " + curToken.kind + "!");
    }

    public void skip(TokenKind t) {
        test(t);
        readNextToken();
    }
}
