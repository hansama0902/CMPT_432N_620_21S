import java.util.LinkedList;

public class Semantic {
  // AST tree
  Tree ast;
  LinkedList<Scope> scopes;
  int scopeName;

  // Tree node
  Node astNode;

  // program counter
  int program = 1;

  // end of program
  boolean eop = false;

  // error number
  int err = 0;

  /*
   * start to semantic analysis
   */
  public void initAnalysis(Parser parser) {
    this.scopes = new LinkedList<>();
    this.ast = new Tree();
    this.scopeName = 0;

    if (program == 1) {
      System.out.println("Program 1 Semantic Analysis");
    } else if (eop){
      System.out.println("Program " + program +" Semantic Analysis");
      eop = false;
    }
    this.buildAST(parser.tree.getRoot());
    //this.ast.printString(program);
  }

  public void printString(int program) {
    System.out.println("Program\t1\tSymbol\tTable");
    System.out.println("--------------------------------------");
    System.out.println("Name\t Type\t Scope\t Line");
  }

  public void endProgram() {
    if (err == 0) {
      System.out.println("Program " + program + "Semantic Analysis produced 0 error(s) and 0 warning(s)\n\n");
    } else {
      System.out.println("\n\n");
      System.out.println("Program " + program + "Semantic Analysis produced " + err + " error(s)");
      System.out.println("Skipping CST due to Parse Error!");
    }
    eop = true;
    program++;
  }

  public void buildAST(Node root) {
    this.analysisProgram(root);
  }

  public void analysisProgram(Node root) {
    Scope scope = new Scope(this.scopeName);
    this.scopeName++;
    this.analysisBlock(root.children.get(0),scope, this.astNode);
    this.endProgram();
  }

  public void analysisBlock(Node cstNode, Scope scope, Node astNode) {
    Node node = new Node("Block");
    if (this.ast.getRoot() != null) {
      astNode.addChild(node);
      astNode = node;
      Scope newScope = new Scope(this.scopeName);
      this.scopeName++;
      newScope.setParent(scope);
      this.scopes.push(newScope);

      if (cstNode.children.size() > 2) {
        this.analyzeStatementList(cstNode.children.get(1), astNode, newScope);
      }
    } else {
      this.ast.setRoot(node);
      astNode = node;

      this.scopes.push(scope);

      if (cstNode.children.size() > 2) {
        this.analyzeStatementList(cstNode.children.get(1), astNode, scope);
      }
    }
  }

  public void analyzeStatementList(Node cstNode, Node astNode, Scope scope) {
    if (cstNode == null || cstNode.children.size() == 0) {
      return ;
    }
    if (cstNode.children.size() >= 2) {
      this.analyzeStatement(cstNode.children.get(0), astNode, scope);
      this.analyzeStatementList(cstNode.children.get(1), astNode, scope);
    } else {
      this.analyzeStatement(cstNode.children.get(0), astNode, scope);
    }

  }

  public void analyzeStatement(Node cstNode, Node astNode, Scope scope) {
    String type = cstNode.children.get(0).type;

    if (type.equals("<Print Statement>")) {
      this.analysisPrintStatement(cstNode.children.get(0), astNode, scope);
    } else if (type.equals("<Assignment Statement>")) {
      this.analysisAssignmentStatement(cstNode.children.get(0), astNode, scope);

    } else if (type.equals("<var>")) {
      this.analysisVarStatement(cstNode.children.get(0), astNode, scope);
    } else if (type.equals("<While Statement>")) {
      this.analysisWhileStatement(cstNode.children.get(0), astNode, scope);
    } else if (type.equals("<If Statement>")) {
      this.analysisIfStatement(cstNode.children.get(0), astNode, scope);
    } else if (type.equals("<Block>")) {
      this.analysisBlock(cstNode.children.get(0), scope, astNode);
    }

  }

  public void analysisVarStatement(Node cstNode, Node astNode, Scope scope) {
    Node newNode = new Node("Variable");
    Node type = new Node(cstNode.children.get(0).getType());
    Node value = new Node(cstNode.children.get(1).children.get(0).getValue());
    newNode.addChild(type);
    newNode.addChild(value);
    astNode.addChild(newNode);
    Symbol newSymbol = new Symbol(cstNode.children.get(1).children.get(0).getValue(), cstNode.children.get(0).getValue(), cstNode.children.get(0).getLineNumber());
    scope.addSymbol(newSymbol);
  }

