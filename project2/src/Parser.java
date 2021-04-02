import java.util.ArrayList;

public class Parser {
  Tree tree;
  ArrayList<Token> ntokens;
  int tokenIndex = 0;
  int line = 1;
  Token currentToken;

  public void LOG(String method) {
    System.out.println("PARSER: " + method);
  }

  public void init(ArrayList<Token> tokens) {
    ntokens = tokens;
    tree = new Tree();
    currentToken = ntokens.get(tokenIndex);
    if (currentToken.line == 1) {
      System.out.println("PARSER: Parsing program 1...");
    }
    this.parse();
  }

  public void parse() {
    LOG("parser()");
    this.parseProgram();
  }

  public void parseProgram() {
    LOG("parserProgram()");
    Node node = tree.addBranchNode("<Program>");
    this.parseBlock();
    this.match(node,"$");
    LOG("Parse completed successfully");
    tree.endChildren();
    tree.printString();
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
    if (currentToken.type == "print" ||
            currentToken.type == "ID" ||
            currentToken.type == "int"||
            currentToken.type == "boolean" ||
            currentToken.type == "string" ||
            currentToken.type == "{" ||
            currentToken.type == "while" ||
            currentToken.type == "if"
    ) {

      this.parseStatement();
      this.parseStatementList();
    }
    tree.endChildren();
  }

  public void parseStatement() {
    LOG("parseStatement()");
    if (currentToken.type == "print") {
      this.parsePrintStatement();
    } else if (currentToken.type == "string" ||
               currentToken.type == "int" ||
               currentToken.type == "boolean") {
      this.parseVarDecl();
    } else if (currentToken.type == "while") {
      this.parseWhileStatement();
    } else if (currentToken.type == "if") {
      this.parseIfStatement();
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
    if (currentToken.type == "int") {
      this.match(node,"int");
      this.parseId();
    } else if (currentToken.type == "string") {
      this.match(node,"string");
      this.parseId();
    } else if (currentToken.type == "boolean") {
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
    } else if (currentToken.type == "\"") {
      this.parseStringExpr();
    } else if (currentToken.type == "(" ||
               currentToken.type == "true" ||
               currentToken.type == "false") {
      this.parseBooleanExpr();
    } else if (currentToken.type == "id") {
      this.parseId();
    }
  }

  public void parseIntExpr() {
    LOG("parseIntExpr()");
    Node node = tree.addBranchNode("<Int Expression>");
    if (currentToken.type == "digit") {
      this.match(node,"digit");
      if (currentToken.type == "+") {
        this.match(node,"+");
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
    if (currentToken.type == "true") {
      this.match(node,"true");
    } else if (currentToken.type == "false") {
      this.match(node,"false");
    } else {
      this.match(node,"(");
      this.parseExpr();

      if (currentToken.type == "==") {
        this.match(node,"==");
        this.parseExpr();
        this.match(node,")");
      } else if (currentToken.type == "!=") {
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
    this.match(node,"id");
    tree.endChildren();
  }

  public void parseCharList() {
    LOG("parseCharList()");
    if (currentToken.type == "char") {
      Node node = tree.addBranchNode("<Char List>");
      this.match(node,"char");
      this.parseCharList();
      tree.endChildren();
    } else if (currentToken.type == "space") {
      Node node = tree.addBranchNode("<Char List>");
      this.match(node,"space");
      this.parseCharList();
      tree.endChildren();
    }
  }

  public void match(Node node, String type) {
    if(currentToken.type.equals(type)) {
      tree.addLeafNode(node, currentToken);
    }
    if (tokenIndex + 1 < ntokens.size()) {

      currentToken = ntokens.get(tokenIndex + 1);
      tokenIndex++;
      if (currentToken.line != line && currentToken.index == 0) {
        System.out.println("PARSER: Parsing program" + currentToken.line +"...");
        line = currentToken.line;
        this.parse();
      }
    }
  }
}
