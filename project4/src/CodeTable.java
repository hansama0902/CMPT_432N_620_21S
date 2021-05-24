public class CodeTable {
  private char[] table;
  private int address;
  private int heapPos;


  CodeTable() {
    this.table = new char[256];
    this.address = 0;
    this.heapPos = 255;

    for (int i = 0; i < 256; i++) {
      this.table[i] = '.';
    }
  }

  public char getByte(int index) {
    if (index > 255) {
      System.out.println("out of index");
      return 0;
    }
    return this.table[index];
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
    for (int i = 0; i < 256; i++) {
      if(i%8 == 0) {
        System.out.printf("\n%02x ", ((int)this.table[i]));
      } else {
        System.out.printf("%02x ",((int)this.table[i]));
      }
    }
  }

  public void zero() {
    for (int i = 0; i < 256; i++) {
      if (this.table[i] == '.') {
        this.table[i] = 0;
      }
    }
  }

}
