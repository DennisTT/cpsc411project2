package minijava.typechecker.implementation;

import minijava.ast.VarDecl;
import minijava.ast.Type;
import minijava.util.Indentable;
import minijava.util.IndentingWriter;

public class VarInfo implements Indentable {
	public VarDecl.Kind kind;
	public Type type;
	
	@Override
	public void dump(IndentingWriter out) {
		// TODO Auto-generated method stub
		
	}
}
