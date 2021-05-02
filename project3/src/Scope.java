import java.util.LinkedList;

public class Scope {
  int number;
  Scope parent;

  LinkedList<Scope> children;
  LinkedList<Symbol> symbols;

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

  public void addSymbol(Symbol symbol) {
    String id = symbol.getName();
    boolean flag = this.findIdInCurrentScope(id);
    if (!flag) {
      this.symbols.push(symbol);
    } else {
      System.out.println("Identifier '" + id + "' already declared in scope."+ symbol.getLine() + "Semantic Analyzer");
      throw new Error("ID already in scope");
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
      if(symbols.get(i).isInitialized) {
        System.out.println(symbols.get(i).name + " " + symbols.get(i).type + " " +
                           this.number + " " + symbols.get(i).line);
      } else {
        System.out.println(symbols.get(i).name + " was not initialized at " + symbols.get(i).line);
      }
    }
  }

  public boolean findId(String id) {
    for (int i = 0; i < this.symbols.size(); i++) {
      System.out.println("symbol name" + this.symbols.get(i).getName() + " " + id);
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
      System.out.println("symbol name" + scope.symbols.get(i).getName() + " " + id);
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
