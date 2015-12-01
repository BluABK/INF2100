package no.uio.ifi.pascal2100.main;

import no.uio.ifi.pascal2100.parser.Program;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeFile {
    private String codeFileName;
    private PrintWriter code;
    private int numLabels = 0;

    CodeFile(String fName) {
        codeFileName = fName;
        try {
            code = new PrintWriter(fName);
        } catch (FileNotFoundException e) {
            Main.error("Cannot create code file " + fName + "!");
        }
        code.println("# Code file created by Pascal2100 compiler " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    void finish() {
        code.close();
    }

    public String identify() {
        return "Code file named " + codeFileName;
    }


    public String getLabel(String origName) {
        return origName + "_" + (++numLabels);
    }
    public String getLocalLabel() {
        return String.format(".L%04d", ++numLabels);
    }


    public void genDirective(String directive) {
        code.printf("%-7s %s\n", " ", directive);
    }
    public void genDirective(String directive, String param) {
        code.printf("%-7s %-7s %s\n", " ", directive, param);
    }

    // Layout:
    // 8 spaces
    // 7+1 for instr
    // 23+1 for arg
    // comment

    public void genInstr(String instr) {
        code.printf("        %s\n", instr);
    }
    public void genInstr(String instr, String arg) {
        code.printf("        %-7s %s\n", instr, arg);
    }
    public void genInstr(String instr, String arg, String comment) {
        code.printf("        %-7s %-23s # %s\n", instr, arg, comment);
    }

    // Layout:
    // label:
    // up to 40 characters before # comment
    public void genLabel(String lab) {
        code.println(lab + ":");
    }
    public void genLabel(String lab, String comment) {
        // Calculate spaces before comment
        int spaces = 40-lab.length()-1;

        code.printf("%s:%"+spaces+"s# %s\n", lab, "", comment);
    }

    /* Get a label first */
    public void genString(String name, String s) {
        genString(name, s, "");
    }
    public void genString(String name, String s, String comment) {
        genDirective(".data");
        genLabel(name);
        code.printf(".asciz   \"");
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '"')
                code.print("\\");
            code.print(s.charAt(i));
        }
        code.print("\"");

        if (comment.length() > 0) {
            code.print("# " + comment);
        }
        code.println();
        genDirective(".align", "2");
        genDirective(".text");
    }


    public void Cifdef(String s) {
        code.println(".ifdef "+s);
    }
    public void Celse() {
        code.println(".else");
    }
    public void Cendif() {
        code.println(".endif");
    }
}
