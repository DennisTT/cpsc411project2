package minijava.typechecker.implementation;

import minijava.ast.AST;
import minijava.ast.And;
import minijava.ast.ArrayAssign;
import minijava.ast.ArrayLength;
import minijava.ast.ArrayLookup;
import minijava.ast.Assign;
import minijava.ast.Block;
import minijava.ast.BooleanLiteral;
import minijava.ast.BooleanType;
import minijava.ast.Call;
import minijava.ast.ClassDecl;
import minijava.ast.IdentifierExp;
import minijava.ast.If;
import minijava.ast.IntArrayType;
import minijava.ast.IntegerLiteral;
import minijava.ast.IntegerType;
import minijava.ast.LessThan;
import minijava.ast.MainClass;
import minijava.ast.MethodDecl;
import minijava.ast.Minus;
import minijava.ast.NewArray;
import minijava.ast.NewObject;
import minijava.ast.NodeList;
import minijava.ast.Not;
import minijava.ast.ObjectType;
import minijava.ast.Plus;
import minijava.ast.Print;
import minijava.ast.Program;
import minijava.ast.This;
import minijava.ast.Times;
import minijava.ast.VarDecl;
import minijava.ast.While;
import minijava.typechecker.TypeChecked;
import minijava.visitor.Visitor;

public class TypeCheckVisitor implements Visitor<TypeChecked> {

	@Override
	public <T extends AST> TypeChecked visit(NodeList<T> ns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Program n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(MainClass n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(ClassDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(VarDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(MethodDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(IntArrayType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(BooleanType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(IntegerType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(ObjectType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Block n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(If n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(While n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Print n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Assign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(And n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(LessThan n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Plus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Minus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Times n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(ArrayLength n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Call n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(IntegerLiteral n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(BooleanLiteral n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(IdentifierExp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(This n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(NewArray n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(NewObject n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeChecked visit(Not not) {
		// TODO Auto-generated method stub
		return null;
	}

}
