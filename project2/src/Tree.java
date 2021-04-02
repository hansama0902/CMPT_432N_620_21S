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

  public Node addBranchNode(String type) {
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
    return this.currentNode;

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

  public void addLeafNode(Node nd,  Token token) {
    Node node = new Node();
    node.setType(token.type);
    node.setValue(token.value);
    node.setLeafNode(true);
    node.setLineNumber(token.line);

    if (this.root == null) {
      // log an error message, throw error

    } else {
      nd.addChild(node);
      node.setParent(nd);
    }
  }

  public void endChildren() {
    if ((this.currentNode.getParent() != null))
    {
      this.currentNode = this.currentNode.getParent();
    }
  }

  public void traverse(Node node, int depth, StringBuilder res)
  {
    String tmp = "";
    for (int i = 0; i < depth; i++)
    {
      tmp += "-" ;
    }

    if (node.children.isEmpty() || node.children.size() == 0)
    {
      tmp += "[ " + node.type + " ]";
      tmp += "\n";
      res.append(tmp);
    }
    else
    {
      tmp +=   node.type + "\n";
      res.append(tmp);
      for (int i = 0; i < node.children.size(); i++)
      {
        traverse(node.children.get(i), depth + 1, res);
      }
    }
  }
  public void printString(int program) {
    StringBuilder res = new StringBuilder();
    System.out.println("CST for program " + program + "…");
    traverse(this.root, 0, res);
    System.out.println(res);
  }

}
