/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author bernnico
 */
public class FicheEnfant extends Fiche {
    private String sexe; //M ou F
    private String classe;
    private String dateNaissance; //format JJ/MM/AAAA
    private String divers;
    private String regime;
    private FicheParent parents;
    private Cantine cantine;
    private Prestation prestation;

    public FicheEnfant(String sexe, String classe, String dateNaissance, String divers, String regime, FicheParent parents, Cantine cantine, Prestation prestation, String nom, String prenom) {
        super(nom, prenom);
        this.sexe = sexe;
        this.classe = classe;
        this.dateNaissance = dateNaissance;
        this.divers = divers;
        this.regime = regime;
        this.parents = parents;
        this.cantine = cantine;
        this.prestation = prestation;
    }
  
}
