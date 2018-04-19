package controleur;

import dao.ActiviteDAO;
import dao.DAOException;
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
import modele.FicheEnfant;
import modele.FicheParent;
import modele.Periode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
     * Actions possibles en POST :
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
                return;
            } else if (action.equals("connexion")) {
                actionConnexion(request, response, parentDAO);
            } else if (action.equals("modifParent")) {
                actionModifierParent(request, response, parentDAO);
            }else if (action.equals("creationCompteParent")) {
                actionCreationCompteParent(request, response, parentDAO);
            }else if (action.equals("creationFiche")) {
                actionCreationFiche(request, response, parentDAO);
            }else if (action.equals("modifInfo")) {
                actionModifInfo(request, response, parentDAO);
            }
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
    }
    
    /**
     * Modifie les informations du parent dans la base de données
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException 
     */
    public void actionModifInfo(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException {
        String newAdresse = request.getParameter("adresse");
        String newNom = request.getParameter("nom");
        String newPrenom = request.getParameter("prenom");
        String loginParent = request.getParameter("login");
        parentDAO.modifierInfo(newAdresse, newNom, newPrenom, loginParent);
        FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
        request.setAttribute("parent", ficheParent);
        request.setAttribute("modifOK", 1);
        PeriodeDAO periodeDAO = new PeriodeDAO(ds);
        List<Periode> periodes = periodeDAO.getPeriodes();
        request.setAttribute("estEnCours", periodeEncours(periodes));
        request.getRequestDispatcher("/WEB-INF/parent.jsp").forward(request, response);
    }
    
    /**
     * Crée une fiche parent dans la base de données
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException 
     */
    public void actionCreationFiche(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException {
        String adresse = request.getParameter("adresse");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String login = request.getParameter("login");
        String password = request.getParameter("motdepasse");
        parentDAO.creation(login, password, nom, prenom, adresse);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    /**
     * Verifie que le compte parent n'existe pas et en plus que le password est bien rentré
     * Puis on affiche la page d'ajout des informations
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException 
     */
    public void actionCreationCompteParent(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException {
        if(!request.getParameter("password1").equals(request.getParameter("password2"))){
            request.setAttribute("differentPassword", "1");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        else if(parentDAO.verifyLogin(request.getParameter("login"))){
            request.setAttribute("loginUsed", "1");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        request.setAttribute("login",request.getParameter("login"));
        request.setAttribute("password",request.getParameter("password1"));
        request.getRequestDispatcher("/WEB-INF/ajoutParent.jsp").forward(request, response);
    }
    
    /**
     * Affiche la page de modification des informations
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException 
     */
    public void actionModifierParent(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException {
        FicheParent ancienneFicheParent = parentDAO.getFicheParent(request.getParameter("currentLogin"));
        request.setAttribute("ficheParent", ancienneFicheParent);
        request.getRequestDispatcher("/WEB-INF/modifParent.jsp").forward(request, response);        
    }
    
    /**
     * Permet la connexion d'un utilisateur
     * @param request
     * @param response
     * @param parentDAO
     * @throws IOException
     * @throws ServletException 
     */
    public void actionConnexion(HttpServletRequest request, HttpServletResponse response,
            ParentDAO parentDAO) throws IOException, ServletException {
        String login = request.getParameter("login");
        String mdp = request.getParameter("password");
        if (parentDAO.verify(login, mdp)) {
            FicheParent ficheParent = parentDAO.getFicheParent(login);
            request.setAttribute("parent", ficheParent);
            PeriodeDAO periodeDAO = new PeriodeDAO(ds);
            List<Periode> periodes = periodeDAO.getPeriodes();
            request.setAttribute("estEnCours", periodeEncours(periodes));
            request.getRequestDispatcher("/WEB-INF/parent.jsp").forward(request, response);
        } else {
            request.setAttribute("erreurLoginParent", "1");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }        
    }
    
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
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException 
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            invalidParameters(request, response);
            return;
        
        } else if (action.equals("ajoutEnfant")) {
            RegimeDAO regimeDAO = new RegimeDAO(ds);
            List<String> regimes = regimeDAO.getListeRegime();
            request.setAttribute("regimes", regimes);
            String login = request.getParameter("loginParent");
            request.setAttribute("loginParent", login);
            request.getRequestDispatcher("/WEB-INF/ajoutEnfant.jsp").forward(request, response);
        
        } else if (action.equals("enfantAjouter")) {
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
            FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
            request.setAttribute("parent", ficheParent);
            PeriodeDAO periodeDAO = new PeriodeDAO(ds);
            List<Periode> periodes = periodeDAO.getPeriodes();
            request.setAttribute("estEnCours", periodeEncours(periodes));
            request.getRequestDispatcher("WEB-INF/parent.jsp").forward(request, response);
        } else if (action.equals("enfantSupprimer")) {
            String loginParent = request.getParameter("loginParent");
            String prenom = request.getParameter("enfant");
            ParentDAO parentDAO = new ParentDAO(ds);
            parentDAO.supprimerEnfant(loginParent, prenom);
            FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
            request.setAttribute("parent", ficheParent);
            PeriodeDAO periodeDAO = new PeriodeDAO(ds);
            List<Periode> periodes = periodeDAO.getPeriodes();
            request.setAttribute("estEnCours", periodeEncours(periodes));
            request.getRequestDispatcher("WEB-INF/parent.jsp").forward(request, response);
        } else if (action.equals("enfantInfo")) {
            String loginParent = request.getParameter("loginParent");
            String prenom = request.getParameter("enfant");
            ParentDAO parentDAO = new ParentDAO(ds);
            FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
            ActiviteDAO activiteDAO = new ActiviteDAO(ds);
            for (FicheEnfant enfant : ficheParent.getEnfants()) {
                if (enfant.getPrenom().equals(prenom)) {
                    request.setAttribute("ficheEnfant", enfant);
                    List<Activite> activites = activiteDAO.getReserver(enfant, loginParent);
                    request.setAttribute("activites", activites);
                }
            }
            request.setAttribute("loginParent", loginParent);
            PeriodeDAO periodeDAO = new PeriodeDAO(ds);
            List<Periode> periodes = periodeDAO.getPeriodes();
            request.setAttribute("estEnCours", periodeEncours(periodes));
            request.getRequestDispatcher("WEB-INF/enfant.jsp").forward(request, response);
        } else if (action.equals("logout")) {
            request.logout();
            response.sendRedirect("index.jsp");
        } else if (action.equals("retourParent")) {
                ParentDAO parentDAO = new ParentDAO(ds);
                String login = request.getParameter("loginParent");
                FicheParent ficheParent = parentDAO.getFicheParent(login);
                request.setAttribute("parent", ficheParent);
                PeriodeDAO periodeDAO = new PeriodeDAO(ds);
                List<Periode> periodes = periodeDAO.getPeriodes();
                request.setAttribute("estEnCours", periodeEncours(periodes));
                request.getRequestDispatcher("/WEB-INF/parent.jsp").forward(request, response);
        }else if (action.equals("ajoutActivite")) {
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
        } else if (action.equals("activiteAjouter")) {
            // On parse la value choisi pour les elements importants
            String activite = request.getParameter("activiteChoisi");
            String prenomEnfant = request.getParameter("prenomEnfant");
            String loginParent = request.getParameter("loginParent");
            ParentDAO parentDAO = new ParentDAO(ds);
            ActiviteDAO activiteDAO = new ActiviteDAO(ds);
            PeriodeDAO periodeDAO = new PeriodeDAO(ds);
            List<Periode> periodes = periodeDAO.getPeriodes();
            FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
            String nomActivite = activite.split("le")[0];
            String creneauxJour = activite.split("le")[1].split("à")[0];
            String creneauxHeure = activite.split("le")[1].split("à")[1];
            Periode periodeSuivante = getPeriodeSuivante(periodes);
            activiteDAO.reserverActivite(prenomEnfant, loginParent, nomActivite, 
                    creneauxJour, creneauxHeure, periodeSuivante.debutToString(), periodeSuivante.finToString());
            for (FicheEnfant enfant : ficheParent.getEnfants()) {
                if (enfant.getPrenom().trim().equals(prenomEnfant.trim())) {
                    request.setAttribute("ficheEnfant", enfant);
                    List<Activite> activites = activiteDAO.getReserver(enfant, loginParent);
                    request.setAttribute("activites", activites);
                }
            }
            request.setAttribute("loginParent", loginParent);
            request.setAttribute("estEnCours", periodeEncours(periodes));
            request.getRequestDispatcher("WEB-INF/enfant.jsp").forward(request, response);
        } else if (action.equals("activiteSupprimer")) {
            // On parse la value choisi pour les elements importants
            String nomActivite = request.getParameter("nomActivite");
            String prenomEnfant = request.getParameter("prenomEnfant");
            String loginParent = request.getParameter("loginParent");
            String creneauxJour = request.getParameter("creneauxJour");
            String creneauxHeure = request.getParameter("creneauxHeure");
            ParentDAO parentDAO = new ParentDAO(ds);
            ActiviteDAO activiteDAO = new ActiviteDAO(ds);
            activiteDAO.supprimerActivite(prenomEnfant, loginParent, nomActivite, creneauxJour, creneauxHeure);
            FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
            for (FicheEnfant enfant : ficheParent.getEnfants()) {
                if (enfant.getPrenom().trim().equals(prenomEnfant.trim())) {
                    request.setAttribute("ficheEnfant", enfant);
                    List<Activite> activites = activiteDAO.getReserver(enfant, loginParent);
                    request.setAttribute("activites", activites);
                }
            }
            request.setAttribute("loginParent", loginParent);
            PeriodeDAO periodeDAO = new PeriodeDAO(ds);
            List<Periode> periodes = periodeDAO.getPeriodes();
            request.setAttribute("estEnCours", periodeEncours(periodes));
            request.getRequestDispatcher("WEB-INF/enfant.jsp").forward(request, response);
        }
    }
    
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
