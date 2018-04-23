package modele;

/**
 *
 * @author bernnico
 */
public class Garderie extends Prestation {

    private String creneauxJour;
    private String creneauxHeure;
    private String dateDebut;
    private String dateFin;
    private int prix = 10;

    public Garderie(String creneauxJour, String creneauxHeure, String dateDebut, String dateFin) {
        this.creneauxJour = creneauxJour;
        this.creneauxHeure = creneauxHeure;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getPrix() {
        return prix;
    }

    public Periode getPeriode() {
        return new Periode(dateDebut, dateFin);
    }

    public String getCreneauxHeure() {
        return creneauxHeure;
    }

    public String getCreneauxJour() {
        return creneauxJour;
    }
}
