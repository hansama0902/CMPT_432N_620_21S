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

  public void Print() {
    this.codeTable.printString();
  }

  public void initProgram(Semantic semantic) {
    this.translateStatement(semantic.ast.getRoot(), semantic.scopes.get(0));
    this.addBreak();
    this.staticTable.removeTemp(this.codeTable);
    this.codeTable.zero();
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
    } else if (type.equals("Variable")) {
      this.translateVarDec(node, scope);
    } else if (type.equals("Assignment Statement")) {
      this.translateAssignment(node, scope);

    }
  }

  public void translateWhile(Node node, Scope scope) {

  }

  public void translateIF(Node node, Scope scope) {

  }

  public void translatePrint(Node node, Scope scope) {
    if (node.children.get(0).isIdentifier()) {
      StaticData tableEntry = this.staticTable.getItemWithId(node.children.get(0).getType());
      this.addLoadRegYWithMemory(tableEntry.getTemp().charAt(0), tableEntry.getTemp().charAt(1));
      if (tableEntry.getType().equals("int")) {
        this.addLoadRegXWithConstant((char)0x01);
      } else {
        this.addLoadRegXWithConstant((char)0x02);
      }
      this.addSystemCall();
    } else if (node.children.get(0).isInt()) {

      this.translateInt(node.children.get(0), scope);
      this.addStoreInMemory((char)0x00, (char)0x00);
      // system call to print int
      this.addLoadRegXWithConstant((char)0x01);
      this.addLoadRegXWithMemory((char)0x00, (char)0x00);
      this.addSystemCall();
    }

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

    StaticData data = new StaticData(this.staticTable.getCurrentData() ,node.children.get(1).getType(), "int", scope.getNumber(), this.staticTable.getOffset());

    this.staticTable.addData(data);
  }

  public void translateBoolean(Node node, Scope scope) {

  }

  public void translateString(Node node, Scope scope) {

  }

  private char leftPad(String value) {
    int v = Integer.parseInt(value);
    if (v >= 255) {
      return (char)0xFF;
    } else {
      return (char)v;
    }
  }

  public void translateAssignment(Node node, Scope scope) {
    String type = node.children.get(1).getType();
    System.out.println(type);
    if (node.children.get(1).isIdentifier()) {
      // Setting an ID to another ID's value
      StaticData firstTableEntry = this.staticTable.getItemWithId(node.children.get(1).getType());
      StaticData secondTableEntry = this.staticTable.getItemWithId(node.children.get(0).getType());
      if (firstTableEntry == null || secondTableEntry == null) {
        System.out.println("can't find the table entery\n");
        return ;
      }
      this.addLoadWithMemory(firstTableEntry.getTemp().charAt(0), firstTableEntry.getTemp().charAt(1));
      this.addStoreInMemory(secondTableEntry.getTemp().charAt(0), secondTableEntry.getTemp().charAt(1));

    } else if (node.children.get(1).isInt()) {
      // int assignment
      StaticData tableEntry = this.staticTable.getItemWithId(node.children.get(0).getType());
      if (tableEntry == null) {
        System.out.println("can't find the entery");
        return;
      }
      char value = this.leftPad(node.children.get(1).getType());
      this.addLoadWithConstant(value);
      this.addStoreInMemory(tableEntry.getTemp().charAt(0), tableEntry.getTemp().charAt(1));

    } else if (node.children.get(1).isBoolean()) {

    } else {

    }

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

  public void addLoadRegXWithConstant(char constant) {
    char opcode = 0xA2;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(constant);
  }

  public void addLoadRegXWithMemory(char atAddr, char fromAddr) {
    char opcode = 0xAE;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(atAddr);
    this.codeTable.addByte(fromAddr);
  }

  public void addLoadRegYWithConstant(char constant) {
    char opcode = 0xA0;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(constant);
  }

  public void addLoadRegYWithMemory(char atAddr, char fromAddr) {
    char opcode = 0xAC;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(atAddr);
    this.codeTable.addByte(fromAddr);
  }

  public void addAddWithCarry(char atAddr, char fromAddr) {
    char opcode = 0x6D;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(atAddr);
    this.codeTable.addByte(fromAddr);
  }

  public void addInc(char atAddr, char fromAddr) {
    char opcode = 0xEA;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(atAddr);
    this.codeTable.addByte(fromAddr);
  }

  public void addCompareByte(char atAddr, char fromAddr) {
    char opcode = 0xEC;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(atAddr);
    this.codeTable.addByte(fromAddr);
  }

  public void addBranch(char compareByte) {
    char opcode = 0xD0;
    this.codeTable.addByte(opcode);
    this.codeTable.addByte(compareByte);
  }

  public void addNoOperation() {
    char opcode = 0xEA;
    this.codeTable.addByte(opcode);
  }

  public void addBreak() {
    char opcode = 0x00;
    this.codeTable.addByte(opcode);
  }

  public void addSystemCall() {
    char opcode = 0xFF;
    this.codeTable.addByte(opcode);
  }
}
