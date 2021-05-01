import java.util.Scanner;

public class Main {


  public static void main(String[] args) {
    System.out.println("please input your program : ");
    Lex lex = new Lex();
    Parser parser = new Parser();
    Semantic semantic = new Semantic();
    lex.initTokenKind();
    Scanner input = new Scanner(System.in);
    String line;
    while(input.hasNext()) {
      line = input.nextLine();
      lex.parse(line);
      if (lex.eop) {
        parser.init(lex.getTokens());
        semantic.initAnalysis(parser);

        System.out.println("Program " + parser.program + " Concrete Syntax Tree");
        System.out.println("----------------------------------------------------");
        parser.tree.printString(parser.program);

        System.out.println("Program " + parser.program + " Abstract Syntax Tree");
        System.out.println("----------------------------------------------------");
        semantic.ast.printString(semantic.program);
        lex.eop = false;

      }
    }

    input.close();
  }
}
