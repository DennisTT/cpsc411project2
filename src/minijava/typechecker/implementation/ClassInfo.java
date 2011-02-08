package minijava.typechecker.implementation;

import minijava.util.FunTable;
import minijava.util.Indentable;
import minijava.util.IndentingWriter;

public class ClassInfo implements Info, Indentable {
	public String superClass;
	public FunTable<Info> fields;
	public FunTable<Info> methods;
	
	@Override
	public void dump(IndentingWriter out) {
		fields.dump(out);
		methods.dump(out);
	}
}
