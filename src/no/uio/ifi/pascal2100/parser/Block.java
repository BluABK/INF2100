package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * [ {@link ConstDeclPart} ]
 * [ {@link TypeDeclPart} ]
 * [ {@link VarDeclPart} ]
 * [ {@link FuncDecl} | {@link ProcDecl} ]*
 * 'begin' {@link StatmList} 'end'
 */
public class Block extends PascalSyntax {
    public ConstDeclPart constants;
    public TypeDeclPart types;
    public VarDeclPart variables;
    public ArrayList<FuncDecl> functions;
    public ArrayList<ProcDecl> procedures;

    public StatmList statements;

    public boolean hasRet = true;
    public Block outerScope;
    public PascalDecl parent;
    // @BluABK Poke: This will be in.. Robot because that's where the variables are stored. Hashmap maps from a variable string to the actual reference. k. Parts of this code can basically be copied.. well it's OUR code so.. give credits to yourself properly, allright? wel lnot even a joke might need to give credit to the task ettersom det var 2 stk på den, devilry plaguarsim systemet er alltid en koseklump..... Bare legg inn kommentar dersom du kopierer en full funksjon elns
    HashMap<String, PascalDecl> decls = new HashMap<>();

    Block(int n, int c) {
        super(n, c);
        functions = new ArrayList<>();
        procedures = new ArrayList<>();
    }

    // Limit creaters to PacalDecl
    public static Block parse(Scanner s, PascalDecl context) {
        enterParser("Block");

        Block b = new Block(s.curLineNum(), s.curColNum());
        b.context = context;
        b.parent = context;

        if (s.curToken.kind == TokenKind.constToken) {
            b.constants = ConstDeclPart.parse(s, b);
        }

        if (s.curToken.kind == TokenKind.typeToken) {
            b.types = TypeDeclPart.parse(s, b);
        }

        if (s.curToken.kind == TokenKind.varToken) {
            b.variables = VarDeclPart.parse(s, b);
        }

        while (s.curToken.kind == TokenKind.functionToken || s.curToken.kind == TokenKind.procedureToken) {
            if (s.curToken.kind == TokenKind.functionToken)
                b.functions.add(FuncDecl.parse(s, b));
            else
                b.procedures.add(ProcDecl.parse(s, b));
        }

        s.skip(TokenKind.beginToken);
        b.statements = StatmList.parse(s, b);
        s.skip(TokenKind.endToken);

        leaveParser("Block");
        return b;
    }

    void addDecl(String id, PascalDecl d) {
        id = id.toLowerCase();
        if (decls.containsKey(id)) {
            d.error(id + " was declared twice in the same block!");
        } else {
            decls.put(id, d);
        }
    }

    PascalDecl findDecl(String id, PascalSyntax w) {
        id = id.toLowerCase();
        PascalDecl d = decls.get(id);
        if (d != null) {
            Main.log.noteBinding(id, w, d);
            return d;
        }

        if (outerScope != null)
            return outerScope.findDecl(id, w);

        w.error("Name " + id + " is undefined");
        return null;
    }

    // poke2? BluABKPoke daddsada :P BluABK.poke()
    // so check is where it does the same stuff you'll do now:
    @Override
    public void check(Block curScope, Library lib) {
	// We only have one scope, but that would be something like:
	// curRobot = robot
        outerScope = curScope;

	// First it adds all the variables into as hashmap so we can look them up later
        if (constants != null)
            for (ConstDecl cd : constants.constants)
                addDecl(cd.name, cd);
        if (types != null)
            for (TypeDecl td : types.types) {
                addDecl(td.name, td);

                // Enum problem: Link to the Enum definition inside the EnumType.
                // Fix this in part4 if necessary
                if (td.type instanceof EnumType) {
                    EnumType e = (EnumType) td.type;
                    for (Enum l : e.literals) {
                        addDecl(l.name, l);
                    }
                }
            }
        if (variables != null)
            for (VarDecl vd : variables.vars)
                addDecl(vd.name, vd);
        for (FuncDecl fd : functions)
            addDecl(fd.name, fd);
        for (ProcDecl pd : procedures)
            addDecl(pd.name, pd);

        if (constants != null)
            constants.check(this, lib);
        if (variables != null)
            variables.check(this, lib);
        if (types != null)
            types.check(this, lib);
        for (ProcDecl pd : procedures)
            pd.check(this, lib);
        for (FuncDecl fd : functions)
            fd.check(this, lib);

	// then it checks the code. since it passed itself down, that code can call back here to retrieve a reference
        statements.check(this, lib);
    }

    @Override
    public void genCode(CodeFile code) {
        // level is to be set by the time we reach here
        // mangledName is to be set as well

        // Figure out stack sizes, this does not generate actual code:
        // As well as the total stack size: variables.totalStackSize

        int stackSize;
        if (variables != null) {
            variables.parentDeclLevel = parent.declLevel;
            variables.genCode(code);
            stackSize = variables.totalStackSize;
        } else {
            stackSize = 32;
        }

        for (FuncDecl f : functions) {
            f.declLevel = parent.declLevel + 1;
            f.genCode(code);
        }
        for (ProcDecl p : procedures) {
            p.declLevel = parent.declLevel + 1;
            p.genCode(code);
        }

        // statements
        code.genLabel(parent.progProcFuncName, parent.name + " (level " + parent.declLevel + ")");

        code.genInstr("enter", "$" + stackSize + ",$" + parent.declLevel);
        statements.genCode(code);
        if (hasRet)
            code.genInstr("movl", "-32(%ebp),%eax");
        code.genInstr("leave");
        code.genInstr("ret");
    }

    @Override
    public String identify() {
        return identifyTemplate();
    }

    @Override
    public void prettyPrint() {
        if (constants != null)
            constants.prettyPrint();
        if (types != null)
            types.prettyPrint();
        if (variables != null)
            variables.prettyPrint();

        for (FuncDecl f : functions) {
            f.prettyPrint();
        }
        for (ProcDecl p : procedures) {
            p.prettyPrint();
        }
        Main.log.prettyPrintLn("begin");
        Main.log.prettyIndent();
        statements.prettyPrint();
        Main.log.prettyOutdent();
        Main.log.prettyPrint("end");
    }
}
