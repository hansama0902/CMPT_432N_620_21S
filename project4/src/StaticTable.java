import java.util.LinkedList;

class StaticData {
  private String temp;

  // var name
  private String varName;

  // var type
  private String type;

  // var scope line
  private int scope;

  // static Table offset
  private int offset;

  StaticData(String tmp, String varName, String type, int scope, int offset) {
    this.temp = tmp;
    this.varName = varName;
    this.type = type;
    this.scope = scope;
    this.offset = offset;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public void incTemp() {
    this.number++;
  }

  public void addData(StaticData data) {
    this.staticDatas.push(data);
  }

  public StaticData getItemWithId(String temp) {
    for (int i = 0; i < this.staticDatas.size(); i++) {
      if (this.staticDatas.get(i).getVarName().equals(temp)) {
        return this.staticDatas.get(i);
      }
    }
    return null;
  }

  public StaticData getItemWithTemp(String temp) {
    for (int i = 0; i < this.staticDatas.size(); i++) {
      if (this.staticDatas.get(i).getTemp().equals(temp)) {
        return this.staticDatas.get(i);
      }
    }
    return null;
  }

  public void removeTemp(CodeTable codeTable) {
    String temp = "";
    for(int i = 0; i < 255; i++) {
      if (codeTable.getByte(i) == 'T') {
        temp = "";
        temp += codeTable.getByte(i);
        temp += codeTable.getByte(i+1);
        StaticData staticData = this.getItemWithTemp(temp);

        codeTable.addByte((char)(staticData.getTemp().charAt(1) - '0' + codeTable.getCurrentAddress()), i);
        codeTable.addByte((char)0x00, (i + 1));
      }
    }
  }

}
