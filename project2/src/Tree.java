public class Tree {
  Node root;
  Node currentNode;

  Tree() {
    this.root = null;
    this.currentNode = null;
  }

  public Node getRoot() {
    return root;
  }

  public void setRoot(Node root) {
    this.root = root;
  }

  public Node getCurrentNode() {
    return currentNode;
  }

  public void setCurrentNode(Node currentNode) {
    this.currentNode = currentNode;
  }

  public void addBranchNode(String type) {
    Node node = new Node();
    node.setType(type);

    if (this.root == null) {
      this.root = node;
      this.currentNode = node;
    } else {
      this.currentNode.addChild(node);
      node.setParent(this.currentNode);
      this.currentNode = node;
    }
  }


  public void addLeafNode(Token token) {
    Node node = new Node();
    node.setType(token.type);
    node.setValue(token.value);
    node.setLeafNode(true);
    node.setLineNumber(token.line);

    if (this.root == null) {
      // log an error message, throw error

    } else {
      this.currentNode.addChild(node);
      node.setParent(this.currentNode);
    }
  }

  public String toString() {

    return null;
  }

}
