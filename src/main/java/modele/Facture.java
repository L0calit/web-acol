package modele;

/**
 *
 * @author LucBR
 */
public class Facture {

    private int prixTotal;
    private int montantEnleve;
    private int montantFinal;
    private Periode periode;

    public Facture(int prixTotal, int montantEnleve, int montantFinal, Periode periode) {
        this.prixTotal = prixTotal;
        this.montantEnleve = montantEnleve;
        this.montantFinal = montantFinal;
        this.periode = periode;
    }

    public Periode getPeriode() {
        return periode;
    }

    public int getMontantEnleve() {
        return montantEnleve;
    }

    public int getMontantFinal() {
        return montantFinal;
    }

    public int getPrixTotal() {
        return prixTotal;
    }

}
