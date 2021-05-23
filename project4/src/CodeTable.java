public class CodeTable {
  private char[] table;
  private int address;
  private int heapPos;


  CodeTable() {
    this.table = new char[256];
    this.address = 0;
    this.heapPos = 255;

    for (int i = 0; i < 256; i++) {
      this.table[i] = 0;
    }
  }

  public void addByte(char bchar) {
    this.table[this.address] = bchar;
    this.address++;
  }

  public void addByte(char bchar, int addr) {
    if (addr >= 256) {
      System.out.println("the addr is out of index");
      return ;
    }
    this.table[addr] = bchar;
  }

  public int getCurrentAddress() {
    return this.address;
  }

  public int getHeapPos() {
    return this.heapPos;
  }

  public void printString() {
    String str = "";
    for (int i = 0; i < 256; i++) {
      str += Character.toString(this.table[i]);
    }
    System.out.println(str);
  }

}
