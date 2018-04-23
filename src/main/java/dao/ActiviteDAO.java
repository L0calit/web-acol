package dao;

import modele.Activite;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import modele.FicheEnfant;
import modele.Garderie;
import modele.Periode;

/**
 *
 * @author boussanl
 */
public class ActiviteDAO extends AbstractDataBaseDAO {

    public ActiviteDAO(DataSource ds) {
        super(ds);
    }

    /**
     * Renvoie la liste des activites de la table activités sans restriction
     *
     * @return liste d'objet Activité
     */
    public List<Activite> getListeActivite() throws DAOException {
        List<Activite> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM Activites ORDER BY nom");
            while (rs.next()) {
                String nom = rs.getString("nom");
                String classe = rs.getString("classe");
                List<String> listeClasse = new ArrayList<>();
                if (classe.contains("/")) {
                    String[] classeDiviser = classe.split("/");
                    listeClasse = Arrays.asList(classeDiviser);
                } else {
                    listeClasse.add(classe);
                }
                String creneauxJour = rs.getString("creneauxJour");
                String creneauxHeure = rs.getString("creneauxHeure");
                Periode periode = new Periode(rs.getString("dateDebut"), rs.getString("dateFin"));
                result.add(new Activite(nom, creneauxJour, creneauxHeure, listeClasse, rs.getInt("prix"),
                        rs.getInt("effectif"), rs.getString("mailaccompagnateur1"), rs.getString("mailaccompagnateur2"), periode));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Renvoie la liste des activités sur une période donnée
     *
     * @param periode
     * @return Liste d'objet Activite
     */
    public List<Activite> getListeActivite(Periode periode) throws DAOException {
        List<Activite> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM Activites WHERE dateDebut='" + periode.debutToString() + "' AND dateFin='" + periode.finToString() + "'");
            while (rs.next()) {
                String nom = rs.getString("nom");
                String classe = rs.getString("classe");
                List<String> listeClasse = new ArrayList<>();
                if (classe.contains("/")) {
                    String[] classeDiviser = classe.split("/");
                    listeClasse = Arrays.asList(classeDiviser);
                } else {
                    listeClasse.add(classe);
                }
                String creneauxJour = rs.getString("creneauxJour");
                String creneauxHeure = rs.getString("creneauxHeure");
                result.add(new Activite(nom, creneauxJour, creneauxHeure, listeClasse, rs.getInt("prix"),
                        rs.getInt("effectif"), rs.getString("mailaccompagnateur1"), rs.getString("mailaccompagnateur2"), periode));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Retourne la liste des activités garderie réservé par un enfant en
     * particulier
     *
     * @param prenomEnfant
     * @param loginParent
     * @return liste d'objet Garderie
     */
    public List<Garderie> getGarderie(String prenomEnfant, String loginParent)
            throws DAOException {
        List<Garderie> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM ActiviteReserve WHERE prenomEnfant='" + prenomEnfant + "' AND loginParent='" + loginParent + "' AND nomActivite='Garderie'");
            while (rs.next()) {
                String creneauxJour = rs.getString("creneauxJour");
                String creneauxHeure = rs.getString("creneauxHeure");
                String dateDebut = rs.getString("dateDebut");
                String dateFin = rs.getString("dateFin");
                result.add(new Garderie(creneauxJour, creneauxHeure, dateDebut, dateFin));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Retourne la liste des activités garderie réservé par un enfant en
     * particulier et pour une période donnée
     *
     * @param prenomEnfant
     * @param loginParent
     * @param periode
     * @return liste d'objet Garderie
     */
    public List<Garderie> getGarderie(String prenomEnfant, String loginParent,
            Periode periode) throws DAOException {
        List<Garderie> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM ActiviteReserve WHERE prenomEnfant='" + prenomEnfant + "' AND loginParent='" + loginParent + "' AND nomActivite='Garderie'"
                    + " AND dateDebut='" + periode.debutToString() + "' AND dateFin='" + periode.finToString() + "'");
            while (rs.next()) {
                String creneauxJour = rs.getString("creneauxJour");
                String creneauxHeure = rs.getString("creneauxHeure");
                String dateDebut = rs.getString("dateDebut");
                String dateFin = rs.getString("dateFin");
                result.add(new Garderie(creneauxJour, creneauxHeure, dateDebut, dateFin));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Renvoie la liste des activités disponible à la reservation
     *
     * @param ficheEnfant
     * @param loginParent
     * @return liste d'objet Activité
     */
    public List<Activite> getListeActivite(FicheEnfant ficheEnfant, String loginParent)
            throws DAOException {
        List<Activite> listActivite = this.getListeActivite();
        List<Activite> reserved = this.getReserver(ficheEnfant, loginParent);
        List<Activite> result = new ArrayList<>();
        // On enleve toutes les activites qui ont déjà été reservé par l'enfant
        listActivite.removeAll(reserved);
        for (Activite activite : listActivite) {
            // Verifie que l'enfant est bien dans une classe autorisé pour cette activité
            // Verifie que l'enfant n'a pas déjà selectionner une activité lors de ce créneaux
            if (activite.testClasse(ficheEnfant.getClasse()) && activite.testCreneaux(reserved)) {
                result.add(activite);
            }
        }
        return result;
    }

    /**
     * Renvoie la période associé à une certaine activité entrée par
     * l'utilisateur
     *
     * @param nomActivite
     * @param creneauxJour
     * @param creneauxHeure
     * @return Objet période qui correspond à une activité
     */
    public Periode getPeriode(String nomActivite, String creneauxJour,
            String creneauxHeure) throws DAOException {
        Periode result = null;
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM activites WHERE nom='" + nomActivite.trim() + "' and creneauxJour='" + creneauxJour.trim() + "' and creneauxHeure='" + creneauxHeure.trim() + "'");
            if (rs.next()) {
                String dateDebut = rs.getString("dateDebut");
                String dateFin = rs.getString("dateFin");
                result = new Periode(dateDebut, dateFin);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Permet de retirer de manière aléatoire toutes les reservations en trop
     * pour une activité en fonction de l'effectif maximal de l'activité
     *
     * @param effectif
     * @param nomActivite
     * @param creneauxJour
     * @param creneauxHeure
     */
    public void miseAJour(int effectif, String nomActivite, String creneauxJour,
            String creneauxHeure) throws DAOException {
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            int resultat = 0;
            ResultSet rs = st.executeQuery("SELECT COUNT(nomActivite) AS total FROM "
                    + "activiteReserve WHERE nomActivite='" + nomActivite + "' and"
                    + " creneauxJour='" + creneauxJour + "' and creneauxHeure='" + creneauxHeure + "'");
            if (rs.next()) {
                resultat = rs.getInt("total");
            }
            if (resultat >= effectif) {
                st.executeQuery("DELETE TOP(" + (resultat - effectif) + ") FROM"
                        + " activiteReserve WHERE nomActivite='" + nomActivite + "'"
                        + " and creneauxJour='" + creneauxJour + "' and creneauxHeure='" + creneauxHeure + "'");
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Recupère la liste des activités reservé par un certain enfant choisi par
     * l'utilisateur
     *
     * @param ficheEnfant
     * @param loginParent
     * @return
     */
    public List<Activite> getReserver(FicheEnfant ficheEnfant, String loginParent)
            throws DAOException {
        List<Activite> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM ActiviteReserve AR JOIN Activites A ON "
                    + "nomActivite=nom AND AR.creneauxJour=A.creneauxJour "
                    + "AND AR.creneauxHeure=A.creneauxHeure WHERE AR.prenomEnfant='"
                    + ficheEnfant.getPrenom() + "' AND AR.loginParent='"
                    + loginParent + "'");
            while (rs.next()) {
                String nom = rs.getString("nom");
                String classe = rs.getString("classe");
                List<String> listeClasse = new ArrayList<>();
                if (classe.contains("/")) {
                    String[] classeDiviser = classe.split("/");
                    listeClasse = Arrays.asList(classeDiviser);
                } else {
                    listeClasse.add(classe);
                }
                String creneauxJour = rs.getString("creneauxJour");
                String creneauxHeure = rs.getString("creneauxHeure");
                Periode periode = new Periode(rs.getString("dateDebut"), rs.getString("dateFin"));
                result.add(new Activite(nom, creneauxJour, creneauxHeure, listeClasse, rs.getInt("prix"),
                        rs.getInt("effectif"), rs.getString("mailaccompagnateur1"), rs.getString("mailaccompagnateur2"), periode));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Recupère la liste des activités reservé par un certain enfant choisi par
     * l'utilisateur et pour une période donné
     *
     * @param ficheEnfant
     * @param loginParent
     * @param periode
     * @return
     * @throws DAOException
     */
    public List<Activite> getReserver(FicheEnfant ficheEnfant, String loginParent,
            Periode periode) throws DAOException {
        List<Activite> result = new ArrayList<>();
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM ActiviteReserve AR JOIN Activites A ON "
                    + "nomActivite=nom AND AR.creneauxJour=A.creneauxJour "
                    + "AND AR.creneauxHeure=A.creneauxHeure WHERE AR.prenomEnfant='"
                    + ficheEnfant.getPrenom() + "' AND AR.loginParent='"
                    + loginParent + "' AND AR.dateDebut='" + periode.debutToString().trim()
                    + "' AND AR.dateFin='" + periode.finToString().trim() + "'");
            while (rs.next()) {
                String nom = rs.getString("nom");
                String classe = rs.getString("classe");
                List<String> listeClasse = new ArrayList<>();
                if (classe.contains("/")) {
                    String[] classeDiviser = classe.split("/");
                    listeClasse = Arrays.asList(classeDiviser);
                } else {
                    listeClasse.add(classe);
                }
                String creneauxJour = rs.getString("creneauxJour");
                String creneauxHeure = rs.getString("creneauxHeure");
                result.add(new Activite(nom, creneauxJour, creneauxHeure, listeClasse, rs.getInt("prix"),
                        rs.getInt("effectif"), rs.getString("mailaccompagnateur1"),
                        rs.getString("mailaccompagnateur2"), periode));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Permet l'ajout d'une activité à la base de données
     *
     * @param nom
     * @param creneauxJour
     * @param creneauxHeure
     * @param classe
     * @param prix
     * @param effectif
     * @param mail1
     * @param mail2
     * @param dateDebut
     * @param dateFin
     * @throws DAOException
     */
    public void ajouterActivite(String nom, String creneauxJour, String creneauxHeure,
            String classe, int prix, int effectif, String mail1, String mail2,
            String dateDebut, String dateFin) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("INSERT INTO activites"
                        + " (nom, creneauxJour, creneauxHeure, classe, prix, effectif,"
                        + " mailAccompagnateur1, mailAccompagnateur2, dateDebut, dateFin)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");) {
            st.setString(1, nom);
            st.setString(2, creneauxJour);
            st.setString(3, creneauxHeure);
            st.setString(4, classe);
            st.setInt(5, prix);
            st.setInt(6, effectif);
            st.setString(7, mail1);
            st.setString(8, mail2);
            st.setString(9, dateDebut);
            st.setString(10, dateFin);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet la suppresion d'une activité dans la base de données
     *
     * @param nom
     * @param creneauxJour
     * @param creneauxHeure
     */
    public void supprimerActivite(String nom, String creneauxJour,
            String creneauxHeure) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("DELETE FROM activites"
                        + " WHERE  nom='" + nom + "' and creneauxJour='" + 
                        creneauxJour + "' and creneauxHeure='" + creneauxHeure 
                        + "'");) {
            st.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Verifie si l'activité entrée en paramètre n'existe pas déjà
     *
     * @param nom
     * @param jour
     * @param heure
     * @return true si l'activité existe, false sinon
     * @throws DAOException
     */
    public boolean existeDeja(String nom, String jour, String heure)
            throws DAOException {
        try (
                Connection conn = getConn();
                Statement st = conn.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM activites WHERE "
                    + "nom='" + nom + "' and creneauxJour='" + jour + "' and"
                    + " creneauxHeure='" + heure + "'");
            return rs.next();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet l'ajout d'une entrée reservation dans la base de données
     * activitereserve
     *
     * @param prenomEnfant
     * @param loginParent
     * @param nomActivite
     * @param creneauxJour
     * @param creneauxHeure
     * @param dateDebut
     * @param dateFin
     * @throws DAOException
     */
    public void reserverActivite(String prenomEnfant, String loginParent,
            String nomActivite, String creneauxJour, String creneauxHeure,
            String dateDebut, String dateFin) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("INSERT INTO "
                        + "activitereserve (prenomEnfant, loginParent, "
                        + "nomActivite, creneauxJour, creneauxHeure, dateDebut, "
                        + "dateFin) VALUES (?, ?, ?, ?, ?, ?, ?)");) {
            st.setString(1, prenomEnfant.trim());
            st.setString(2, loginParent.trim());
            st.setString(3, nomActivite.trim());
            st.setString(4, creneauxJour.trim());
            st.setString(5, creneauxHeure.trim());
            st.setString(6, dateDebut.trim());
            st.setString(7, dateFin.trim());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet de supprimer toutes les activités concernant la periode donnée et
     * permet de supprimer toutes les reservations de ces activités
     *
     * @param periode
     * @throws DAOException
     */
    public void finPeriode(Periode periode) throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("DELETE FROM "
                        + "activitereserve WHERE dateDebut='" + periode.debutToString().trim()
                        + "' and dateFin='" + periode.finToString().trim() + "'");
                PreparedStatement st2 = conn.prepareStatement("DELETE FROM "
                        + "activites WHERE dateDebut='" + periode.debutToString().trim()
                        + "' and dateFin='" + periode.finToString().trim() + "'");) {
            st.executeUpdate();
            st2.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Permet la suppression d'une activité rentrée en paramètre
     *
     * @param prenomEnfant
     * @param loginParent
     * @param nomActivite
     * @param creneauxJour
     * @param creneauxHeure
     * @throws DAOException
     */
    public void supprimerActivite(String prenomEnfant, String loginParent,
            String nomActivite, String creneauxJour, String creneauxHeure)
            throws DAOException {
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement("DELETE FROM "
                        + "activitereserve WHERE prenomEnfant='" + prenomEnfant.trim()
                        + "' and loginparent='" + loginParent.trim()
                        + "' and nomActivite='" + nomActivite.trim()
                        + "' and creneauxJour='" + creneauxJour.trim()
                        + "' and creneauxHeure='" + creneauxHeure.trim() + "'");) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

}
