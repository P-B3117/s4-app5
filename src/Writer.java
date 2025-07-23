package src;

import java.io.FileOutputStream;

/** Classe Permettant l'�criture d'un fichier texte
 */
public class Writer {

    String _str;

    /** Constructeur prenant en parametre le nom du fichier et de la donnee, et
     * inserer celle-ci dans celui-la
     */

    public Writer(String name, String data) {
        try {
            FileOutputStream fos = new FileOutputStream(name);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
