package src.ast;

import src.Terminal;

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

    // Attribut(s)
    private Terminal token;

    /**Constructeur pour l'initialisation d'attribut(s)
     */
    public FeuilleAST(Terminal token) {
        this.token = token;
    }

    /** Evaluation de feuille d'AST
     */
    public int EvalAST() {
        if (token.isLiteral()) {
            return Integer.parseInt(token.getValue());
        } else {
            // Pour les variables, on pourrait retourner une valeur par défaut
            // ou implémenter un système de variables
            ErreurEvalAST("Variable non supportée: " + token.getValue());
            return 0;
        }
    }

    /** Lecture de chaine de caracteres correspondant a la feuille d'AST
     */
    public String LectAST() {
        return token.getValue();
    }
}
