import java.util.ArrayList;

public class Parser {
  Tree tree;
  ArrayList<Token> ntokens;
  int tokenIndex = 0;
  int line = 1;
  Token currentToken;
  boolean eop = false;
  int program = 1;
  int err = 0;
  public void LOG(String method) {
    System.out.println("PARSER: " + method);
  }

  public void init(ArrayList<Token> tokens) {
    ntokens = tokens;
    tree = new Tree();
    currentToken = ntokens.get(tokenIndex);

    if (program == 1) {
      System.out.println("PARSER: Parsing program 1...");
    } else if (eop){
      System.out.println("PARSER: Parsing program " + program +"...");
      eop = false;
    }
    this.parse();
  }

  public void parse() {
    LOG("parser()");
    this.parseProgram();
  }

  public void endProgram() {
    if (err == 0) {
      LOG("Parse completed successfully\n\n");
      tree.printString(program);
    } else {
      System.out.println("\n\n");
      System.out.println(err + " Parser Errors detected in program " + program);
      System.out.println("Skipping CST due to Parse Error!");
    }
    eop = true;
    program++;
  }

  public void parseProgram() {
    LOG("parserProgram()");
    Node node = tree.addBranchNode("<Program>");
    this.parseBlock();
    this.match(node,"$");
    tree.endChildren();
    this.endProgram();
  }


  public void parseBlock() {
    LOG("parseBlock()");
    Node node = tree.addBranchNode("<Block>");
    this.match(node, "{");
    this.parseStatementList();
    this.match(node, "}");
    tree.endChildren();
  }

  public void parseStatementList() {
    LOG("parseStatementList()");
    tree.addBranchNode("<Statement List>");
    if (currentToken.type.equals( "print" ) ||
            currentToken.type.equals( "ID" )||
            currentToken.type.equals( "int" )||
            currentToken.type.equals( "boolean" ) ||
            currentToken.type.equals( "string" ) ||
            currentToken.type.equals( "{" ) ||
            currentToken.type.equals( "while" ) ||
            currentToken.type.equals( "if" )
    ) {

      this.parseStatement();
      this.parseStatementList();
    }
    tree.endChildren();
  }

  public void parseStatement() {
    LOG("parseStatement()");
    if (currentToken.type.equals("print")) {
      this.parsePrintStatement();
    } else if (currentToken.type.equals("string") ||
               currentToken.type.equals("int") ||
               currentToken.type.equals("boolean")) {
      this.parseVarDecl();
    } else if (currentToken.type.equals("while")) {
      this.parseWhileStatement();
    } else if (currentToken.type.equals("if")) {
      this.parseIfStatement();
    } else if (currentToken.type.equals("ID")) {
      this.parseAssignmentStatement();
    } else {
      this.parseBlock();
    }

  }

  public void parsePrintStatement() {
    LOG("parsePrintStatement()");
    Node node = tree.addBranchNode("<Print Statement>");
    this.match(node, "print");
    this.match(node, "(");
    this.parseExpr();
    this.match(node,")");
    tree.endChildren();
  }

  public void parseAssignmentStatement() {
    LOG("parseAssignmentStatement()");
    Node node = tree.addBranchNode("<Assignment Statement>");
    this.parseId();
    this.match(node,"=");
    this.parseExpr();
    tree.endChildren();
  }

  public void parseVarDecl() {
    LOG("parseVarDecl()");
    Node node = tree.addBranchNode("<var>");
    if (currentToken.type.equals("int")) {
      this.match(node,"int");
      this.parseId();
    } else if (currentToken.type.equals("string")) {
      this.match(node,"string");
      this.parseId();
    } else if (currentToken.type.equals("boolean")) {
      this.match(node,"boolean");
      this.parseId();
    }
    tree.endChildren();
  }

  public void parseWhileStatement() {
    LOG("parseWhileStatement()");
    Node node = tree.addBranchNode("<While Statement>");
    this.match(node,"while");
    this.parseBooleanExpr();
    this.parseBlock();
    tree.endChildren();
  }

  public void parseIfStatement() {
    LOG("parseIfStatement()");
    Node node = tree.addBranchNode("<If Statement>");
    this.match(node,"if");
    this.parseBooleanExpr();
    this.parseBlock();
    tree.endChildren();
  }

  public void parseExpr() {
    LOG("parseExpr()");
    tree.addBranchNode("<Expression>");
    if (currentToken.type == "digit") {
      this.parseIntExpr();
    } else if (currentToken.type.equals("\"")) {
      this.parseStringExpr();
    } else if (currentToken.type.equals("(") ||
               currentToken.type.equals("true") ||
               currentToken.type.equals("false")) {
      this.parseBooleanExpr();
    } else if (currentToken.type.equals("ID")) {
      this.parseId();
    }
  }

  public void parseIntExpr() {
    LOG("parseIntExpr()");
    Node node = tree.addBranchNode("<Int Expression>");
    if (currentToken.type.equals("digit")) {
      this.match(node, "digit");
      if (currentToken.type.equals("+")) {
        this.match(node, "+");
        this.parseExpr();
      }
    }
    tree.endChildren();
  }

  public void parseStringExpr() {
    LOG("parseStringExpr()");
    Node node = tree.addBranchNode("<String Expression>");
    this.match(node,"\"");
    this.parseCharList();
    this.match(node,"\"");
    tree.endChildren();
  }

  public void parseBooleanExpr() {
    LOG("parseBooleanExpr()");
    Node node = tree.addBranchNode("<Boolean Expression>");
    if (currentToken.type.equals("true")) {
      this.match(node,"true");
    } else if (currentToken.type.equals("false")) {
      this.match(node,"false");
    } else {
      this.match(node,"(");
      this.parseExpr();

      if (currentToken.type.equals("==")) {
        this.match(node,"==");
        this.parseExpr();
        this.match(node,")");
      } else if (currentToken.type.equals("!=")) {
        this.match(node,"!=");
        this.parseExpr();
        this.match(node, ")");
      }
    }
    tree.endChildren();
  }

  public void parseId() {
    LOG("parseId()");
    Node node = tree.addBranchNode("<Identifier>");
    this.match(node,"ID");
    tree.endChildren();
  }

  public void parseCharList() {
    LOG("parseCharList()");
    if (currentToken.type.equals("char")) {
      Node node = tree.addBranchNode("<Char List>");
      this.match(node,"char");
      this.parseCharList();
      tree.endChildren();
    } else if (currentToken.type.equals("space")) {
      Node node = tree.addBranchNode("<Char List>");
      this.match(node,"space");
      this.parseCharList();
      tree.endChildren();
    }
  }

  public void match(Node node, String type) {
    if(currentToken.type.equals(type)) {
      tree.addLeafNode(node, currentToken);
    } else {
      if (currentToken.type.equals("digit")) {
        String tmp = type + " or + ";
        System.out.println("Parser Error: Found " + currentToken.type + " at line " + currentToken.line + " index " + currentToken.index +
                " Expecting " + tmp);
      } else {
        System.out.println("Parser Error: Found " + currentToken.type + " at line " + currentToken.line + " index " + currentToken.index +
                " Expecting " + type);
      }
      err++;
    }
    if (tokenIndex + 1 < ntokens.size()) {
      currentToken = ntokens.get(tokenIndex + 1);
      tokenIndex++;
    } else if (currentToken.type.equals("$")) {
      tokenIndex++;
    }
  }
}
