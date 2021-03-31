import sun.jvm.hotspot.ui.tree.CStringTreeNodeAdapter;

import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
  Tree CST = new Tree();

  public void LOG(String method) {
    System.out.println("PARSER: " + method);
  }

  public void parse() {
    LOG("parser()");
  }

  public void parseProgram() {
    LOG("parserProgram()");
  }

  public void parseBlock() {
    LOG("parseBlock()");
  }

  public void parseStatementList() {
    LOG("parseStatementList()");
  }

  public void parseStatement() {
    LOG("parseStatement()");
  }

  public void parsePrintStatement() {
    LOG("parsePrintStatement()");
  }

  public void parseAssignmentStatement() {
    LOG("parseAssignmentStatement()");
  }

  public void parseVarDecl() {
    LOG("parseVarDecl()");
  }

  public void parseWhileStatement() {
    LOG("parseWhileStatement()");
  }

  public void parseIfStatement() {
    LOG("parseIfStatement()");
  }

  public void parseExpr() {
    LOG("parseExpr()");
  }

  public void parseIntExpr() {
    LOG("parseIntExpr()");
  }

  public void parseStringExpr() {
    LOG("parseStringExpr()");
  }

  public void parseBooleanExpr() {
    LOG("parseBooleanExpr()");
  }

  public void parseId() {
    LOG("parseId()");
  }

  public void parseCharList() {
    LOG("arseCharList()");
  }

  public void match(String type) {

  }
}
