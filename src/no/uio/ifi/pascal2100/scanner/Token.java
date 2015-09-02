package no.uio.ifi.pascal2100.scanner;

import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class Token {
    public TokenKind kind;
    public String id, strVal;
    public int intVal, lineNum, colNum;

    // Other initializer
    Token(TokenKind k, int lNum, int cNum) {
        kind = k;
        lineNum = lNum;
        colNum = cNum;
    }

    // Name initializer
    Token(String s, int lNum, int cNum) {
        switch (s) {
            case "and":
                kind = andToken;
                break;
            case "array":
                kind = arrayToken;
                break;
            case "begin":
                kind = beginToken;
                break;
            case "const":
                kind = constToken;
                break;
            case "div":
                kind = divToken;
                break;
            case "do":
                kind = doToken;
                break;
            case "else":
                kind = elseToken;
                break;
            case "end":
                kind = endToken;
                break;
            case "function":
                kind = functionToken;
                break;
            case "if":
                kind = ifToken;
                break;
            case "mod":
                kind = modToken;
                break;
            case "not":
                kind = notToken;
                break;
            case "of":
                kind = ofToken;
                break;
            case "or":
                kind = orToken;
                break;
            case "procedure":
                kind = procedureToken;
                break;
            case "program":
                kind = programToken;
                break;
            case "then":
                kind = thenToken;
                break;
            case "type":
                kind = typeToken;
                break;
            case "var":
                kind = varToken;
                break;
            case "while":
                kind = whileToken;
                break;
            default:
                kind = nameToken;
                break;
        }

        id = s;
        lineNum = lNum;
        colNum = cNum;
    }

    // String initializer
    Token(String any, String s, int lNum, int cNum) {
        kind = stringValToken;
        strVal = s;
        lineNum = lNum;
        colNum = cNum;
    }

    // Digit initializer
    Token(int n, int lNum, int cNum) {
        kind = intValToken;
        intVal = n;
        lineNum = lNum;
        colNum = cNum;
    }

    public String identify() {
        String t = kind.identify();
        if (lineNum > 0)
            t += " on line " + lineNum + " column " + colNum;

        switch (kind) {
            case nameToken:
                t += ": " + id;
                break;
            case intValToken:
                t += ": " + intVal;
                break;
            case stringValToken:
                t += ": '" + strVal + "'";
                break;
        }
        return t;
    }
}
