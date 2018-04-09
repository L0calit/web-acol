/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import modele.Activite;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                String[] classeDiviser = classe.split("/");
                List<String> listeClasse = new ArrayList<String>(Arrays.asList(classeDiviser));
                String creneaux = rs.getString("creneaux");
                result.add(new Activite(nom, listeClasse, creneaux, rs.getInt("prix"), rs.getInt("effectif")));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
	}
	return result;
    }
    
}
