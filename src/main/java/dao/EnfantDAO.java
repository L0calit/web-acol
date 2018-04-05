/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author bernnico
 */
public class EnfantDAO extends AbstractDataBaseDAO {

    public EnfantDAO(DataSource ds) {
        super(ds);
    }
    
     /**
     * Renvoie la liste des enfants de la table enfants.
     */
    public List<String> getListeEnfant() {
        List<String> result = new ArrayList<String>();
        try (
	     Connection conn = getConn();
	     Statement st = conn.createStatement();
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM Enfants");
            while (rs.next()) {
                result.add(rs.getString("prenom"));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
	}
	return result;
    }
    
}
