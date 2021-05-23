public class CodeGen {

  private StaticTable staticTable;
  private JumpTable jumpTable;
  private CodeTable codeTable;
  private int jumpCount;

  CodeGen() {
    this.staticTable = new StaticTable();
    this.jumpTable = new JumpTable();
    this.codeTable = new CodeTable();
    this.jumpCount = 0;
  }

  public void initProgram(Semantic semantic) {
    this.translateStatement(semantic.ast.getRoot(), semantic.scopes.get(0));
    this.addBreak();
    this.codeTable.printString();

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

    char constant = 0x00;
    this.addLoadWithConstant(constant);
    this.addStoreInMemory(this.staticTable.getCurrentData().charAt(0), this.staticTable.getCurrentData().charAt(1));

    StaticData data = new StaticData(this.staticTable.getCurrentData() ,node.children.get(1).getValue(), scope.getNumber(), this.staticTable.getOffset());
    this.staticTable.addData(data);
  }

  public void translateBoolean(Node node, Scope scope) {

  }

  public void translateString(Node node, Scope scope) {

  }

  public void translateAssignment(Node node, Scope scope) {

  }

  public void addLoadWithConstant(char constant) {
    char opcode = 0xA9;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(constant);
  }

  public void addLoadWithMemory(char atAddr, char fromAddr) {
    char opcode = 0xAD;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(atAddr);
    this.codeTable.addByte(fromAddr);
  }

  public void addStoreInMemory(char atAddr, char fromAddr) {
    char opcode = 0x8D;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(atAddr);
    this.codeTable.addByte(fromAddr);
  }

  public void addBreak() {
    char opcode = 0x00;
    this.codeTable.addByte(opcode);
  }
}
