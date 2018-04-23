package dao;

import java.sql.*;
import javax.sql.DataSource;

/**
 *
 * @author delevoyj
 */
public class AbsenceDAO extends AbstractDataBaseDAO {

    public AbsenceDAO(DataSource ds) {
        super(ds);
    }

    /**
     * Permet l'ajout a la base de donnée d'une absence à une activité
     *
     * @param nomActivite
     * @param creneauxJour
     * @param creneauxHeure
     * @param dateAbsence
     * @param loginParent
     * @param prenomEnfant
     */
    public void ajoutAbsence(String nomActivite, String creneauxJour,
            String creneauxHeure, String dateAbsence, String loginParent,
            String prenomEnfant) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("INSERT INTO Absences (nomActivite, creneauxJour, creneauxHeure, dateAbsence, loginParent, nomEnfant) VALUES (?, ?, ?, ?, ?, ?)");) {
            st.setString(1, nomActivite);
            st.setString(2, creneauxJour);
            st.setString(3, creneauxHeure);
            st.setString(4, dateAbsence);
            st.setString(5, loginParent);
            st.setString(6, prenomEnfant);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Verifie qu'il n'existe pas déjà l'absence mis en paramètre
     *
     * @param nomActivite
     * @param creneauxJour
     * @param creneauxHeure
     * @param dateAbsence
     * @param loginParent
     * @param prenomEnfant
     * @return true si l'absence existe déjà, false sinon
     */
    public boolean verificationAbsence(String nomActivite, String creneauxJour,
            String creneauxHeure, String dateAbsence, String loginParent,
            String prenomEnfant) throws DAOException {
        boolean test = false;
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM Absences WHERE nomActivite='" + nomActivite + "' AND creneauxJour='" + creneauxJour + "' "
                    + "AND creneauxHeure='" + creneauxHeure + "' AND dateAbsence='" + dateAbsence + "' AND loginParent='" + loginParent + "'"
                    + " AND nomEnfant='" + prenomEnfant + "'");
            if (rs.next()) {
                test = true;
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return test;
    }

    /**
     * Permet la suppression de toutes les absences
     */
    public void finPeriode() throws DAOException {
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            st.executeQuery("DELETE FROM Absences");
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet de recuperer le nombre d'absence d'un enfant à une activité
     *
     * @param nomActivite
     * @param creneauxJour
     * @param creneauxHeure
     * @param loginParent
     * @param prenomEnfant
     * @return entier représentant le nombre d'absences
     */
    public int getAbsences(String nomActivite, String creneauxJour,
            String creneauxHeure, String loginParent, String prenomEnfant)
            throws DAOException {
        int result = 0;
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM Absences WHERE nomActivite='" + nomActivite.trim() + "' AND creneauxJour='" + creneauxJour.trim() + "' "
                    + "AND creneauxHeure='" + creneauxHeure.trim() + "' AND loginParent='" + loginParent.trim() + "'"
                    + " AND nomEnfant='" + prenomEnfant.trim() + "'");
            while (rs.next()) {
                result++;
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }
}
