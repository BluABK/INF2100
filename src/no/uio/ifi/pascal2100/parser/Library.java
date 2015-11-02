package no.uio.ifi.pascal2100.parser;

import java.util.HashMap;

public class Library extends Block {
    public Library() {
        super(0, 0);

        outerScope = null;
        types = new TypeDeclPart(0, 0);
        types.context = this;
        constants = new ConstDeclPart(0, 0);
        constants.context = this;
        variables = null;
        functions = null;
        statements = null;

        /* Define and link char */
        TypeDecl ch = new TypeDecl("char", 0, 0);
        ch.context = types;
        NameType nt = new NameType(0, 0);
        ch.child = nt;
        nt.name = "char";
        ch.child.context = ch;
        types.types.add(ch);

        /* Define and link integer */
        TypeDecl integer = new TypeDecl("integer", 0, 0);
        integer.context = types;
        nt = new NameType(0, 0);
        nt.name = "integer";
        integer.child = nt;
        integer.child.context = integer;
        types.types.add(integer);

        /* Define and link boolean */
        TypeDecl bool = new TypeDecl("boolean", 0, 0);
        bool.context = types;
        EnumType et = new EnumType(0, 0);
        bool.child = et;
        bool.child.context = bool;
        et.literals.add("true");
        et.literals.add("false");
        types.types.add(bool);

        /* Define and link eol */
        ConstDecl cd = new ConstDecl("eol", 0, 0);
        cd.context = constants;
        ConstantStr n = new ConstantStr(0, 0);
        cd.child = n;
        cd.child.context = cd;
        n.str = "\n";
        constants.constants.add(cd);

        /* Define and link write */
        ProcDecl write = new ProcDecl("write", 0, 0);
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
}
