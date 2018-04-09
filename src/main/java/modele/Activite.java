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
public class Activite {
    private String nom;
    private List<String> classe;
    private String jour;

    public Activite(String nom, List<String> classe, String jour) {
        this.nom = nom;
        this.classe = classe;
        this.jour = jour;
    }

    public List<String> getClasse() {
        return classe;
    }

    public String getJour() {
        return jour;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return jour + ":" + nom + "("+ classe.toString() + ")";
    }
    
}
