package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;

public class Library extends Block {
    public Library() {
        super(-1, 0);

        outerScope = null;
        types = new TypeDeclPart(-1, 0);
        types.context = this;
        constants = new ConstDeclPart(-1, 0);
        constants.context = this;
        variables = null;
        functions = null;
        statements = null;

        /* Define and link char */
        TypeDecl ch = new TypeDecl("char", -1, 0);
        ch.context = types;
        NameType nt = new NameType(-1, 0);
        ch.type = nt;
        nt.name = "char";
        ch.type.context = ch;
        types.types.add(ch);

        /* Define and link integer */
        TypeDecl integer = new TypeDecl("integer", -1, 0);
        integer.context = types;
        nt = new NameType(-1, 0);
        nt.name = "integer";
        integer.type = nt;
        integer.type.context = integer;
        types.types.add(integer);

        /* Define and link boolean */
        TypeDecl bool = new TypeDecl("boolean", -1, 0);
        bool.context = types;
        EnumType et = new EnumType(-1, 0);
        bool.type = et;
        bool.type.context = bool;
        et.literals.add(new Enum("true", -1, 0));
        et.literals.get(0).context = et;
        et.literals.add(new Enum("false", -1, 0));
        et.literals.get(1).context = et;
        types.types.add(bool);

        /* Define and link eol */
        ConstDecl cd = new ConstDecl("eol", -1, 0);
        cd.context = constants;
        ConstantStr n = new ConstantStr(-1, 0);
        cd.child = n;
        cd.child.context = cd;
        n.str = "\n";
        constants.constants.add(cd);

        /* Define and link write */
        ProcDecl write = new ProcDecl("write", -1, 0);
        write.context = this;
        procedures.add(write);


        // Then add all
        for(ConstDecl c: constants.constants)
            addDecl(c.name, c);
        for(TypeDecl t: types.types)
            addDecl(t.name, t);
        for(ProcDecl p: procedures)
            addDecl(p.name, p);
    }

    /**
     * Create linking directives that call the given block as the main function
     * @param code
     * @param start
     */
    @Override
    public void genCode(CodeFile code, Block start){
        code.genDirective(".extern", "write_char");
        code.genDirective(".extern", "write_int");
        code.genDirective(".extern", "write_string");
        code.genDirective(".globl", "main");
        code.genDirective(".globl", "_main");
        code.genLabel("_main");
        code.genLabel("main");
        code.genInstr("call", start.mangledName);
        code.genInstr("movl", "$0,%eax");
        code.genInstr("ret");
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
}
