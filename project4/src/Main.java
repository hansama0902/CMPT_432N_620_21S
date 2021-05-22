import java.util.Scanner;

public class Main {


  public static void main(String[] args) {
    System.out.println("please input your program : ");
    Lex lex = new Lex();
    Parser parser = new Parser();
    Semantic semantic = new Semantic();
    CodeGen codeGen = new CodeGen();
    lex.initTokenKind();
    Scanner input = new Scanner(System.in);
    String line;
    while(input.hasNext()) {
      line = input.nextLine();
      lex.parse(line);
      if (lex.eop) {
        parser.init(lex.getTokens());
        semantic.initAnalysis(parser);
        codeGen.initProgram(semantic);

        System.out.println("Program " + (parser.program - 1) + " Concrete Syntax Tree");
        System.out.println("----------------------------------------------------");
        parser.tree.printString(parser.program);

        System.out.println("Program " + (parser.program - 1) + " Abstract Syntax Tree");
        System.out.println("----------------------------------------------------");
        semantic.ast.printString(semantic.program);
        lex.eop = false;

        if (semantic.flag) {
          semantic.printScopeString();
        } else {
          System.out.println("not produced due to error(s) detected by semantic analysis.");
          semantic.flag = true;
        }
      }
    }

    input.close();
  }
}
