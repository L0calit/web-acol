package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 *
 * @author bernnico
 */
public class EmployeDAO extends AbstractDataBaseDAO {

    public EmployeDAO(DataSource ds) {
        super(ds);
    }

    /**
     * Verifie si la combinaison mot de passe et login sont correctes
     *
     * @param login
     * @param password
     * @return true si le mot de passe et login sont correctes, false sinon
     * @throws DAOException
     */
    public boolean verify(String login, String password) throws DAOException {
        boolean test = false;
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM EMPLOYE WHERE login='"
                    + login + "' AND mdp='" + password + "'");
            if (rs.next()) {
                test = true;
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return test;
    }

    /**
     * Permet l'ajout d'une entrée login, mot de passe dans la base de données
     *
     * @param login
     * @param password
     * @throws DAOException
     */
    public void creation(String login, String password) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("INSERT INTO EMPLOYE (login,mdp)"
                        + "VALUES"
                        + "('" + login + "','" + password + "')");) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet de verifier si le login est bien dans la base de données
     *
     * @param login
     * @return
     */
    public boolean verifyLogin(String login) throws DAOException {
        boolean test = false;
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM EMPLOYE WHERE "
                    + "login='" + login + "'");
            if (rs.next()) {
                test = true;
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return test;
    }
}
