import java.util.LinkedList;

public class Scope {
  //scope number
  int number;

  // previouse scope
  Scope parent;

  // next scopes
  LinkedList<Scope> children;

  // symbols in the scope
  LinkedList<Symbol> symbols;

  //constructor
  public Scope(int num) {
    this.number = num;
    this.parent = null;
    this.symbols = new LinkedList<>();
    this.children = new LinkedList<>();
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public Scope getParent() {
    return parent;
  }

  public void setParent(Scope parent) {
    this.parent = parent;
  }

  public LinkedList<Symbol> getSymbols() {
    return symbols;
  }

  public Symbol getSymbol(int index) {
    return symbols.get(index);
  }

  // add the symbol in the scope
  public boolean addSymbol(Symbol symbol) {
    String id = symbol.getName();
    boolean flag = this.findIdInCurrentScope(id);
    if (!flag) {
      this.symbols.push(symbol);
      return true;
    } else {
      System.out.println("Semantic Error: Identifier '" + id + "' already declared on "+ symbol.getLine());
      return false;
    }
  }

  public LinkedList<Scope> getChildren() {
    return children;
  }

  public void addChildren(Scope scope) {
    this.children.add(scope);
  }

  public void printString() {
    for (int i = 0; i < symbols.size(); i++) {
      int index = symbols.size() - 1 - i;
      System.out.println(symbols.get(index).name + " " + symbols.get(index).type + " " +
                           this.number + " " + symbols.get(index).line);

    }
  }


  public String getTypeOfSymbol(String id) {
    for (int i = 0; i < this.symbols.size(); i++) {
      if (this.symbols.get(i).getName().equals(id)) {
        return this.symbols.get(i).getType();
      }
    }

    if (this.getParent() != null) {
      return this.getTypeOfSymbolInScope(id, this.getParent());
    }
    return "";
  }

  public String getTypeOfSymbolInScope(String id, Scope scope) {
    for (int i = 0; i < scope.symbols.size(); i++) {
      if (scope.symbols.get(i).getName().equals(id)) {
        return scope.symbols.get(i).getType();
      }
    }

    if (scope.getParent() != null) {
      return this.getTypeOfSymbolInScope(id, scope.getParent());
    }
    return "";
  }

  private boolean isNaN(String value) {
    //Todo: Implement
    return false;
  }

  /*
   * check type in current scope
   */
  public boolean checkType(String id, Node node)  {
    String type = this.getTypeOfSymbol(id);
    String value = node.type;

    if (node.isIdentifier()) {
      String idType = this.getTypeOfSymbol(node.getType());
      return type.equals(idType);
    } else if (type.equals("int")) {
      return !isNaN(value);
    } else if (type.equals("string")) {
      if (value.equals("true") || value.equals("false")) {
        return !node.isBoolean();
      }
      if (node.isInt()) {
        return false;
      }
      return true;
    } else if (type.equals("boolean")){
      return node.isBoolean();
    } else {
      return false;
    }

  }


  public boolean findId(String id) {
    for (int i = 0; i < this.symbols.size(); i++) {
      if (this.symbols.get(i).getName().equals(id)) {
        this.symbols.get(i).setInitialized(true);
        return true;
      }
    }

    if (this.getParent() != null) {
      return this.findIdInScope(id, this.getParent());
    } else {
      return false;
    }
  }

  public boolean findIdInScope(String id, Scope scope) {
    for (int i = 0; i < scope.symbols.size(); i++) {
      if (scope.symbols.get(i).getName().equals(id)) {
        scope.symbols.get(i).setInitialized(true);
        return true;
      }
    }

    if (scope.getParent() != null) {
      return this.findIdInScope(id, scope.getParent());
    } else {
      return false;
    }
  }

  /*
   * lookup id in current scope
   * @param id : String
   */
  public boolean findIdInCurrentScope(String id) {
    for (int i = 0; i < this.symbols.size(); i++) {
      if (this.symbols.get(i).getName().equals(id)) {
        return true;
      }
    }
    return false;
  }

}
