package src.ast;

import src.Terminal;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

    // Attributs

    /** Constructeur pour l'initialisation d'attributs
     */
    public NoeudAST(Operateur op) {
        // avec arguments
        //
    }

    /** Evaluation de noeud d'AST
     */
    public int EvalAST() {
        return 0;
    }

    /** Lecture de noeud d'AST
     */
    public String LectAST() {
        return "";
    }
}

enum Operateur {
    Addition("+"),
    Soustraction("-"),
    Multiplication("*"),
    Division("/");

    private String symbol;

    Operateur(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Operateur fromToken(Terminal token) {
        for (Operateur op : values()) {
            if (op.getSymbol().equals(token.toString())) {
                return op;
            }
        }

        return null;
    }
}
