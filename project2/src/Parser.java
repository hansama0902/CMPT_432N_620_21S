import java.util.ArrayList;

public class Parser {
  Tree tree;
  ArrayList<Token> ntokens;
  int tokenIndex = 0;
  Token currentToken;

  public void LOG(String method) {
    System.out.println("PARSER: " + method);
  }

  public void init(ArrayList<Token> tokens) {
    ntokens = tokens;
    tree = new Tree();
    currentToken = ntokens.get(tokenIndex);
    this.parse();
  }

  public void parse() {
    LOG("parser()");
    this.parseProgram();
  }

  public void parseProgram() {
    LOG("parserProgram()");
    tree.addBranchNode("<Program>");
    this.parseBlock();
    this.match("$");
    tree.endChildren();
  }

  public void parseBlock() {
    LOG("parseBlock()");
    tree.addBranchNode("Block");
    this.match("{");
    this.parseStatementList();
    this.match("}");
    tree.endChildren();
  }

  public void parseStatementList() {
    LOG("parseStatementList()");
    if (currentToken.type == "print" ||
            currentToken.type == "ID" ||
            currentToken.type == "int"||
            currentToken.type == "boolean" ||
            currentToken.type == "string" ||
            currentToken.type == "{" ||
            currentToken.type == "while" ||
            currentToken.type == "if"
    ) {
      tree.addBranchNode("Statement List");
      this.parseStatement();
      this.parseStatementList();
      tree.endChildren();
    }
  }

  public void parseStatement() {
    LOG("parseStatement()");

  }

  public void parsePrintStatement() {
    LOG("parsePrintStatement()");
    tree.addBranchNode("Print Statement");
    this.match("print");
    this.match("(");
    this.parseExpr();
    this.match(")");
    tree.endChildren();
  }

  public void parseAssignmentStatement() {
    LOG("parseAssignmentStatement()");
    tree.addBranchNode("Assignment Statement");
    this.parseId();
    this.match("=");
    this.parseExpr();
    tree.endChildren();
  }

  public void parseVarDecl() {
    LOG("parseVarDecl()");
  }

  public void parseWhileStatement() {
    LOG("parseWhileStatement()");
    tree.addBranchNode("While Statement");
    this.match("while");
    this.parseBooleanExpr();
    this.parseBlock();
    tree.endChildren();
  }

  public void parseIfStatement() {
    LOG("parseIfStatement()");
    tree.addBranchNode("If Statement");
    this.match("if");
    this.parseBooleanExpr();
    this.parseBlock();
    tree.endChildren();
  }

  public void parseExpr() {
    LOG("parseExpr()");
  }

  public void parseIntExpr() {
    LOG("parseIntExpr()");
    tree.addBranchNode("Int Expression");
    if (currentToken.type == "digit") {
      this.match("digit");
      if (currentToken.type == "+") {
        this.match("+");
        this.parseExpr();
      }
    }
    tree.endChildren();
  }

  public void parseStringExpr() {
    LOG("parseStringExpr()");
    tree.addBranchNode("String Expression");
    this.match("\"");
    this.parseCharList();
    this.match("\"");
    tree.endChildren();
  }

  public void parseBooleanExpr() {
    LOG("parseBooleanExpr()");

  }

  public void parseId() {
    LOG("parseId()");
    tree.addBranchNode("Identifier");
    this.match("id");
    tree.endChildren();
  }

  public void parseCharList() {
    LOG("parseCharList()");
    if (currentToken.type == "char") {
      tree.addBranchNode("Char List");
      this.match("char");
      this.parseCharList();
      tree.endChildren();
    } else if (currentToken.type == "space") {
      tree.addBranchNode("Char List");
      this.match("space");
      this.parseCharList();
      tree.endChildren();
    }
  }

  public void match(String type) {
    if(currentToken.type == type) {
      tree.addLeafNode(currentToken);
    }

    if (tokenIndex < ntokens.size()) {
      currentToken = ntokens.get(tokenIndex);
      tokenIndex++;
    }
  }
}
