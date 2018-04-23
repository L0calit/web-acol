package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modele.FicheAccompagnateur;

/**
 *
 * @author delevoyj
 */
public class AccompagnateurDAO extends AbstractDataBaseDAO {

    public AccompagnateurDAO(DataSource ds) {
        super(ds);
    }

    /**
     * Permet de récupérer la liste des email des accompagnateurs
     *
     * @return Liste de string contenant les emails des accompagnateurs
     */
    public List<String> getListEmail() throws DAOException {
        List<String> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT mail FROM accompagnateur");
            while (rs.next()) {
                result.add(rs.getString("mail"));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }
    
    /**
     * Permet de récupérer la liste des emails des accompagnateurs
     * 
     * @return Liste d'objet Fiche Accompagnateur
     * @throws DAOException
     */
    public List<FicheAccompagnateur> getListe() throws DAOException {
        List<FicheAccompagnateur> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
             ResultSet rs = st.executeQuery("SELECT * FROM accompagnateur");
            while (rs.next()) {
                String mail = rs.getString("mail");
                String numeroTel = rs.getString("numTelephone");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                result.add(new FicheAccompagnateur(mail, numeroTel, nom, prenom));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }
    
    /**
     * Permet la suppression d'une entrée dans la base de données accompagnateur
     * 
     * @param mail
     * @throws DAOException
     */
    public void supprimerAccompagnateur(String mail) throws DAOException{
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("DELETE FROM accompagnateur"
                        + " WHERE mail='" + mail + "'");) {
            st.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
    
    /**
     * Permet d'ajouter une entrée accompagnateur dans la base de données
     * 
     * @param nom
     * @param prenom
     * @param mail
     * @param numeroTel
     * @throws DAOException
     */
    public void ajouterAccompagnateur(String nom, String prenom, String mail, 
            String numeroTel) throws DAOException{
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("INSERT INTO accompagnateur"
                        + " (nom, prenom, mail, numTelephone) VALUES (?, ?, ?, ?)");) {
            st.setString(1, nom);
            st.setString(2, prenom);
            st.setString(3, mail);
            st.setString(4, numeroTel);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
    
    /**
     * Permet de verifier si l'accompagnateur existe déjà
     *
     * @param nom
     * @return true si il existe, false sinon
     * @throws DAOException
     */
    public boolean existeDeja(String mail) {
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM accompagnateur WHERE "
                    + "mail='" + mail + "'");
            return rs.next();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
}
