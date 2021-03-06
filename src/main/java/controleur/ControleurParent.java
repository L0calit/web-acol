package controleur;

import dao.AbsenceDAO;
import dao.ActiviteDAO;
import dao.DAOException;
import dao.FactureDAO;
import dao.ParentDAO;
import dao.PeriodeDAO;
import dao.RegimeDAO;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import modele.Activite;
import modele.Cantine;
import modele.Facture;
import modele.FicheEnfant;
import modele.FicheParent;
import modele.Garderie;
import modele.Periode;

/**
 *
 * @author bernnico
 */
/**
 * Le contrôleur de l'application.
 */
@WebServlet(name = "ControleurParent", urlPatterns = {"/controleurParent"})

public class ControleurParent extends HttpServlet {

    @Resource(name = "jdbc/bibliography")
    private DataSource ds;

    /**
     * Permet de gérer le cas si les paramètres donnés ne sont pas correctes
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void invalidParameters(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/controleurErreur.jsp").forward(request, response);
    }

    /**
     * Permet de gérer le cas où il y a eu une erreur avec la base de données
     *
     * @param request
     * @param response
     * @param e
     * @throws ServletException
     * @throws IOException
     */
    private void erreurBD(HttpServletRequest request,
            HttpServletResponse response, DAOException e)
            throws ServletException, IOException {
        e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
        request.setAttribute("erreurMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }

    /**
     * Actions possibles en POST : Connexion au compte parent Modification des
     * informations parents Creation d'un compte parent Creation d'une fiche
     * parent
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        ParentDAO parentDAO = new ParentDAO(ds);
        try {
            if (action == null) {
                invalidParameters(request, response);
            } else if (action.equals("connexion")) {
                testMiseAJourBDD();
                actionConnexion(request, response, parentDAO);
            } else if (action.equals("modifParent")) {
                actionModifierParent(request, response, parentDAO);
            } else if (action.equals("creationCompteParent")) {
                actionCreationCompteParent(request, response, parentDAO);
            } else if (action.equals("creationFiche")) {
                actionCreationFiche(request, response, parentDAO);
            } else if (action.equals("modifInfo")) {
                actionModifInfo(request, response, parentDAO);
            }
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
    }

    /**
     * Permet le controle de la base de donnée avant chaque connexion pour
     * savoir si on vient de finir une periode ou si on a une periode en cours
     */
    public void testMiseAJourBDD() throws DAOException {
        PeriodeDAO periodeDAO = new PeriodeDAO(ds);
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        AbsenceDAO absenceDAO = new AbsenceDAO(ds);
        List<Periode> periodes = periodeDAO.getPeriodes();
        for (Periode periode : periodes) {
            if (periode.estFini()) {
                // On calcule toutes les factures
                calculeFacture(periode);

                // On supprime toutes les activites de cette periode
                // On supprime toutes les reservations de cette periode
                activiteDAO.finPeriode(periode);

                // On supprime la periode
                periodeDAO.supprimerPeriode(periode.debutToString(), periode.finToString());

                // On supprime toutes les absences
                absenceDAO.finPeriode();
            } else if (periode.estEnCours()) {
                List<Activite> activites = activiteDAO.getListeActivite(periode);
                for (Activite activite : activites) {
                    int effectifMax = activite.getEffectif();
                    activiteDAO.miseAJour(effectifMax, activite.getNom(), activite.getCreneauxJour(), activite.getCreneauxHeure());
                }
            }

        }
    }

    /**
     * Permet le calcul de la facture pour tous les parents sur une periode
     * donné
     *
     * @param periode
     */
    public void calculeFacture(Periode periode) throws DAOException {
        ParentDAO parentDAO = new ParentDAO(ds);
        FactureDAO factureDAO = new FactureDAO(ds);
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        AbsenceDAO absenceDAO = new AbsenceDAO(ds);
        List<String> listeParent = parentDAO.getParents();
        for (String loginParent : listeParent) {
            FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
            int prixTotal = 0;
            int montantEnlever = 0;
            for (FicheEnfant ficheEnfant : ficheParent.getEnfants()) {
                List<Activite> activites = activiteDAO.getReserver(ficheEnfant, loginParent, periode);
                for (Activite activite : activites) {
                    prixTotal += activite.getPrix();
                    montantEnlever += activite.getPrixIndiv() * absenceDAO.getAbsences(activite.getNom(),
                            activite.getCreneauxJour(), activite.getCreneauxHeure(),
                            loginParent, ficheEnfant.getPrenom());
                }
                List<Garderie> garderies = activiteDAO.getGarderie(ficheEnfant.getPrenom(), loginParent, periode);
                for (Garderie garderie : garderies) {
                    prixTotal += garderie.getPrix();
                }
                prixTotal += ficheEnfant.getCantine().getPrix();
            }
            int montantFinal = prixTotal - montantEnlever;
            factureDAO.ajoutFacture(loginParent, periode.debutToString(), periode.finToString(), prixTotal, montantEnlever, montantFinal);
        }
    }

    /**
     * Permet l'actualisation de la page parent
     *
     * @param request
     * @param response
     * @param loginParent
     * @throws IOException
     * @throws ServletException
     */
    public void actualiserPageParent(HttpServletRequest request, HttpServletResponse response, String loginParent)
            throws IOException, ServletException, DAOException {
        ParentDAO parentDAO = new ParentDAO(ds);
        FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
        request.setAttribute("parent", ficheParent);
        PeriodeDAO periodeDAO = new PeriodeDAO(ds);
        List<Periode> periodes = periodeDAO.getPeriodes();
        request.setAttribute("estEnCours", periodeEncours(periodes));
        FactureDAO factureDAO = new FactureDAO(ds);
        List<Facture> factures = factureDAO.getAllFacture(loginParent);
        request.setAttribute("factures", factures);
        request.getRequestDispatcher("/WEB-INF/parent.jsp").forward(request, response);
    }

    /**
     * Modifie les informations du parent dans la base de données
     *
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException
     */
    public void actionModifInfo(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException, DAOException {
        String newAdresse = request.getParameter("adresse");
        String newNom = request.getParameter("nom");
        String newPrenom = request.getParameter("prenom");
        String loginParent = request.getParameter("login");
        parentDAO.modifierInfo(newAdresse, newNom, newPrenom, loginParent);
        request.setAttribute("modifOK", 1);
        actualiserPageParent(request, response, loginParent);
    }

    /**
     * Crée une fiche parent dans la base de données
     *
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException
     */
    public void actionCreationFiche(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException, DAOException {
        String adresse = request.getParameter("adresse");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String login = request.getParameter("login");
        String password = request.getParameter("motdepasse");
        parentDAO.creation(login, password, nom, prenom, adresse);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    /**
     * Verifie que le compte parent n'existe pas et en plus que le password est
     * bien rentré Puis on affiche la page d'ajout des informations
     *
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException
     */
    public void actionCreationCompteParent(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException, DAOException {
        if (!request.getParameter("password1").equals(request.getParameter("password2"))) {
            request.setAttribute("differentPassword", "1");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else if (parentDAO.verifyLogin(request.getParameter("login"))) {
            request.setAttribute("loginUsed", "1");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        request.setAttribute("login", request.getParameter("login"));
        request.setAttribute("password", request.getParameter("password1"));
        request.getRequestDispatcher("/WEB-INF/ajoutParent.jsp").forward(request, response);
    }

    /**
     * Affiche la page de modification des informations
     *
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException
     */
    public void actionModifierParent(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException, DAOException {
        FicheParent ancienneFicheParent = parentDAO.getFicheParent(request.getParameter("currentLogin"));
        request.setAttribute("ficheParent", ancienneFicheParent);
        request.getRequestDispatcher("/WEB-INF/modifParent.jsp").forward(request, response);
    }

    /**
     * Permet la connexion d'un utilisateur
     *
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException
     */
    public void actionConnexion(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException, DAOException {
        String login = request.getParameter("login");
        String mdp = request.getParameter("password");
        if (parentDAO.verify(login, mdp)) {
            actualiserPageParent(request, response, login);
        } else {
            request.setAttribute("erreurLoginParent", "1");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    /**
     * Permet de savoir si une des periodes données est en cours ou non
     *
     * @param periodes
     * @return true si il y a une periode en cours, false sinon
     */
    public boolean periodeEncours(List<Periode> periodes) {
        for (Periode periode : periodes) {
            if (periode.estEnCours()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Actions possible en GET : 
     * Ajouter une enfant à une fiche parent 
     * Supprimer un enfant d'une fiche parent
     * Récupérer les informations d'un enfant
     * Se déconnecter 
     * Retourner sur la page parent lorsque l'on est sur la page enfant 
     * Ajouter une activité à une fiche enfant 
     * Ajouter une activité garderie à une fiche enfant 
     * Supprimer une activité à une fiche enfant
     * Supprimer une activité garderie à une fiche enfant 
     * Annoncer son absence à une activité 
     * Voir une facture
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            if (action == null) {
                invalidParameters(request, response);
            } else if (action.equals("ajoutEnfant")) {
                actionAjoutEnfant(request, response);
            } else if (action.equals("enfantAjouter")) {
                actionAjouterEnfant(request, response);
            } else if (action.equals("enfantSupprimer")) {
                actionSupprimerEnfant(request, response);
            } else if (action.equals("enfantInfo")) {
                String loginParent = request.getParameter("loginParent");
                String prenom = request.getParameter("enfant");
                actualiserPageEnfant(request, response, loginParent, prenom);
            } else if (action.equals("logout")) {
                request.logout();
                response.sendRedirect("index.jsp");
            } else if (action.equals("retourParent")) {
                String login = request.getParameter("loginParent");
                actualiserPageParent(request, response, login);
            } else if (action.equals("ajoutActivite")) {
                actionAjoutActivite(request, response);
            } else if (action.equals("activiteAjouter")) {
                actionActiviteAjouter(request, response);
            } else if (action.equals("ajoutGarderie")) {
                actionAjoutGarderie(request, response);
            } else if (action.equals("activiteSupprimer")) {
                actionSupprimerActivite(request, response);
            } else if (action.equals("activiteAbsent")) {
                actionActiviteAbsent(request, response);
            } else if (action.equals("voirFacture")) {
                actionVueFacture(request, response);
            } else if (action.equals("garderieSupprimer")) {
                actionGarderieSupprimer(request, response);
            }
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }

    }

    /**
     * Permet la suppression d'une activité garderie à une fiche enfant
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionGarderieSupprimer(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        String loginParent = request.getParameter("loginParent");
        String prenom = request.getParameter("prenomEnfant");
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        String creneauxJour = request.getParameter("creneauxJour");
        String creneauxHeure = request.getParameter("creneauxHeure");
        activiteDAO.supprimerActivite(prenom, loginParent, "Garderie", creneauxJour, creneauxHeure);
        actualiserPageEnfant(request, response, loginParent, prenom);
    }

    /**
     * Permet d'acceder à la facture sélectionnée
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionVueFacture(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        String dateDebut = request.getParameter("factureDateDebut");
        String dateFin = request.getParameter("factureDateFin");
        String loginParent = request.getParameter("loginParent");
        ParentDAO parentDAO = new ParentDAO(ds);
        FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
        String nomParent = ficheParent.getNom();
        String prenomParent = ficheParent.getPrenom();
        FactureDAO factureDAO = new FactureDAO(ds);
        Facture facture = factureDAO.getFacture(loginParent, dateDebut, dateFin);
        request.setAttribute("facture", facture);
        request.setAttribute("nomParent", nomParent);
        request.setAttribute("prenomParent", prenomParent);
        request.getRequestDispatcher("WEB-INF/vueFacture.jsp").forward(request, response);
    }

    /**
     * Permet d'annoncer son absence au prochain créneaux d'une activité Si on
     * annonce moins de 48h en avance, cela ne fonctionne pas Si on a déjà
     * annoncer son absence pour le prochain créneaux, on ne peut annoncer son
     * absence pour celui d'après
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionActiviteAbsent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        AbsenceDAO absenceDAO = new AbsenceDAO(ds);
        String nomActivite = request.getParameter("nomActivite");
        String prenomEnfant = request.getParameter("prenomEnfant");
        String loginParent = request.getParameter("loginParent");
        String creneauxJour = request.getParameter("creneauxJour");
        String creneauxHeure = request.getParameter("creneauxHeure");
        Periode periode = new Periode();
        String dateAbsence = periode.getProchainCreneaux(creneauxJour, creneauxHeure);
        if (dateAbsence.equals("")) {
            request.setAttribute("delai48h", true);
        } else if (!absenceDAO.verificationAbsence(nomActivite, creneauxJour, creneauxHeure, dateAbsence, loginParent, prenomEnfant)) {
            absenceDAO.ajoutAbsence(nomActivite, creneauxJour, creneauxHeure, dateAbsence, loginParent, prenomEnfant);
            request.setAttribute("annule", true);
        } else {
            request.setAttribute("dejaAnnule", true);
        }
        actualiserPageEnfant(request, response, loginParent, prenomEnfant);
    }

    /**
     * Permet la suppression d'une activité choisi par l'utilisateur
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionSupprimerActivite(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // On parse la value choisi pour les elements importants
        String nomActivite = request.getParameter("nomActivite");
        String prenomEnfant = request.getParameter("prenomEnfant");
        String loginParent = request.getParameter("loginParent");
        String creneauxJour = request.getParameter("creneauxJour");
        String creneauxHeure = request.getParameter("creneauxHeure");
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        activiteDAO.supprimerActivite(prenomEnfant, loginParent, nomActivite, creneauxJour, creneauxHeure);
        actualiserPageEnfant(request, response, loginParent, prenomEnfant);
    }

    /**
     * Permet l'ajout d'activité garderie pour toutes les horaires réferencées
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionAjoutGarderie(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        String loginParent = request.getParameter("loginParent");
        String prenomEnfant = request.getParameter("prenomEnfant");
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        PeriodeDAO periodeDAO = new PeriodeDAO(ds);
        List<Periode> periodes = periodeDAO.getPeriodes();
        Periode periode = getPeriodeSuivante(periodes);
        List<Garderie> garderies = activiteDAO.getGarderie(prenomEnfant, loginParent);
        if (request.getParameter("Lu7") != null && !testDejaReserve(garderies, "lundi", "7h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "lundi", "7h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Ma7") != null && !testDejaReserve(garderies, "mardi", "7h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "mardi", "7h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Je7") != null && !testDejaReserve(garderies, "jeudi", "7h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "jeudi", "7h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Ve7") != null && !testDejaReserve(garderies, "vendredi", "7h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "vendredi", "7h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Lu5") != null && !testDejaReserve(garderies, "lundi", "15h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "lundi", "15h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Ma5") != null && !testDejaReserve(garderies, "mardi", "15h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "mardi", "15h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Je5") != null && !testDejaReserve(garderies, "jeudi", "15h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "jeudi", "15h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Ve5") != null && !testDejaReserve(garderies, "vendredi", "15h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "vendredi", "15h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Lu6") != null && !testDejaReserve(garderies, "lundi", "16h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "lundi", "16h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Ma6") != null && !testDejaReserve(garderies, "mardi", "16h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "mardi", "16h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Je6") != null && !testDejaReserve(garderies, "jeudi", "16h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "jeudi", "16h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Ve6") != null && !testDejaReserve(garderies, "vendredi", "16h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "vendredi", "16h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Lu17") != null && !testDejaReserve(garderies, "lundi", "17h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "lundi", "17h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Ma17") != null && !testDejaReserve(garderies, "mardi", "17h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "mardi", "17h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Je17") != null && !testDejaReserve(garderies, "jeudi", "17h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "jeudi", "17h", periode.debutToString(), periode.finToString());
        }
        if (request.getParameter("Ve17") != null && !testDejaReserve(garderies, "vendredi", "17h")) {
            activiteDAO.reserverActivite(prenomEnfant, loginParent, "Garderie",
                    "vendredi", "17h", periode.debutToString(), periode.finToString());
        }
        actualiserPageEnfant(request, response, loginParent, prenomEnfant);
    }

    /**
     * Permet l'ajout d'une activité choisie par l'utilisateur
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionActiviteAjouter(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // On parse la value choisi pour les elements importants
        String activite = request.getParameter("activiteChoisi");
        String prenomEnfant = request.getParameter("prenomEnfant");
        String loginParent = request.getParameter("loginParent");
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        String nomActivite = activite.split("le")[0];
        String creneauxJour = activite.split("le")[1].split("à")[0];
        String creneauxHeure = activite.split("le")[1].split("à")[1];
        Periode periode = activiteDAO.getPeriode(nomActivite, creneauxJour, creneauxHeure);
        activiteDAO.reserverActivite(prenomEnfant, loginParent, nomActivite,
                creneauxJour, creneauxHeure, periode.debutToString(), periode.finToString());
        actualiserPageEnfant(request, response, loginParent, prenomEnfant);
    }

    /**
     * Permet d'acceder à la page permettant l'ajout d'activité
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionAjoutActivite(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        // On souhaite recuperer toute la liste des activites disponible pour cette enfant
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String loginParent = request.getParameter("loginParent");
        ParentDAO parentDAO = new ParentDAO(ds);
        FicheEnfant ficheEnfant = parentDAO.getFicheEnfant(loginParent, nom, prenom);
        List<Activite> listActivite = activiteDAO.getListeActivite(ficheEnfant, loginParent);
        request.setAttribute("activites", listActivite);
        request.setAttribute("prenomEnfant", prenom);
        request.setAttribute("loginParent", loginParent);
        request.getRequestDispatcher("WEB-INF/ajoutActivite.jsp").forward(request, response);
    }

    /**
     * Permet la suppression d'une fiche enfant
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionSupprimerEnfant(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        String loginParent = request.getParameter("loginParent");
        String prenom = request.getParameter("enfant");
        ParentDAO parentDAO = new ParentDAO(ds);
        parentDAO.supprimerEnfant(loginParent, prenom);
        actualiserPageParent(request, response, loginParent);
    }

    /**
     * Permet l'ajout d'une fiche enfant
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionAjouterEnfant(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        ParentDAO parentDAO = new ParentDAO(ds);
        String loginParent = request.getParameter("loginParent");
        String nomEnfant = request.getParameter("nom");
        String prenomEnfant = request.getParameter("prenom");
        String sexe = request.getParameter("sexe");
        String dateNaissance = request.getParameter("dateNaissance");
        String classe = request.getParameter("classe");
        String regime = request.getParameter("regimeChoisi");
        String cantine = "";
        if (request.getParameter("Lu") != null) {
            cantine += "lu/";
        }
        if (request.getParameter("Ma") != null) {
            cantine += "ma/";
        }
        if (request.getParameter("Me") != null) {
            cantine += "me/";
        }
        if (request.getParameter("Je") != null) {
            cantine += "je/";
        }
        if (request.getParameter("Ve") != null) {
            cantine += "ve/";
        }
        if (cantine.equals("")) {
            cantine = "0";
        }
        String divers = request.getParameter("divers");
        Cantine cantineElement = new Cantine(cantine);
        FicheParent parents = parentDAO.getFicheParent(loginParent);
        FicheEnfant ficheEnfant = new FicheEnfant(sexe, classe, dateNaissance,
                divers, regime, parents, cantineElement, null, nomEnfant, prenomEnfant);
        parentDAO.ajoutEnfant(ficheEnfant, loginParent);
        actualiserPageParent(request, response, loginParent);
    }

    /**
     * Permet d'acceder à la page d'ajout de fiche enfant
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void actionAjoutEnfant(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DAOException {
        RegimeDAO regimeDAO = new RegimeDAO(ds);
        List<String> regimes = regimeDAO.getListeRegime();
        request.setAttribute("regimes", regimes);
        String login = request.getParameter("loginParent");
        request.setAttribute("loginParent", login);
        request.getRequestDispatcher("/WEB-INF/ajoutEnfant.jsp").forward(request, response);
    }

    /**
     * Permet de verifier si un creneau a déjà été reservé ou non
     *
     * @param garderies
     * @param creneauxJour
     * @param creneauxHeure
     * @return true si le creneau a été réservé, false sinon
     */
    public boolean testDejaReserve(List<Garderie> garderies, String creneauxJour, String creneauxHeure) {
        boolean test = false;
        for (Garderie garderie : garderies) {
            if (garderie.getCreneauxJour().trim().equals(creneauxJour.trim())) {
                if (garderie.getCreneauxHeure().trim().equals(creneauxHeure.trim())) {
                    test = true;
                }
            }
        }
        return test;
    }

    /**
     * Permet l'actualisation de la page enfant
     *
     * @param request
     * @param response
     * @param loginParent
     * @param prenomEnfant
     * @throws IOException
     * @throws ServletException
     */
    public void actualiserPageEnfant(HttpServletRequest request, HttpServletResponse response,
            String loginParent, String prenomEnfant) throws IOException, ServletException, DAOException {
        ParentDAO parentDAO = new ParentDAO(ds);
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
        for (FicheEnfant enfant : ficheParent.getEnfants()) {
            if (enfant.getPrenom().trim().equals(prenomEnfant.trim())) {
                request.setAttribute("ficheEnfant", enfant);
                List<Activite> activites = activiteDAO.getReserver(enfant, loginParent);
                List<Garderie> garderies = activiteDAO.getGarderie(enfant.getPrenom(), loginParent);
                request.setAttribute("garderies", garderies);
                request.setAttribute("activites", activites);
            }
        }
        request.setAttribute("loginParent", loginParent);
        PeriodeDAO periodeDAO = new PeriodeDAO(ds);
        List<Periode> periodes = periodeDAO.getPeriodes();
        request.setAttribute("estEnCours", periodeEncours(periodes));
        request.getRequestDispatcher("WEB-INF/enfant.jsp").forward(request, response);
    }

    /**
     * Permet de recuperer la période suivante parmi toutes les periodes
     * disponible
     *
     * @param periodes
     * @return retourne la période suivante
     */
    public Periode getPeriodeSuivante(List<Periode> periodes) {
        Periode periodeSuivante = periodes.get(0);
        GregorianCalendar dateActuelle = new Periode().getDateActuelle();
        for (Periode periode : periodes) {
            if (periode.getDateDebut().after(dateActuelle) && periode.getDateDebut().before(periodeSuivante.getDateDebut())) {
                periodeSuivante = periode;
            }
        }
        return periodeSuivante;
    }
}
