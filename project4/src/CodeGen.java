public class CodeGen {

  private StaticTable staticTable;
  private JumpTable jumpTable;
  private int jumpCount;

  CodeGen() {
    this.staticTable = new StaticTable();
    this.jumpTable = new JumpTable();
    this.jumpCount = 0;
  }

  public void initProgram(Semantic semantic) {

  }

  public void translateBlock(Node node, Scope scope) {
    for(int i = 0; i < node.children.size(); i++) {
      this.translateStatement(node.children.get(i), scope);
    }
  }

  public void translateStatement(Node node, Scope scope) {
    String type = node.getType();
    if (type.equals("Block")) {
      this.translateBlock(node, scope);
    } else if (type.equals("While Statement")) {
      this.translateWhile(node, scope);
    } else if (type.equals("If Statement")) {
      this.translateIF(node, scope);
    } else if (type.equals("Print Statement")) {
      this.translatePrint(node, scope);
    } else if (type.equals("Variable Declaration")) {
      this.translateVarDec(node, scope);
    } else if (type.equals("Assignment Statemen")) {
      this.translateAssignment(node, scope);

    }
  }

  public void translateWhile(Node node, Scope scope) {

  }

  public void translateIF(Node node, Scope scope) {

  }

  public void translatePrint(Node node, Scope scope) {

  }

  public void translateVarDec(Node node, Scope scope) {
    String type = node.children.get(0).getType();
    if (type.equals("int")) {
      this.translateInt(node, scope);
    } else if (type.equals("boolean")) {
      this.translateBoolean(node, scope);
    } else if (type.equals("string")) {
      this.translateString(node, scope);
    }
  }

  public void translateInt(Node node, Scope scope) {

  }

  public void translateBoolean(Node node, Scope scope) {

  }

  public void translateString(Node node, Scope scope) {

  }

  public void translateAssignment(Node node, Scope scope) {

  }
}
