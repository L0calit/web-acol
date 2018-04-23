package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modele.Facture;
import modele.Periode;

/**
 *
 * @author delevoyj
 */
public class FactureDAO extends AbstractDataBaseDAO {

    public FactureDAO(DataSource ds) {
        super(ds);
    }

    /**
     * Permet de recupérer toutes les factures associés à une fiche parent
     *
     * @param loginParent
     * @return liste d'objet Facture
     * @throws DAOException
     */
    public List<Facture> getAllFacture(String loginParent) throws DAOException {
        List<Facture> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM Factures WHERE "
                    + "loginParent='" + loginParent.trim() + "'");
            while (rs.next()) {
                Periode periode = new Periode(rs.getString("dateDebut").trim(),
                        rs.getString("dateFin").trim());
                result.add(new Facture(rs.getInt("prixTotal"), rs.getInt("montantEnleve"),
                        rs.getInt("montantFinal"), periode));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Permet de recuperer la facture associé à une certaine période et à une
     * certaine fiche parent
     *
     * @param loginParent
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws DAOException
     */
    public Facture getFacture(String loginParent, String dateDebut, String dateFin)
            throws DAOException {
        Facture result = null;
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM Factures WHERE loginParent='"
                    + loginParent.trim() + "' and dateDebut='" + dateDebut.trim() + "' and"
                    + " dateFin='" + dateFin.trim() + "'");
            if (rs.next()) {
                Periode periode = new Periode(dateDebut, dateFin);
                result = new Facture(rs.getInt("prixTotal"), rs.getInt("montantEnleve"),
                        rs.getInt("montantFinal"), periode);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Permet l'ajout d'une facture dans la base de données
     *
     * @param loginParent
     * @param dateDebut
     * @param dateFin
     * @param prixTotal
     * @param montantEnlever
     * @param montantFinal
     * @throws DAOException
     */
    public void ajoutFacture(String loginParent, String dateDebut, String dateFin,
            int prixTotal, int montantEnlever, int montantFinal) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("INSERT INTO Factures "
                        + "(loginParent, dateDebut, dateFin, prixTotal,"
                        + " montantEnleve, montantFinal) VALUES (?, ?, ?, ?, ?, ?)");) {
            st.setString(1, loginParent);
            st.setString(2, dateDebut);
            st.setString(3, dateFin);
            st.setInt(4, prixTotal);
            st.setInt(5, montantEnlever);
            st.setInt(6, montantFinal);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
}
