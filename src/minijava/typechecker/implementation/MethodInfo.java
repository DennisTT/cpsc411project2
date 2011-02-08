package minijava.typechecker.implementation;

import java.util.ArrayList;

import minijava.ast.Type;
import minijava.util.FunTable;
import minijava.util.Indentable;
import minijava.util.IndentingWriter;

public class MethodInfo implements Info, Indentable {
	public Type returnType;
	public ArrayList<VarInfo> formalsList;
	public FunTable<Info> formals;
	public FunTable<Info> locals;
	
	@Override
	public void dump(IndentingWriter out) {
		out.println("returnType " + returnType);
		
		out.print("formalsList ");
		while(formalsList.iterator().hasNext())
		{
			formalsList.iterator().next().dump(out);
		}
		out.println();
		
		out.print("formals");
		formals.dump(out);
		out.println();
		
		out.print("locals");
		locals.dump(out);
	}
}
