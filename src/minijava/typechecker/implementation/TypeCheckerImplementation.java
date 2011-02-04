package minijava.typechecker.implementation;

import minijava.ast.AST;
import minijava.typechecker.TypeChecked;
import minijava.util.FunTable;

public class TypeCheckerImplementation {

	private AST program;
	private FunTable classTable;
	
	public TypeCheckerImplementation(AST program)
	{
		this.program = program;
	}
	
	public TypeChecked typeCheck()
	{
		return program.accept(new TypeCheckVisitor());
	}

	public Object buildClassTable()
	{
		return classTable;
	}
}
