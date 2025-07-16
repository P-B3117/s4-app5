package src.ast;

import src.AnalLex;
import src.Writer;

/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

    private AnalLex lex;

    /** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
    public DescenteRecursive(AnalLex lex) {
        this.lex = lex;
    }

    /** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
     *    Elle retourne une reference sur la racine de l'AST construit
     */
    public ElemAST AnalSynt() {
        ElemAST current = null;

        while (lex.resteTerminal()) {
            var token = lex.prochainTerminal();

            if (token.isClosingParenthesis()) {
                break;
            } else if (token.isOpeningParenthesis()) {
                var node = AnalSynt();
                if (current instanceof NoeudAST) {
                    ((NoeudAST) current).right = node;
                } else {
                    current = node;
                }
            } else if (token.isOperator()) {
                var noeud = new NoeudAST(Operateur.fromToken(token));
                noeud.left = current;
                current = noeud;
            } else if (token.isVariable()) {
                var node = new FeuilleAST(token);
                if (current instanceof NoeudAST) {
                    ((NoeudAST) current).right = node;
                } else {
                    current = node;
                }
            } else if (token.isLiteral()) {
                var node = new FeuilleAST(token);
                if (current instanceof NoeudAST) {
                    ((NoeudAST) current).right = node;
                } else {
                    current = node;
                }
            }
        }

        return current;
    }

    // Methode pour chaque symbole non-terminal de la grammaire retenue
    // ...
    // ...

    /** ErreurSynt() envoie un message d'erreur syntaxique
     */
    public void ErreurSynt(String s) {
        //
    }

    //Methode principale a lancer pour tester l'analyseur syntaxique
    public static void main(String[] args) {
        String toWriteLect = "";
        String toWriteEval = "";

        var lex = new AnalLex("ExpArith.txt");

        System.out.println("Debut d'analyse syntaxique");
        DescenteRecursive dr = new DescenteRecursive(lex);

        try {
            ElemAST RacineAST = dr.AnalSynt();
            toWriteLect +=
                "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
            System.out.println(toWriteLect);
            toWriteEval +=
                "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
            System.out.println(toWriteEval);
            Writer w = new Writer(
                "ResultatSyntaxique.txt",
                toWriteLect + toWriteEval
            ); // Ecriture de toWrite
            // dans fichier args[1]
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(51);
        }
        System.out.println("Analyse syntaxique terminee");
    }
}
