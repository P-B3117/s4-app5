package src;

import java.util.ArrayList;
import java.util.StringJoiner;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

    // Attributs
    private String chaine;
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
    private ArrayList<Terminal> output = new ArrayList<>();

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

    public ArrayList<Terminal> getOutput() {
        return output;
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
    // TODO regarder le next character so comme ça le isTerminal
    public Terminal prochainTerminal() {
        System.out.println("Prochain terminal");
        if (resteTerminal() == false) {
            System.out.println("No more terminal");
            return null;
        }

        String chaineBackup = chaine;
        int index = 0;
        if (!isTerminal(chaine.charAt(0))) {
            while (!isTerminal(chaine.charAt(0))) {
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

    static boolean isCharChainFlag = false;
    static boolean wasCharChainFlag = false;
    static char lastChar = ' ';

    private boolean isTerminal(char c) {
        boolean returnVal = false;

        if (legalChars.contains(c)) {
            // caractères spéciaux (opérateurs)
            returnVal = true;
            isCharChainFlag = false;
        } else if (
            Character.isUpperCase(c) // une majuscule
        ) {
            if (isCharChainFlag) {
                ErreurLex(c);
                isCharChainFlag = true;
            }
            isCharChainFlag = true;
            returnVal = false;
        } else if (
            Character.isLowerCase(c) || c == '_' // une minuscule
        ) {
            if (!isCharChainFlag) {
                ErreurLex(c);
                isCharChainFlag = false;
            }
            isCharChainFlag = true;
            returnVal = false;
        } else if (
            Character.isDigit(c) // si on as un chiffre
        ) {
            isCharChainFlag = false;
            returnVal = false;
        } else {
            ErreurLex(c);
            returnVal = false;
        }
        // fin de nom de variables
        if (wasCharChainFlag && !isCharChainFlag) {
            returnVal = true;
            wasCharChainFlag = false;
            isCharChainFlag = false;
        }
        if (!wasCharChainFlag && isCharChainFlag) {
            returnVal = true;
        }
        // on se souvient du dernier caractère
        lastChar = c;
        wasCharChainFlag = isCharChainFlag;
        return returnVal;
    }

    /** ErreurLex() envoie un message d'erreur lexicale
     */
    public static void ErreurLex(String s) {
        System.out.println("Erreur lexicale: " + s);
    }

    /** ErreurLex() envoie un message d'erreur lexicale
     */
    public static void ErreurLex(Character c) {
        System.out.println("Erreur lexicale: " + c);
    }

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
