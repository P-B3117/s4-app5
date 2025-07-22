package src;

import java.util.ArrayList;

/** @author Ahmed Khoumsi */

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal {

    public enum CharType {
        OPERATOR,
        UPPERCASE,
        LOWERCASE,
        UNDERSCORE,
        NUMBER,
        RETURN,
        SPACE,
        ERROR,
    }

    private static ArrayList<Character> legalChars = new ArrayList<
        Character
    >() {
        {
            add('+');
            add('-');
            add('*');
            add('/');
            add('(');
            add(')');
            add(' ');
        }
    };

    public static CharType classifyChar(char c) {
        if (c == ' ') {
            return CharType.SPACE;
        } else if (legalChars.contains(c)) {
            return CharType.OPERATOR;
        } else if (Character.isUpperCase(c)) {
            return CharType.UPPERCASE;
        } else if (Character.isLowerCase(c)) {
            return CharType.LOWERCASE;
        } else if (c == '_') {
            return CharType.UNDERSCORE;
        } else if (Character.isDigit(c)) {
            return CharType.NUMBER;
        } else if (c == '\n') {
            return CharType.RETURN;
        } else {
            return CharType.ERROR;
        }
    }

    private String chaine;

    public Terminal(String chaine) {
        this.chaine = chaine;
    }

    public boolean isOperator() {
        return (
            chaine.equals("+") ||
            chaine.equals("-") ||
            chaine.equals("*") ||
            chaine.equals("/")
        );
    }

    public boolean isOpeningParenthesis() {
        return (chaine.equals("("));
    }

    public boolean isClosingParenthesis() {
        return (chaine.equals(")"));
    }

    public boolean isVariable() {
        return (
            !this.isOperator() &&
            !this.isOpeningParenthesis() &&
            !this.isClosingParenthesis() &&
            !this.isLiteral()
        );
    }

    public boolean isLiteral() {
        return (chaine.matches("\\d+"));
    }

    public boolean isPlus() {
        return chaine.equals("+");
    }

    public boolean isMinus() {
        return chaine.equals("-");
    }

    public boolean isMultiply() {
        return chaine.equals("*");
    }

    public boolean isDivide() {
        return chaine.equals("/");
    }

    public String getValue() {
        return chaine;
    }

    @Override
    public String toString() {
        return chaine;
    }
}
