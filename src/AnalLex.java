package src;

import java.util.ArrayList;
import java.util.StringJoiner;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

    // Attributs
    private String chaine;
    public ArrayList<Terminal> output = new ArrayList<>();

    /** Constructeur pour l'initialisation d'attribut(s)
     */
    public AnalLex(String in) {
        String toWrite = "";
        System.out.println("Debut d'analyse lexicale");

        Reader r = new Reader(in);
        chaine = r.toString();

        // Execution de l'analyseur lexical
        Terminal t = null;
        while (this.resteTerminal()) {
            t = this.prochainTerminal();
            toWrite += t.toString() + "\n"; // toWrite contient le resultat
            output.add(t);
        } //    d'analyse lexicale
        System.out.println(toWrite); // Ecriture de toWrite sur la console
        System.out.println("Fin d'analyse lexicale");
    }

    /** resteTerminal() retourne :
      false  si tous les terminaux de l'expression arithmetique ont ete retournes
      true s'il reste encore au moins un terminal qui n'a pas ete retourne
 */
    public boolean resteTerminal() {
        if (chaine == null || chaine.isEmpty() || chaine.length() == 1) {
            return false;
        }
        return true;
    }

    /** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */
    public Terminal prochainTerminal() {
        System.out.println("Prochain terminal");
        if (resteTerminal() == false) {
            System.out.println("No more terminal");
            return null;
        }

        String chaineBackup = chaine;
        int index = 0;
        if (!isOperator(chaine.charAt(0))) {
            while (!isOperator(chaine.charAt(0))) {
                if ((chaineBackup.length() - 1) == index) break;
                System.out.println(
                    "Index: " +
                    index +
                    "    Chain: " +
                    chaine +
                    "    Chain length: " +
                    (chaineBackup.length() - 1)
                );
                chaine = chaine.substring(1);
                index++;
            }
        } else {
            index++;
        }
        String token = chaineBackup.substring(0, index);

        chaine = chaineBackup.substring(index);
        System.out.println("Index: " + index + " Chain: " + chaine);
        return new Terminal(token);
    }

    private static boolean isOperator(char c) {
        return (
            c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')'
        );
    }

    /** ErreurLex() envoie un message d'erreur lexicale
     */
    public void ErreurLex(String s) {}

    //Methode principale a lancer pour tester l'analyseur lexical
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatLexical.txt";
        }

        AnalLex lexical = new AnalLex(args[0]); // Creation de l'analyseur lexical
        StringJoiner joiner = new StringJoiner("\n", "", ""); // Delimiter, prefix, suffix

        for (Terminal t : lexical.output) {
            joiner.add(t.toString()); // Add the string representation of each object
        }
        Writer w = new Writer(args[1], joiner.toString()); // Ecriture de toWrite dans fichier args[1]
    }
}