  public void analysisWhileStatement(Node cstNode, Node astNode, Scope scope) {
    Node newNode = new Node("While Statement");
    astNode.addChild(newNode);
    astNode = newNode;

    this.analysisBooleanExpression(cstNode.children.get(1), astNode, scope);
    this.analysisBlock(cstNode.children.get(2), scope, astNode);
  }

  public void analysisIfStatement(Node cstNode, Node astNode, Scope scope) {
    Node newNode = new Node("If Statement");
    astNode.addChild(newNode);
    astNode = newNode;
    this.analysisBooleanExpression(cstNode.children.get(1), astNode, scope);
    this.analysisBlock(cstNode.children.get(2), scope, astNode);
  }

  public void analysisAssignmentStatement(Node cstNode, Node astNode, Scope scope) {
    Node newNode = new Node("Assignment Statement");
    // Add the identifier to the AST
    Node id = new Node(cstNode.children.get(0).children.get(0).getValue());
    newNode.addChild(id);
    newNode.setLineNumber(cstNode.children.get(0).children.get(0).getLineNumber());
    astNode.addChild(newNode);
    astNode = newNode;

    this.analysisExpression(cstNode.children.get(2), astNode, scope);
  }

  public void analysisPrintStatement(Node cstNode, Node astNode, Scope scope) {
    Node newNode = new Node("Print Statement");
    astNode.addChild(newNode);
    astNode = newNode;
    this.analysisExpression(cstNode.children.get(2), astNode, scope);
  }

  public void analysisExpression(Node cstNode, Node astNode, Scope scope) {
    String type = cstNode.children.get(0).type;
    if (type.equals("<Int Expression>")) {
      this.analysisIntExpression(cstNode.children.get(0), astNode, scope);
    } else if (type.equals("<String Expression>")) {
      this.analysisStringExpression(cstNode.children.get(0), astNode, scope);
    } else if (type.equals("<Boolean Expression>")) {
      this.analysisBooleanExpression(cstNode.children.get(0), astNode, scope);
    } else if (type.equals("<Identifier>")) {
      Node id = new Node(cstNode.children.get(0).children.get(0).getValue());
      id.setIdentifier(true);
      astNode.addChild(id);
    }
  }
  public void analysisIntExpression(Node cstNode, Node astNode, Scope scope) {
    if (cstNode.children.size() == 1) {
      Node value = new Node(cstNode.children.get(0).getValue());
      value.setInt(true);
      astNode.addChild(value);
    } else {
      Node value = new Node(cstNode.children.get(0).getValue());
      value.setInt(true);
      astNode.addChild(value);

      Node plus = new Node("+");
      astNode.addChild(plus);
      astNode = plus;

      this.analysisExpression(cstNode.children.get(2), astNode, scope);
    }
  }
  public void analysisStringExpression(Node cstNode, Node astNode, Scope scope) {
    if (cstNode.children.size() > 2) {
      this.analysisCharList(cstNode.children.get(1), astNode, "", scope);
    } else {
      Node newNode = new Node("");
      astNode.addChild(newNode);
    }
  }
  public void analysisBooleanExpression(Node cstNode, Node astNode, Scope scope) {
    if (cstNode.children.size() > 1) {
      Node newNode = new Node(cstNode.children.get(0).getValue());
      astNode.addChild(newNode);
      astNode = newNode;

      this.analysisExpression(cstNode.children.get(1), astNode, scope);
      this.analysisExpression(cstNode.children.get(3), astNode, scope);
    } else {
      Node newNode = new Node(cstNode.children.get(0).getValue());
      newNode.setBoolean(true);
      astNode.addChild(newNode);
    }
  }

  public void analysisCharList(Node cstNode, Node astNode,  String str, Scope scope) {
    if (cstNode.children.size() == 1) {
      str += cstNode.children.get(0).getValue();
      Node newNode = new Node(str);
      astNode.addChild(newNode);
    } else {
      str += cstNode.children.get(0).getValue();
      this.analysisCharList(cstNode.children.get(1), astNode, str, scope);
    }
  }
}
