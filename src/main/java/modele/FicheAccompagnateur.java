package modele;

/**
 *
 * @author bernnico
 */
public class FicheAccompagnateur extends Fiche {

    private String mail;
    private String numeroTel; //format XX-XX-XX-XX-XX

    public FicheAccompagnateur(String mail, String numeroTel, String nom, String prenom) {
        super(nom, prenom);
        this.mail = mail;
        this.numeroTel = numeroTel;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public String getMail() {
        return mail;
    }

    
}
