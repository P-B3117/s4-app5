package src;

/** @author Ahmed Khoumsi */

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal {

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

    @Override
    public String toString() {
        return chaine;
    }
}
