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
}
