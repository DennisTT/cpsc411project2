package minijava.typechecker.implementation;

import minijava.ast.*;
import minijava.typechecker.ErrorReport;
import minijava.typechecker.TypeChecked;
import minijava.util.FunTable;
import minijava.visitor.Visitor;

public class TypeCheckVisitor implements Visitor<TypeChecked>
{
  private FunTable<Info> table;
  private ErrorReport error;
  
  private String currentClass,
                 currentMethod;
  
  public TypeCheckVisitor(FunTable<Info> table, ErrorReport error)
  {
    this.table = table;
    this.error = error;
  }
  
  @Override
  public <T extends AST> TypeChecked visit(NodeList<T> ns)
  {
    for(int i = 0; i < ns.size(); ++i)
    {
      ns.elementAt(i).accept(this);
    }
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = null;
    return t;
  }

  @Override
  public TypeChecked visit(Program p)
  {
    p.mainClass.accept(this);
    p.classes.accept(this);
    
    // Reset context
    this.currentClass = null;
    this.currentMethod = null;
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = null;
    return t;
  }

  @Override
  public TypeChecked visit(MainClass c)
  {
    // Set main context
    this.currentClass = c.className;
    this.currentMethod = "main";
    
    return c.statement.accept(this);
  }

  @Override
  public TypeChecked visit(ClassDecl d)
  {
    if(d.superName != null && this.lookupClassInfo(d.superName) == null)
    {
      this.error.undefinedId(d.superName);
    }
    
    if(this.lookupClassInfo(d.name) == null)
    {
      this.error.undefinedId(d.name);
    }
    
    // Set class context
    this.currentClass = d.name;
    
    d.methods.accept(this);
    d.vars.accept(this);
    
    // Reset class context
    this.currentClass = null;
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = null;
    return t;
  }

  @Override
  public TypeChecked visit(VarDecl n)
  {
    Type type = n.type;
    
    // Check identifier and declared object type (if necessary)
    ClassInfo c = this.lookupClassInfo(this.currentClass);
    MethodInfo m = (MethodInfo) c.methods.lookup(this.currentMethod);
    boolean b1 = ((m != null && ( m.locals.lookup(n.name) != null ||
                                  m.formals.lookup(n.name) != null)) ||
                  c.fields.lookup(n.name) != null),
            b2 = (!(type instanceof ObjectType) ||
                  this.lookupClassInfo(((ObjectType) type).name) != null);
    if(!b1 || !b2)
    {
      if(!b1)
      {
        this.error.undefinedId(n.name);
      }
      
      if(!b2)
      {
        this.error.undefinedId(((ObjectType) type).name);
      }
      
      return null;
    }
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = type;
    return t;
  }

  @Override
  public TypeChecked visit(MethodDecl n)
  {
    Type type = n.returnType;
    
    // Check method identifier and object return type (if necessary)
    ClassInfo c = (ClassInfo) this.lookupClassInfo(this.currentClass);
    MethodInfo m = (MethodInfo) c.methods.lookup(n.name);
    boolean b1 = (m != null),
            b2 = (!(type instanceof ObjectType) ||
                  this.lookupClassInfo(((ObjectType) type).name) != null);
    
    if(!b1 || !b2)
    {
      if(!b1)
      {
        this.error.undefinedId(n.name);
      }
      
      if(!b2)
      {
        this.error.undefinedId(((ObjectType) type).name);
      }
      
      return null;
    }
    
    this.currentMethod = n.name;
    
    n.formals.accept(this);
    n.vars.accept(this);
    n.statements.accept(this);
    
    TypeCheckedImplementation t = (TypeCheckedImplementation) n.returnExp.accept(this);
    
    this.currentMethod = null;
    
    // Check return type
    if(t != null && !t.type.equals(m.returnType))
    {
      this.error.typeError(n.returnExp, m.returnType, t.type);
      return null;
    }
    
    return t;
  }

  @Override
  public TypeChecked visit(IntArrayType n)
  {
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = n;
    return t;
  }

  @Override
  public TypeChecked visit(BooleanType n)
  {
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = n;
    return t;
  }

  @Override
  public TypeChecked visit(IntegerType n)
  {
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = n;
    return t;
  }

  @Override
  public TypeChecked visit(ObjectType n)
  {
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = n;
    return t;
  }

  @Override
  public TypeChecked visit(Block b)
  {
    return b.statements.accept(this);
  }

  @Override
  public TypeChecked visit(If n)
  {
    TypeCheckedImplementation t = (TypeCheckedImplementation) n.tst.accept(this);
    if(t != null && !t.type.equals(new BooleanType()))
    {
      this.error.typeError(n.tst, new BooleanType(), t.type);
      return null;
    }
    
    n.thn.accept(this);
    n.els.accept(this);
    
    return t;
  }

