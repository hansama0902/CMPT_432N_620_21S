import java.util.Scanner;

public class Main {


  public static void main(String[] args) {
    System.out.println("please input your program : ");
    Lex lex = new Lex();
    Parser parser = new Parser();

    lex.initTokenKind();
    Scanner input = new Scanner(System.in);
    lex.parse(input);
    input.close();

    parser.init(lex.getTokens());


  }
}
