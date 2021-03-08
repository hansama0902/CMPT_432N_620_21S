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
    log(LOG.DEBUG,  hmap.get(type) + "[ "+ type + " ] " +" found at (" + lineNum + ", " + (linePos+1) + ")");
  }

  public static void parse(Scanner input) {
    String line;
    int len, i, f;
    char ch;
    int program = 1;
    boolean eop = false;
    boolean isKeyword = false;
    log(LOG.INFO, "Lexing program 1...");

    while (input.hasNext()) {
      line = input.nextLine();
      len = line.length();
      lineNum = lineNum + 1;
      linePos = 0;
      i = 0; f = 0;

      if (eop) {
        log(LOG.INFO, "Lexing program " + program + "...");
        eop = false;
      }

      STATE state = STATE.DEFAULT;
      while (true) {
        if (i >= len || f >= len) {
          if (eop) {
            break;
          }
          log(LOG.WARNING, "Reached EOL without finding $.");
          break;
        }

        ch = line.charAt(i);
        switch (state) {
          case DEFAULT:
            if (ch == '$') {
              createToken(Character.toString(ch));
              if (i < len - 1) {
                log(LOG.WARNING, "Unreachable code. All code after the '$' has been ignored.");
              } else {
                program++;
                eop = true;
                log(LOG.INFO, "Lex completed with 0 errors");
                System.out.println("\n");
              }
              break;

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
            } else if (ch == '"') {
              state = STATE.STRING;
              createToken(Character.toString(ch));
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
            } else if (ch >= 'a' && ch <= 'z') {
               state = STATE.SEARCHING;
               f = i + 1;
            }
            break;
          case SEARCHING:
            String s;
            s = line.substring(i, f);
            if (hmap.containsKey(s)) {
              createToken(s);
              state = STATE.DEFAULT;
              linePos += (f - i);
              isKeyword = true;
              break;
            } else {
              f++;
            }
            if (!isKeyword) {
              createToken("char");
            }
            break;
          case STRING:
            if(ch == '"') {
              createToken(Character.toString(ch));
              state = STATE.DEFAULT;
            } else if (ch >= 'a' && ch <='z') {
              createToken("char");
            }
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
    input.close();
  }


}