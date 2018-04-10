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
                String creneaux = rs.getString("creneaux");
                result.add(new Activite(nom, creneaux, listeClasse, rs.getInt("prix"), 
                        rs.getInt("effectif"), rs.getString("mailaccompagnateur1"), rs.getString("mailaccompagnateur2")));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
	}
	return result;
    }
    
    /**
     * Ajoute l'actuvité dans la table activité
     */
    public void ajouterActivite(String nom, String creneaux, String classe, String prix, String effectif, String mail1, String mail2) {
        try (
	     Connection conn = getConn();
	     PreparedStatement st = conn.prepareStatement
	       ("INSERT INTO activites (nom, creneaux, classe, prix, effectif, mailAccompagnateur1, mailAccompagnateur2) VALUES (?, ?, ?, ?, ?, ?, ?)");
	     ) {
            st.setString(1, nom);
            st.setString(2, creneaux);
            st.setString(3, classe);
            st.setInt(4, Integer.parseInt(prix));
            st.setInt(5, Integer.parseInt(effectif));
            st.setString(6, mail1);
            st.setString(7, mail2);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
    
}
