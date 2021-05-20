import java.util.HashMap;
import java.util.LinkedList;

class JumpItem {
  String  temp;
  int   distance;

  JumpItem(String temp, int dis) {
    this.temp = temp;
    this.distance = dis;
  }

  public String getTemp() {
    return temp;
  }

  public void setTemp(String temp) {
    this.temp = temp;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }


}

public class JumpTable {
  LinkedList<JumpItem> jumpItems;
  String prefix;
  int    number;

  JumpTable() {
    this.jumpItems = new LinkedList<>();
    this.prefix = "J";
    this.number = 0;
  }

  public String getCurrentTemp() {
    return this.prefix + Integer.toString(number);
  }

  public String getNextTemp() {
    this.number++;
    return this.prefix + Integer.toString(number);
  }

  public int getOffset() {
    return this.number;
  }

  public JumpItem getItemAtIndex(int index) {
    if (index >= this.jumpItems.size()) {
      return null;
    }
    return this.jumpItems.get(index);
  }

  public void addItem(JumpItem item) {
    this.jumpItems.push(item);
  }
}
