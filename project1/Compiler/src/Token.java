public class Token {
  //Token attributes
  String type;
  int line;
  int index;
  int value;
  String name;


  //Default Constructor
  public Token(){}

  public Token(String type, int line, int index)
  {
    this.type = type;
    this.line = line;
    this.index = index;
  }

  public Token(String type, int line, int index, int value)
  {
    this.type = type;
    this.line = line;
    this.index = index;
    this.value = value;
  }

  public Token(String type, int line, int index, String name)
  {
    this.type = type;
    this.line = line;
    this.index = index;
    this.name = name;
  }

  public Token(String type, int line, int index, int value, String name)
  {
    this.type = type;
    this.line = line;
    this.index = index;
    this.value = value;
    this.name = name;
  }
}
