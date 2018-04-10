/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modele.Cantine;
import modele.FicheEnfant;
import modele.FicheParent;

/**
 *
 * @author boussanl
 */
public class ParentDAO extends AbstractDataBaseDAO {
    
    public ParentDAO(DataSource ds) {
        super(ds);
    }
    
    /**
     * Renvoie la fiche parent de la table parents avec tout ses attributs.
     */
    public FicheParent getFicheParent(String login) {
        FicheParent result = null;
        try (
	     Connection conn = getConn();
	     Statement st = conn.createStatement();
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM Parent WHERE login='" + login +"'");
            String nom = null, prenom = null, adresse = null;
            while (rs.next()) {
                nom = rs.getString("nom");
                prenom = rs.getString("prenom");
                adresse = rs.getString("adresse");
            }
            
            List<FicheEnfant> enfants = new ArrayList<FicheEnfant>();    
            rs = st.executeQuery("SELECT * FROM Enfant WHERE loginParent='" + login +"'");
            while (rs.next()) {
                String nomEnfant = rs.getString("nom");
                String sexe = rs.getString("sexe");
                String prenomEnfant = rs.getString("prenom");
                String dateNaissance = rs.getString("datedeNaissance");
                String divers = rs.getString("divers");
                String regime = rs.getString("regime");
                String classe = rs.getString("classe");
                Cantine cantine = new Cantine(rs.getString("cantine"));
                enfants.add(new FicheEnfant(sexe, classe, dateNaissance, divers, regime, result, cantine, null, nomEnfant, prenomEnfant));
            }
            result = new FicheParent(adresse, login, enfants, nom, prenom);

        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
	}
	return result;
    }
    
    public boolean verify(String login, String password) {
        boolean test = false;
        try (
	     Connection conn = getConn();
	     Statement st = conn.createStatement();
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM Parent WHERE login='" + login +"' AND mdp='"+password+"'");

            if (rs.next()) {
                test = true;
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
	}
        return test;
    }
    
    
}
