import java.util.Scanner;

public class Parser {

  class Grammar {

  }






  public static void main(String[] args) {
    System.out.println("please input your program : ");
    Lex lex = new Lex();
    lex.initTokenKind();
    Scanner input = new Scanner(System.in);
    lex.parse(input);
    input.close();
  }
}