  @Override
  public TypeChecked visit(While n)
  {
    TypeCheckedImplementation t = (TypeCheckedImplementation) n.tst.accept(this);
    if(t != null && !t.type.equals(new BooleanType()))
    {
      this.error.typeError(n.tst, new BooleanType(), t.type);
      return null;
    }
    
    n.body.accept(this);
    
    return t;
  }

  @Override
  public TypeChecked visit(Print n)
  {
    TypeCheckedImplementation t = (TypeCheckedImplementation) n.exp.accept(this);
    if(t != null && !t.type.equals(new IntegerType()))
    {
      this.error.typeError(n.exp, new IntegerType(), t.type);
        return null;
    }
    
    return t;
  }

  @Override
  public TypeChecked visit(Assign n)
  {
    TypeCheckedImplementation t = (TypeCheckedImplementation) n.value.accept(this);
    if(t == null)
    {
      return null;
    }
    
    VarInfo v = this.lookupVarInfo(n.name);
    if(v == null)
    {
      return null;
    }
    
    if(!t.type.equals(v.type))
    {
      this.error.typeError(n.value, v.type, t.type);
      return null;
    }
    
    return t;
  }

  @Override
  public TypeChecked visit(ArrayAssign n)
  {
    VarInfo v = this.lookupVarInfo(n.name);
    if(v == null)
    {
      return null;
    }
    
    if(!v.type.equals(new IntArrayType()))
    {
      this.error.typeError(new IdentifierExp(n.name), new IntArrayType(), v.type);
      return null;
    }
    
    TypeCheckedImplementation t1 = (TypeCheckedImplementation) n.value.accept(this),
                              t2 = (TypeCheckedImplementation) n.index.accept(this);
    boolean b1 = t1.type.equals(new IntegerType()),
            b2 = t2.type.equals(new IntegerType());
    
    if(!b1 || !b2)
    {
      if(!b1)
      {
        this.error.typeError(n.value, new IntegerType(), t1.type);
      }
      
      if(!b2)
      {
        this.error.typeError(n.index, new IntegerType(), t2.type);
      }
      
      return null;
    }
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new IntegerType();
    return t;
  }

  @Override
  public TypeChecked visit(And n)
  {
    TypeCheckedImplementation t1 = (TypeCheckedImplementation) n.e1.accept(this),
                              t2 = (TypeCheckedImplementation) n.e2.accept(this);
    boolean b1 = t1.type.equals(new BooleanType()),
            b2 = t2.type.equals(new BooleanType());
    
    if(!b1 || !b2)
    {
      if(!b1)
      {
        this.error.typeError(n.e1, new BooleanType(), t1.type);
      }
      
      if(!b2)
      {
        this.error.typeError(n.e2, new BooleanType(), t2.type);
      }
      
      return null;
    }
    
    return t1;
  }

  @Override
  public TypeChecked visit(LessThan n)
  {
    TypeCheckedImplementation t1 = (TypeCheckedImplementation) n.e1.accept(this),
                              t2 = (TypeCheckedImplementation) n.e2.accept(this);
    boolean b1 = t1.type.equals(new IntegerType()),
            b2 = t2.type.equals(new IntegerType());
    
    if(!b1 || !b2)
    {
      if(!b1)
      {
        this.error.typeError(n.e1, new IntegerType(), t1.type);
      }
      
      if(!b2)
      {
        this.error.typeError(n.e2, new IntegerType(), t2.type);
      }
      
      return null;
    }
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new BooleanType();
    return t;
  }

  @Override
  public TypeChecked visit(Plus n)
  {
    TypeCheckedImplementation t1 = (TypeCheckedImplementation) n.e1.accept(this),
                              t2 = (TypeCheckedImplementation) n.e2.accept(this);
    boolean b1 = t1.type.equals(new IntegerType()),
            b2 = t2.type.equals(new IntegerType());
    
    if(!b1 || !b2)
    {
      if(!b1)
      {
        this.error.typeError(n.e1, new IntegerType(), t1.type);
      }
      
      if(!b2)
      {
        this.error.typeError(n.e2, new IntegerType(), t2.type);
      }
      
      return null;
    }
    
    return t1;
  }

  @Override
  public TypeChecked visit(Minus n)
  {
    TypeCheckedImplementation t1 = (TypeCheckedImplementation) n.e1.accept(this),
                              t2 = (TypeCheckedImplementation) n.e2.accept(this);
    boolean b1 = t1.type.equals(new IntegerType()),
            b2 = t2.type.equals(new IntegerType());
    
    if(!b1 || !b2)
    {
      if(!b1)
      {
        this.error.typeError(n.e1, new IntegerType(), t1.type);
      }
      
      if(!b2)
      {
        this.error.typeError(n.e2, new IntegerType(), t2.type);
      }
      
      return null;
    }
    
    return t1;
  }

