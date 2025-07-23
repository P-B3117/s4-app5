package src.ast;

import src.AnalLex;
import src.Terminal;
import src.Writer;

/** @author Ahmed Khoumsi (adapté) */

/** Cette classe effectue l'analyse syntaxique et construit l'AST.
 *  Elle implémente un parser descendant récursif pour des expressions arithmétiques
 *  avec gestion de la priorité et de l'associativité à droite.
 */
public class DescenteRecursive {

    private AnalLex lex;
    private Terminal currentToken;
    private int tokenIndex;
    private java.util.ArrayList<Terminal> tokens;

    /**
     * Constructeur de DescenteRecursive.
     * Reçoit en argument l'analyseur lexical initialisé.
     */
    public DescenteRecursive(AnalLex lex) {
        this.lex = lex;
        this.tokens = lex.getOutput();
        this.tokenIndex = 0;
    }

    /**
     * AnalSynt() effectue l'analyse syntaxique et construit l'AST.
     * Elle retourne une référence sur la racine de l'AST construit.
     * C'est la méthode de démarrage du parsing.
     */
    public ElemAST AnalSynt() {
        if (tokenIndex < tokens.size()) {
            currentToken = tokens.get(tokenIndex);
        } else {
            currentToken = null;
        }

        ElemAST ast = parseExp();

        if (currentToken != null) {
            ErreurSynt(
                "Tokens inattendus à la fin de l'expression : " +
                currentToken.getValue()
            );
        }

        return ast;
    }

    /**
     * Méthode utilitaire pour vérifier le token courant et passer au suivant.
     * @param expectedValue La valeur attendue du token.
     */
    private void match(String expectedValue) {
        if (
            currentToken == null ||
            !currentToken.getValue().equals(expectedValue)
        ) {
            ErreurSynt(
                "Erreur: Attendu '" +
                expectedValue +
                "', mais reçu '" +
                (currentToken != null
                        ? currentToken.getValue()
                        : "FIN_DE_FICHIER") +
                "'"
            );
        }
        tokenIndex++;
        if (tokenIndex < tokens.size()) {
            currentToken = tokens.get(tokenIndex);
        } else {
            currentToken = null;
        }
    }

    /**
     * Implémente la règle: Exp -> Term ExpTail
     * Gère les opérateurs + et -.
     * @return Le noeud AST de l'expression parsée.
     */
    private ElemAST parseExp() {
        ElemAST leftOperand = parseTerm();
        return parseExpTail(leftOperand);
    }

    /**
     * Implémente la règle: ExpTail -> + Term ExpTail | - Term ExpTail | epsilon
     * Gère l'associativité à droite pour + et -.
     * @param leftOperand L'opérande gauche accumulée.
     * @return Le noeud AST résultant.
     */
    private ElemAST parseExpTail(ElemAST leftOperand) {
        if (currentToken != null) {
            if (currentToken.isPlus() || currentToken.isMinus()) {
                Terminal opToken = currentToken;
                match(opToken.getValue());

                ElemAST rightTerm = parseTerm();
                ElemAST rightOperand = parseExpTail(rightTerm); // Associativité à droite

                NoeudAST node = new NoeudAST(Operateur.fromToken(opToken));
                node.left = leftOperand;
                node.right = rightOperand;

                return node;
            }
        }
        return leftOperand;
    }

    /**
     * Implémente la règle: Term -> Fact TermTail
     * Gère les opérateurs * et /.
     * @return Le noeud AST du terme parsé.
     */
    private ElemAST parseTerm() {
        ElemAST leftOperand = parseFact();
        return parseTermTail(leftOperand);
    }

    /**
     * Implémente la règle: TermTail -> * Fact TermTail | / Fact TermTail | epsilon
     * Gère l'associativité à droite pour * et /.
     * @param leftOperand L'opérande gauche accumulée.
     * @return Le noeud AST résultant.
     */
    private ElemAST parseTermTail(ElemAST leftOperand) {
        if (currentToken != null) {
            if (currentToken.isMultiply() || currentToken.isDivide()) {
                Terminal opToken = currentToken;
                match(opToken.getValue());

                ElemAST rightFact = parseFact();
                ElemAST rightOperand = parseTermTail(rightFact); // Associativité à droite

                NoeudAST node = new NoeudAST(Operateur.fromToken(opToken));
                node.left = leftOperand;
                node.right = rightOperand;

                return node;
            }
        }
        return leftOperand;
    }

    /**
     * Implémente la règle: Fact -> Num | Var | (Exp)
     * Gère les feuilles de l'AST et les parenthèses.
     * @return Le noeud AST du facteur parsé (FeuilleAST ou sous-arbre d'expression).
     */
    private ElemAST parseFact() {
        if (currentToken == null) {
            ErreurSynt(
                "Erreur: Attendu un nombre, une variable ou une parenthèse ouvrante, mais fin de fichier."
            );
        }

        if (currentToken.isOpeningParenthesis()) {
            match("(");
            ElemAST exprInParen = parseExp();
            match(")");
            return exprInParen;
        } else if (currentToken.isVariable() || currentToken.isLiteral()) {
            FeuilleAST leaf = new FeuilleAST(currentToken);
            match(currentToken.getValue());
            return leaf;
        } else {
            ErreurSynt(
                "Erreur: Attendu un nombre, une variable ou '(', mais reçu '" +
                currentToken.getValue() +
                "'"
            );
        }
        return null;
    }

    /**
     * ErreurSynt() envoie un message d'erreur syntaxique
     */
    public void ErreurSynt(String s) {
        throw new IllegalStateException("Erreur Syntactique: " + s);
    }

    public static void main(String[] args) {
        var lex = new AnalLex("TestAssociativite.txt");

        System.out.println("Debut d'analyse syntaxique");
        DescenteRecursive dr = new DescenteRecursive(lex);

        try {
            ElemAST RacineAST = dr.AnalSynt();
            String toWriteLect =
                "Lecture de l'AST trouvé : " + RacineAST.LectAST() + "\n";
            System.out.println(toWriteLect);
            String toWriteEval =
                "Evaluation de l'AST trouvé : " + RacineAST.EvalAST() + "\n";
            System.out.println(toWriteEval);

            Writer w = new Writer(
                "ResultatSyntaxique.txt",
                toWriteLect + toWriteEval
            );
            System.out.println("Resultats écrits dans ResultatSyntaxique.txt");
        } catch (Exception e) {
            System.err.println(
                "Une erreur s'est produite lors de l'analyse : " +
                e.getMessage()
            );
            e.printStackTrace();
            System.exit(51);
        }
        System.out.println("Analyse syntaxique terminee");
    }
}
