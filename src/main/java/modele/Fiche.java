package modele;

/**
 *
 * @author bernnico
 */
public abstract class Fiche {

    private String nom;
    private String prenom;

    public Fiche(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

}
