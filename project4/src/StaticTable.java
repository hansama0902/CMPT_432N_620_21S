import java.util.LinkedList;

class StaticData {
  private String temp;
  private String varName;
  private int scope;
  private int offset;

  StaticData(String tmp, String varName, int scope, int offset) {
    this.temp = tmp;
    this.varName = varName;
    this.scope = scope;
    this.offset = offset;
  }

  public String getTemp() {
    return temp;
  }

  public void setTemp(String temp) {
    this.temp = temp;
  }

  public String getVarName() {
    return varName;
  }

  public void setVarName(String varName) {
    this.varName = varName;
  }

  public int getScope() {
    return scope;
  }

  public void setScope(int scope) {
    this.scope = scope;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }
}


public class StaticTable {
  LinkedList<StaticData> staticDatas;
  private String prefix;
  private int number;

  StaticTable() {
    this.staticDatas = new LinkedList<>();
    this.prefix = "T";
    this.number = 0;
  }

  public String getCurrentData() {
    return this.prefix + Integer.toString(this.number);
  }

  public String getNextData() {
    this.number++;
    return this.prefix + Integer.toString(this.number);
  }

  public int getOffset() {
    return this.number;
  }

  public StaticData getDataAtIndex(int index) {
    if (index >= this.staticDatas.size()) {
      return null;
    }
    return this.staticDatas.get(index);
  }

  public void addData(StaticData data) {
    this.staticDatas.push(data);
  }
}
