import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

enum LOG {
  DEBUG,
  INFO,
  ERROR,
  WARNING,
}

enum STATE {
  SEARCHING,
  STRING,
  DEFAULT,
}


public class Lex {

  ArrayList<Token> tokens = new ArrayList<Token>();
  HashMap<String, String> hmap = new HashMap<String, String>();

  static int lineNum = 0;
  static int linePos = 0;


  public static void log(LOG log , String msg) {
    System.out.println(log + " Lexer - " + msg);
  }

  public void createToken(String type) {
    tokens.add(new Token(type, lineNum, linePos+1));
    log(LOG.INFO,  hmap.get(type) + "[ "+ type + " ] " +" found at (" + lineNum + ", " + linePos+1 + ")");
  }

  public static void parse(Scanner input) {
    String line;
    int len, i = 0;
    char ch;

    while (input.hasNext()) {
      line = input.nextLine();
      len = line.length();
      lineNum = lineNum + 1;
      linePos = 0;

      STATE state = STATE.DEFAULT;

      while (true) {
        ch = line.charAt(i);

        if (i > len) {
          log(LOG.WARNING, "Reached EOL without finding $.");
        }

        switch (state) {
          case DEFAULT:
            if (ch == '$') {

            } else if (ch == '=') {

            } else if (ch == '+') {

            } else if (ch == '(') {

            } else if (ch == ')') {

            } else if (ch == '{') {

            } else if (ch == '}') {

            }
            break;
          case SEARCHING:
            break;
          case STRING:
            break;

        }
      }
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    parse(input);

  }


}