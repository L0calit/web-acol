/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import modele.Activite;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import modele.FicheEnfant;

/**
 *
 * @author boussanl
 */
public class ActiviteDAO extends AbstractDataBaseDAO {

    public ActiviteDAO(DataSource ds) {
        super(ds);
    }
    
    /**
     * Renvoie la liste des regimes de la table regimes.
     */
    public List<Activite> getListeActivite() {
        List<Activite> result = new ArrayList<Activite>();
        try (
	     Connection conn = getConn();
	     Statement st = conn.createStatement();
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM Activites");
            while (rs.next()) {
                String nom = rs.getString("nom");
                String classe = rs.getString("classe");
                List<String> listeClasse = new ArrayList<String>();
                if (classe.contains("/")) {
                    String[] classeDiviser = classe.split("/");
                    listeClasse = Arrays.asList(classeDiviser);
                }
                else {
                    listeClasse.add(classe);
                }
                String creneauxJour = rs.getString("creneauxJour");
                String creneauxHeure = rs.getString("creneauxHeure");
                result.add(new Activite(nom, creneauxJour, creneauxHeure, listeClasse, rs.getInt("prix"), 
                        rs.getInt("effectif"), rs.getString("mailaccompagnateur1"), rs.getString("mailaccompagnateur2")));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
	}
	return result;
    }
    
    public List<Activite> getListeActivite(FicheEnfant ficheEnfant) {
        List<Activite> result = this.getListeActivite();
        for (Activite activite : result) {
            if (activite.testClasse(ficheEnfant.getClasse())) {
                // L'enfant peut être ajouté à cette activité
            }
        }
        return result;
    }
    
    /**
     * Ajoute l'actuvité dans la table activité
     */
    public void ajouterActivite(String nom, String creneauxJour, String creneauxHeure, String classe, int prix, int effectif, String mail1, String mail2) {
        try (
	     Connection conn = getConn();
	     PreparedStatement st = conn.prepareStatement
	       ("INSERT INTO activites (nom, creneauxJour, creneauxHeure, classe, prix, effectif, mailAccompagnateur1, mailAccompagnateur2) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
	     ) {
            st.setString(1, nom);
            st.setString(2, creneauxJour);
            st.setString(3, creneauxHeure);
            st.setString(4, classe);
            st.setInt(5, prix);
            st.setInt(6, effectif);
            st.setString(7, mail1);
            st.setString(8, mail2);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
    
}
