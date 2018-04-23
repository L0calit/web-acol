package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modele.Periode;

/**
 *
 * @author boussanl
 */
public class PeriodeDAO extends AbstractDataBaseDAO {

    public PeriodeDAO(DataSource ds) {
        super(ds);
    }

    /**
     * Permet de recupérer toutes les périodes dans la base de données
     *
     * @return Liste d'objet Periode
     */
    public List<Periode> getPeriodes() throws DAOException {
        List<Periode> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM Periode");
            while (rs.next()) {
                String dateDebut = rs.getString("dateDebut");
                String dateFin = rs.getString("dateFin");
                result.add(new Periode(dateDebut, dateFin));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Permet de supprimer une période dans la base de données
     *
     * @param dateDebut
     * @param dateFin
     */
    public void supprimerPeriode(String dateDebut, String dateFin) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("DELETE FROM "
                        + "Periode WHERE dateDebut='" + dateDebut
                        + "' AND dateFin='" + dateFin + "'");) {
            st.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet d'ajouter une période dans la base de données
     *
     * @param debut
     * @param fin
     */
    public void ajouterPeriode(String debut, String fin) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("INSERT INTO "
                        + "Periode (dateDebut, dateFin) VALUES (?, ?)");) {
            st.setString(1, debut);
            st.setString(2, fin);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
}
