import java.util.Scanner;

public class Main {


  public static void main(String[] args) {
    System.out.println("please input your program : ");
    Lex lex = new Lex();
    Parser parser = new Parser();

    lex.initTokenKind();
    Scanner input = new Scanner(System.in);
    String line;
    while(input.hasNext()) {
      line = input.nextLine();
      lex.parse(line);
      parser.init(lex.getTokens());
    }

    input.close();
  }
}
