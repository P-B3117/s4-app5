package src;

import java.util.ArrayList;
import java.util.StringJoiner;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

    // Attributs
    private String chaine;
    private ArrayList<Terminal> output = new ArrayList<>();

    /** Constructeur pour l'initialisation d'attribut(s)
     */
    public AnalLex(String in) {
        String toWrite = "";
        System.out.println("Debut d'analyse lexicale");

        Reader r = new Reader(in);
        chaine = r.toString();
        chaine = chaine.replaceAll("\\s", "");

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
        if (
            chaine == null ||
            chaine.isEmpty() ||
            chaine.length() == 1 ||
            chaine.charAt(0) == '\n'
        ) {
            return false;
        }
        return true;
    }

    /** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
    */
    // TODO regarder le next character so comme ça le isTerminal
    public Terminal prochainTerminal() {
        resetTerminal();
        System.out.println("Prochain terminal");
        if (resteTerminal() == false) {
            System.out.println("No more terminal");
            return null;
        }

        String chaineBackup = chaine;
        int index = 0;
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
        if (index == 0) {
            index++;
        }
        String token = chaineBackup.substring(0, index);

        chaine = chaineBackup.substring(index);
        System.out.println("Index: " + index + " Chain: " + chaine);
        return new Terminal(token);
    }

    boolean isCharChainFlag = false;
    boolean wasCharChainFlag = false;
    Terminal.CharType lastChar = Terminal.CharType.ERROR;

    private void resetTerminal() {
        lastChar = Terminal.CharType.ERROR;
    }

    private boolean isTerminal(char c) {
        System.out.println("isCharChainFlag: " + isCharChainFlag);
        boolean returnVal = false;
        Terminal.CharType type = Terminal.classifyChar(c);

        switch (type) {
            case Terminal.CharType.OPERATOR:
                System.out.println("OPERATOR");
                // caractères spéciaux (opérateurs)
                returnVal = true;
                isCharChainFlag = false;
                break;
            case Terminal.CharType.UPPERCASE:
                System.out.println("UPPERCASE");
                if (
                    lastChar == Terminal.CharType.LOWERCASE ||
                    lastChar == Terminal.CharType.UNDERSCORE ||
                    lastChar == Terminal.CharType.UPPERCASE
                ) {
                    ErreurLex(c);
                    isCharChainFlag = true;
                }
                isCharChainFlag = true;
                returnVal = false;
                break;
            case Terminal.CharType.LOWERCASE:
                System.out.println("LOWERCASE");
                if (!isCharChainFlag) {
                    ErreurLex(c);
                    isCharChainFlag = false;
                }
                isCharChainFlag = true;
                returnVal = false;
                break;
            case Terminal.CharType.UNDERSCORE:
                System.out.println("UNDERSCORE");
                isCharChainFlag = true;
                returnVal = false;
                break;
            case Terminal.CharType.NUMBER:
                System.out.println("NUMBER");
                returnVal = false;
                isCharChainFlag = false;
                break;
            case Terminal.CharType.RETURN:
                System.out.println("RETURN");
                returnVal = true;
                isCharChainFlag = false;
                break;
            default:
                ErreurLex(c);
                returnVal = false;
                break;
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
        lastChar = type;
        wasCharChainFlag = isCharChainFlag;
        return returnVal;
    }

    /** ErreurLex() envoie un message d'erreur lexicale
     */
    public static void ErreurLex(String s) {
        System.out.println("Erreur lexicale: " + s);
        System.exit(254);
    }

    /** ErreurLex() envoie un message d'erreur lexicale
     */
    public static void ErreurLex(Character c) {
        System.out.println("Erreur lexicale: " + c);
        System.exit(254);
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
