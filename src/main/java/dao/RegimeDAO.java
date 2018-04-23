package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author boussanl
 */
public class RegimeDAO extends AbstractDataBaseDAO {

    public RegimeDAO(DataSource ds) {
        super(ds);
    }

    /**
     * Permet de récuperer la liste des régimes dans la base de données
     *
     * @return
     * @throws DAOException
     */
    public List<String> getListeRegime() throws DAOException {
        List<String> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM Regimes ORDER BY regime");
            while (rs.next()) {
                result.add(rs.getString("regime"));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Permet de supprimer de la base données un régime donné
     *
     * @param regime
     * @throws DAOException
     */
    public void supprimerRegime(String regime) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("DELETE FROM regimes"
                        + " WHERE regime='" + regime + "'");) {
            st.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet d'ajouter un régime dans la base de données
     *
     * @param regime
     * @throws DAOException
     */
    public void ajouterRegime(String regime) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("INSERT INTO regimes"
                        + " (regime) VALUES (?)");) {
            st.setString(1, regime);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet de verifier si le régime existe déjà
     *
     * @param nom
     * @return true si il existe, false sinon
     * @throws DAOException
     */
    public boolean existeDeja(String nom) throws DAOException {
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM regimes WHERE "
                    + "regime='" + nom + "'");
            return rs.next();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
}
