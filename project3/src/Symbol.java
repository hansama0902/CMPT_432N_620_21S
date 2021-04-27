public class Symbol {
  String name;
  String type;
  int line;
  boolean isInitialized;

  public Symbol(String name, String type, int line) {
    this.name = name;
    this.type = type;
    this.line = line;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public boolean isInitialized() {
    return isInitialized;
  }

  public void setInitialized(boolean initialized) {
    isInitialized = initialized;
  }



}
