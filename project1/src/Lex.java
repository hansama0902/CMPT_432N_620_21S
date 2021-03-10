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
  SKIP,
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

  public static void createToken(String type, String a) {
    tokens.add(new Token(type, lineNum, linePos+1));
    log(LOG.DEBUG,  hmap.get(type) + "[ "+ a + " ] " +" found at (" + lineNum + ", " + (linePos+1) + ")");
  }
  public static void parse(Scanner input) {
    String line;
    int len, i, f;
    char ch, ch2;
    int program = 1;
    int err = 0;
    boolean eop = false;
    boolean isKeyword = false;
    boolean isComment = false;

    while (input.hasNext()) {
      line = input.nextLine();
      len = line.length();
      lineNum = lineNum + 1;
      linePos = 0;
      i = 0; f = 0;

      if (lineNum == 1 && linePos == 0) {
        log(LOG.INFO, "Lexing program 1 ...");
      }
      
      if (eop) {
        log(LOG.INFO, "Lexing program " + program + " ...");
        eop = false;
      }

      STATE state = STATE.DEFAULT;
      while (true) {
        if (i >= len) {
          if (eop) {
            break;
          }
          //log(LOG.WARNING, "Reached EOL without finding $." + i);
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
                if (err == 0) {
                  log(LOG.INFO, "Lex completed with 0 errors");
                } else {
                  log(LOG.ERROR, "Lex failed with " + err + " error(s)");
                }
                System.out.println("\n");
                err = 0;
                isComment = false;
                isKeyword = false;
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
               f = i;
            } else if (ch == '/') {
              if (i < len -1 && line.charAt(i+1) == '*') {
                i++;
                state = STATE.SKIP;
              }
            } else if (ch == '\t') {
              break;
            } else if( ch == ' ') {

            } else {
              err++;
              log(LOG.ERROR, "Error:" +lineNum + ":" + linePos +" Unrecognized Token:" + Character.toString(ch));
            }
            break;
          case SEARCHING:
            String s;
            s = line.substring(i, f);
            ch2 = line.charAt(f);
            if (hmap.containsKey(s)) {
              createToken(s);
              state = STATE.DEFAULT;
              linePos += (f-i);
              i = f - 1;
              isKeyword = true;
            } else {
              f++;
            }

            if (!isKeyword && (ch2 > 'z' || ch2 < 'a') || f >= len) {
              createToken("char", Character.toString(ch));
              state = STATE.DEFAULT;
            }
            isKeyword = false;
            break;
          case STRING:
            if(ch == '"') {
              createToken(Character.toString(ch));
              state = STATE.DEFAULT;
            } else if (ch >= 'a' && ch <='z') {
              createToken("char");
            }
            break;
          case SKIP:
            while(i < len) {
              if (line.charAt(i) == '*' && line.charAt(i+1) == '/') {
                state = STATE.DEFAULT;
                isComment = true;
                break;
              }
              i++;
              linePos++;
            }
            if (!isComment && i >= len) {
              log(LOG.ERROR, "/* */ not paired");
              err++;
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