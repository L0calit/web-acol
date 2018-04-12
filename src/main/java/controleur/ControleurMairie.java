/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import dao.ActiviteDAO;
import dao.DAOException;
import dao.PeriodeDAO;
import dao.EmployeDAO;
import dao.RegimeDAO;
import dao.AccompagnateurDAO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import modele.Activite;
import modele.Periode;
import modele.FicheParent;

/**
 * Le contrôleur de l'application.
 */
@WebServlet(name = "ControleurMairie", urlPatterns = {"/controleurMairie"})
public class ControleurMairie extends HttpServlet {

    @Resource(name = "jdbc/bibliography")
    private DataSource ds;

    /* pages d’erreurs */
    private void invalidParameters(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/controleurErreur.jsp").forward(request, response);        
    }

    private void erreurBD(HttpServletRequest request,
                HttpServletResponse response, DAOException e)
            throws ServletException, IOException {
        e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
        request.setAttribute("erreurMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }
  
    /**
     * Actions possibles en GET : Supprimer Regime, Ajouter Regime
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        RegimeDAO regimeDAO = new RegimeDAO(ds);
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        AccompagnateurDAO accompagnateurDAO = new AccompagnateurDAO(ds);
        PeriodeDAO periodeDAO = new PeriodeDAO(ds);
        String action = request.getParameter("action");
        
        if (action.equals("regimeSupprimer")) {
            regimeDAO.supprimerRegime(request.getParameter("regime"));
            actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            
        } else if (action.equals("regimeAjouter")) {
            boolean requeteValide = true;
            if (regimeDAO.existeDeja(request.getParameter("regime"))) {
                request.setAttribute("SameRegime", "1");
                requeteValide = false;
                actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            }
            if (requeteValide) {
                regimeDAO.ajouterRegime(request.getParameter("regime"));
                actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            }
        
        } else if (action.equals("activiteAjouter")) {
            boolean requeteValide = true;
            if (request.getParameter("mail1").equals(request.getParameter("mail2"))) {
                request.setAttribute("SameAccompagnateur", "1");
                requeteValide = false;
                actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            }
            if (activiteDAO.existeDeja(request.getParameter("nom"), request.getParameter("jour"), request.getParameter("horaire"))) {
                request.setAttribute("SameActivity", "1");
                requeteValide = false;
                actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            }
            if (requeteValide) {
                String classes = "";
                if (request.getParameter("PS") != null) {
                    classes += "PS/";
                }
                if (request.getParameter("MS") != null) {
                    classes += "MS/";
                }
                if (request.getParameter("GS") != null) {
                    classes += "GS/";
                }
                if (request.getParameter("CP") != null) {
                    classes += "CP/";
                }
                if (request.getParameter("CE1") != null) {
                    classes += "CE1/";
                }
                if (request.getParameter("CE2") != null) {
                    classes += "CE2/";
                }
                if (request.getParameter("CM1") != null) {
                    classes += "CM1/";
                }
                if (request.getParameter("CM2") != null) {
                    classes += "CM2/";
                }
                if (classes.equals("")) {
                    classes = "0";
                }
                else {
                    classes = classes.substring(0, classes.length()-1);
                }
                activiteDAO.ajouterActivite(request.getParameter("nom"), request.getParameter("jour"), request.getParameter("horaire"),
                                         classes, Integer.parseInt(request.getParameter("prix")),
                                          Integer.parseInt(request.getParameter("effectif")), request.getParameter("mail1"),
                                          request.getParameter("mail2"));
                actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            }
        
        } else if (action.equals("activiteSupprimer")) {
            activiteDAO.supprimerActivite(request.getParameter("actiNom"), request.getParameter("actiJour"), request.getParameter("actiHeure"));
            actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
          
        } else if (action.equals("periodeSupprimer")) {
            periodeDAO.supprimerPeriode(request.getParameter("dateDebut"), request.getParameter("dateFin"));
            actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            
        } else if (action.equals("periodeAjouter")) {
            periodeDAO.ajouterPeriode(request.getParameter("dateDebut"), request.getParameter("dateFin"));
            actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
        
        } else if (action.equals("logout")) {
            request.logout();
            response.sendRedirect("index.jsp");
        }
    }
    
    /**
     * Actions possibles en POST : connexion
     */
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        RegimeDAO regimeDAO = new RegimeDAO(ds);
        ActiviteDAO activiteDAO = new ActiviteDAO(ds);
        AccompagnateurDAO accompagnateurDAO = new AccompagnateurDAO(ds);
        PeriodeDAO periodeDAO = new PeriodeDAO(ds);
        EmployeDAO employeDAO = new EmployeDAO(ds);
        if (action == null) {
            invalidParameters(request, response);
            return;
            
        } else if (action.equals("connexion")) {
            // tester si mdp et login corrects
            String login = request.getParameter("login");
            String mdp = request.getParameter("password");
            if (employeDAO.verify(login, mdp)) {
                request.setAttribute("login",login);
                actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            } else {
                request.setAttribute("erreurLoginMairie", "1");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        }else if (action.equals("creationCompteMairie")) {
            if(!request.getParameter("password1").equals(request.getParameter("password2"))){
                request.setAttribute("differentPassword", "1");
                request.getRequestDispatcher("/WEB-INF/mairie.jsp").forward(request, response);
            }
            else if(employeDAO.verifyLogin(request.getParameter("login"))){
                request.setAttribute("loginUsed", "1");
                request.getRequestDispatcher("/WEB-INF/mairie.jsp").forward(request, response);
            }
            employeDAO.creation(request.getParameter("login"), request.getParameter("password1") );
            actualiserPage(request, response, regimeDAO, activiteDAO, accompagnateurDAO, periodeDAO);
            
        }


    }
    
    public void actualiserPage(HttpServletRequest request,
            HttpServletResponse response, RegimeDAO regimeDAO, ActiviteDAO activiteDAO, 
            AccompagnateurDAO accompagnateurDAO, PeriodeDAO periodeDAO)
            throws IOException, ServletException {
        
        List<String> regimes = regimeDAO.getListeRegime();
        List<Activite> activites = activiteDAO.getListeActivite();
        List<String> accompagnateurs = accompagnateurDAO.getListEmail();
        List<Periode> periodes = periodeDAO.getPeriodes();
        request.setAttribute("regimes", regimes);
        request.setAttribute("activites", activites);
        request.setAttribute("accompagnateurs", accompagnateurs);     
        request.setAttribute("periodes", periodes);
        request.getRequestDispatcher("/WEB-INF/mairie.jsp").forward(request, response);
    }

}