  @Override
  public TypeChecked visit(Times n)
  {
    TypeCheckedImplementation t1 = (TypeCheckedImplementation) n.e1.accept(this),
                              t2 = (TypeCheckedImplementation) n.e2.accept(this);
    boolean b1 = t1.type.equals(new IntegerType()),
            b2 = t2.type.equals(new IntegerType());
    
    if(!b1 || !b2)
    {
      if(!b1)
      {
        this.error.typeError(n.e1, new IntegerType(), t1.type);
      }
      
      if(!b2)
      {
        this.error.typeError(n.e2, new IntegerType(), t2.type);
      }
      
      return null;
    }
    
    return t1;
  }

  @Override
  public TypeChecked visit(ArrayLookup n)
  {
    TypeCheckedImplementation t1 = (TypeCheckedImplementation) n.array.accept(this),
                              t2 = (TypeCheckedImplementation) n.index.accept(this);
    
    if(!t1.type.equals(new IntArrayType()))
    {
      this.error.typeError(n.array, new IntArrayType(), t1.type);
      return null;
    }
    
    if(!t2.type.equals(new IntegerType()))
    {
      this.error.typeError(n.index, new IntegerType(), t2.type);
      return null;
    }
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new IntegerType();
    return t;
  }

  @Override
  public TypeChecked visit(ArrayLength n)
  {
    TypeCheckedImplementation t1 = (TypeCheckedImplementation) n.array.accept(this);
    if(!t1.type.equals(new IntArrayType()))
    {
      this.error.typeError(n.array, new IntArrayType(), t1.type);
      return null;
    }
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new IntegerType();
    return t;
  }

  @Override
  public TypeChecked visit(Call n) {
    TypeCheckedImplementation t = (TypeCheckedImplementation) n.receiver.accept(this);
    
    if(t == null)
    {
      return null;
    }
    
    if(!(t.type instanceof ObjectType))
    {
      this.error.typeErrorExpectObjectType(n.receiver, t.type);
      return null;
    }
    
    ObjectType objectType = (ObjectType) t.type;
    ClassInfo c = this.lookupClassInfo(objectType.name);
    if(c == null)
    {
      this.error.undefinedId(objectType.name);
      return null;
    }
    
    MethodInfo m = (MethodInfo) c.methods.lookup(n.name);
    if(m == null)
    {
      this.error.undefinedId(n.name);
      return null;
    }
    
    if(m.formalsList.size() != n.rands.size())
    {
      this.error.wrongNumberOfArguments(n, m.formalsList.size());
      return null;
    }
    
    for(int i = 0; i < m.formalsList.size(); i++)
    {
      TypeCheckedImplementation randType = (TypeCheckedImplementation) n.rands.elementAt(i).accept(this);
      if(randType != null && !m.formalsList.get(i).type.equals(randType.type))
      {
        this.error.typeError(n.rands.elementAt(i), m.formalsList.get(i).type, randType.type);
      }
    }
    
    t.type = m.returnType;
    return t;
  }

  @Override
  public TypeChecked visit(IntegerLiteral n)
  {
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new IntegerType();
    return t;
  }

  @Override
  public TypeChecked visit(BooleanLiteral n)
  {
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new BooleanType();
    return t;
  }

  @Override
  public TypeChecked visit(IdentifierExp n)
  {
    VarInfo v = this.lookupVarInfo(n.name);
    if(v == null)
    {
      return null;
    }
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = v.type;
    return t;
  }

  @Override
  public TypeChecked visit(This n) {
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new ObjectType(this.currentClass);
    return t;
  }

  @Override
  public TypeChecked visit(NewArray n)
  {
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new IntArrayType();
    return t;
  }

  @Override
  public TypeChecked visit(NewObject n)
  {
    ClassInfo c = this.lookupClassInfo(n.typeName);
    if(c == null)
    {
      this.error.undefinedId(n.typeName);
      return null;
    }
    
    TypeCheckedImplementation t = new TypeCheckedImplementation();
    t.type = new ObjectType(n.typeName);
    return t;
  }

  @Override
  public TypeChecked visit(Not n)
  {
    TypeCheckedImplementation t = (TypeCheckedImplementation) n.e.accept(this);
    if(t != null && !t.type.equals(new BooleanType()))
    {
      this.error.typeError(n.e, new BooleanType(), t.type);
    }
    
    return t;
  }
  
  private ClassInfo lookupClassInfo(String id)
  {
    return (ClassInfo) this.table.lookup(id);
  }

  private VarInfo lookupVarInfo(String id)
  {
    ClassInfo c = this.lookupClassInfo(this.currentClass);
    MethodInfo m = (MethodInfo) c.methods.lookup(this.currentMethod);
    Info v = null;
    
    if(m != null)
    {
      v = m.locals.lookup(id);
      if(v == null)
      {
        v = m.formals.lookup(id);
      }
    }
    
    if(v == null)
    {
      v = c.fields.lookup(id);
      if(v == null)
      {
        this.error.undefinedId(id);
        return null;
      }
    }
    
    return (VarInfo) v;
  }
}
