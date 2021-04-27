import java.util.LinkedList;

public class Node {
  String type;
  String value;
  int lineNumber;
  Node parent;
  LinkedList<Node> children = new LinkedList<Node>();
  boolean isIdentifier;
  boolean isInt;
  boolean isLeafNode;
  boolean isBoolean;

  Node() {}

  Node(String type) {
    if (type == "") {
      this.type = "";
    } else {
      this.type = type;
    }
    this.value = "";
    this.children = null;
    this.parent = null;
    this.lineNumber = 0;
    this.isLeafNode = false;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public void addChild(Node node) {
    this.children.add(node);
  }

  public boolean isIdentifier() {
    return isIdentifier;
  }

  public void setIdentifier(boolean identifier) {
    isIdentifier = identifier;
  }

  public boolean isInt() {
    return isInt;
  }

  public void setInt(boolean anInt) {
    isInt = anInt;
  }

  public boolean isLeafNode() {
    return isLeafNode;
  }

  public void setLeafNode(boolean leafNode) {
    isLeafNode = leafNode;
  }

  public boolean isBoolean() {
    return isBoolean;
  }

  public void setBoolean(boolean aBoolean) {
    isBoolean = aBoolean;
  }

}
