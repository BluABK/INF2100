package no.uio.ifi.pascal2100.parser;

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

    HashMap<String, PascalDecl> decls = new HashMap<>();
    public Block outerScope;


    Block(int n, int c) {
        super(n, c);
        functions = new ArrayList<>();
        procedures = new ArrayList<>();
    }

    void addDecl(String id, PascalDecl d) {
        id = id.toLowerCase();
        if(decls.containsKey(id)) {
            d.error(id + " was declared twice in the same block!");
        } else {
            decls.put(id, d);
        }
    }

    PascalDecl findDecl(String id, PascalSyntax w) {
        id = id.toLowerCase();
        PascalDecl d = decls.get(id);
        if(d != null) {
            Main.log.noteBinding(id, w, d);
            return d;
        }

        if(outerScope != null)
            return outerScope.findDecl(id,w);

        w.error("Name " + id + " is undefined");
        return null;
    }

    @Override
    public void check(Block curScope, Library lib) {
        outerScope = curScope;

        if(constants != null)
            for(ConstDecl cd: constants.constants)
                addDecl(cd.name, cd);
        if(types != null)
            for(TypeDecl td: types.types) {
                addDecl(td.name, td);

                // Enum problem: Link to the Enum definition inside the EnumType.
                // Fix this in part4 if necessary
                if(td.type instanceof EnumType) {
                    EnumType e = (EnumType)td.type;
                    for(Enum l: e.literals) {
                        addDecl(l.name, l);
                    }
                }
            }
        if(variables != null)
            for(VarDecl vd: variables.vars)
                addDecl(vd.name, vd);
        for(FuncDecl fd: functions)
            addDecl(fd.name, fd);
        for(ProcDecl pd: procedures)
            addDecl(pd.name, pd);

        if(constants != null)
            constants.check(this, lib);
        if(variables != null)
            variables.check(this, lib);
        if(types != null)
            types.check(this, lib);
        for(ProcDecl pd: procedures)
            pd.check(this, lib);
        for(FuncDecl fd: functions)
            fd.check(this, lib);

        statements.check(this, lib);
    }

    public static Block parse(Scanner s, PascalSyntax context) {
        enterParser("Block");

        Block b = new Block(s.curLineNum(), s.curColNum());
        b.context = context;

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
