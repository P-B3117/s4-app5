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

    @Override
    public String toString() {
        return chaine;
    }
}
