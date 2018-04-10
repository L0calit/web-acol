/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author boussanl
 */
public class Activite extends Prestation{
    private String nom;
    private List<String> classe;
    private String creneaux;
    private int prix;
    private int effectif;
    private String accompagnateur1;
    private String accompagnateur2;

    public Activite(String nom, List<String> classe, String creneaux, int prix, int effectif, String mail1, String mail2) {
        this.nom = nom;
        this.classe = classe;
        this.creneaux = creneaux;
        this.prix = prix;
        this.effectif = effectif;
        this.accompagnateur1 = mail1;
        this.accompagnateur2 = mail2;
    }

    public String getCreneaux() {
        return creneaux;
    }

    public int getEffectif() {
        return effectif;
    }

    public int getPrix() {
        return prix;
    }

    public String getClasse() {
        return classe.toString();
    }


    public String getNom() {
        return nom;
    }
        
    public String getAccompagnateur1() {
        return accompagnateur1;
    }

    public String getAccompagnateur2() {
        return accompagnateur2;
    }

    @Override
    public String toString() {
        return creneaux + ":" + nom + "("+ classe.toString() + ")";
    }
    
}
