package src.ast;

import src.Terminal;

/** Classe representant un noeud d'AST
 */
public class NoeudAST extends ElemAST {

    public ElemAST left;
    public ElemAST right;
    private Operateur operateur;

    /** Constructeur pour l'initialisation d'attributs
     */
    public NoeudAST(Operateur op) {
        this.operateur = op;
    }

    /** Evaluation de noeud d'AST
     */
    public int EvalAST() {
        int leftVal = left.EvalAST();
        int rightVal = right.EvalAST();
        
        switch (operateur) {
            case Addition:
                return leftVal + rightVal;
            case Soustraction:
                return leftVal - rightVal;
            case Multiplication:
                return leftVal * rightVal;
            case Division:
                if (rightVal == 0) {
                    ErreurEvalAST("Division par zéro");
                    return 0;
                }
                return leftVal / rightVal;
            default:
                ErreurEvalAST("Opérateur non supporté: " + operateur);
                return 0;
        }
    }

    /** Lecture de noeud d'AST
     */
    public String LectAST() {
        return "(" + left.LectAST() + " " + operateur.getSymbol() + " " + right.LectAST() + ")";
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
