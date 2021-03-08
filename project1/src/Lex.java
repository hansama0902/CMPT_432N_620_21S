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

  static ArrayList<Token> tokens = new ArrayList<Token>();
  static HashMap<String, String> hmap = new HashMap<String, String>();
  static int lineNum = 0;
  static int linePos = 0;

  public static void initTokenKind() {
    hmap.put("{", "L_BRACE");
    hmap.put("}", "R_BRACE");
    hmap.put("(", "L_PAREN" );
    hmap.put(")", "R_PAREN");
    hmap.put("if", "IF");
    hmap.put("ID", "ID");
    hmap.put("while", "WHILE");
    hmap.put("print", "PRINT");
    hmap.put("int", "I_TYPE");
    hmap.put("boolean", "I_TYPE");
    hmap.put("string", "I_TYPE");
    hmap.put("false", "BOOLVAR");
    hmap.put("true", "BOOLVAR");
    hmap.put("\"", "QUOTE");
    hmap.put("char", "CHAR");
    hmap.put("!=", "BOOLOP");
    hmap.put("==", "BOOLOP");
    hmap.put("=", "ASSIGN");
    hmap.put("0-9", "DIGIT");
    hmap.put("+", "OP");
    hmap.put("$", "EOP");
  }

  public static void log(LOG log , String msg) {

        System.out.println(log + " Lexer - " + msg);


  }

  public static void createToken(String type) {
    tokens.add(new Token(type, lineNum, linePos+1));
    log(LOG.DEBUG,  hmap.get(type) + "[ "+ type + " ] " +" found at (" + lineNum + ", " + linePos+1 + ")");
  }

  public static void parse(Scanner input) {
    String line;
    int len, i = 0, f = 0;
    char ch;

    while (input.hasNext()) {
      line = input.nextLine();
      len = line.length();
      lineNum = lineNum + 1;
      linePos = 0;

      STATE state = STATE.DEFAULT;
      while (true) {
        ch = line.charAt(i);

        if (i >= len || f >= len) {
          log(LOG.WARNING, "Reached EOL without finding $.");
        }

        switch (state) {
          case DEFAULT:
            if (ch == '$') {
              createToken(Character.toString(ch));
              if (i < len - 1) {
                log(LOG.WARNING, "Unreachable code. All code after the '$' has been ignored.");
              }
            } else if (ch == '=') {
              if (i+1 < len && line.charAt(i+1) == '=') {
                createToken("==");
              } else {
                createToken("=");
              }

            } else if (ch == '!') {
              if (i+1 < len && line.charAt(i+1) == '=') {
                createToken("!=");
              } else {
                log(LOG.ERROR, "Unrecognized Token: " + ch);
              }
            } else if (ch == '+') {
              createToken(Character.toString(ch));
            } else if (ch == '(') {
              createToken(Character.toString(ch));
            } else if (ch == ')') {
              createToken(Character.toString(ch));
            } else if (ch == '{') {
              createToken(Character.toString(ch));
            } else if (ch == '}') {
              createToken(Character.toString(ch));
            } else if (ch == '\n') {
              ++lineNum;
              linePos = 0;
            }
            break;
          case SEARCHING:
            break;
          case STRING:
            break;

        }

        if (state != STATE.SEARCHING) {
          i++;
          linePos++;
        }
      }
    }
  }

  public static void main(String[] args) {
    initTokenKind();
    Scanner input = new Scanner(System.in);
    parse(input);

  }


}