package minijava.typechecker.implementation;

import minijava.util.FunTable;
import minijava.util.Indentable;
import minijava.util.IndentingWriter;

public class ClassInfo implements Indentable {
	public String superClass;
	public FunTable<VarInfo> fields;
	public FunTable<MethodInfo> methods;
	
	@Override
	public void dump(IndentingWriter out) {
		fields.dump(out);
		methods.dump(out);
	}
}
