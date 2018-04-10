package controleur;

import dao.DAOException;
import dao.ParentDAO;
import dao.RegimeDAO;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import modele.Cantine;
import modele.FicheEnfant;
import modele.FicheParent;

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
     * Actions possibles en POST : connexion
     */
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        ParentDAO parentDAO = new ParentDAO(ds);
        if (action == null) {
            invalidParameters(request, response);
            return;
        } else if (action.equals("connexion")) {
            // tester si mdp et login corrects
            String login = request.getParameter("login");
            String mdp = request.getParameter("password");
            if (parentDAO.verify(login, mdp)) {
                FicheParent ficheParent = parentDAO.getFicheParent(login);
                request.setAttribute("parent", ficheParent);
                request.getRequestDispatcher("/WEB-INF/parent.jsp").forward(request, response);
            } else {
                request.setAttribute("erreurLoginParent", "1");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } else if (action.equals("modifParent")) {
            FicheParent ancienneFicheParent = parentDAO.getFicheParent(request.getParameter("currentLogin"));
            request.setAttribute("ficheParent", ancienneFicheParent);
            request.getRequestDispatcher("/WEB-INF/modifParent.jsp").forward(request, response);
        }else if (action.equals("creationCompteParent")) {
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
            
        }else if (action.equals("creationFiche")) {
            String adresse = request.getParameter("adresse");
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String login = request.getParameter("login");
            String password = request.getParameter("motdepasse");
            parentDAO.creation(login, password, nom, prenom, adresse);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }else if (action.equals("modifInfo")) {
            String newAdresse = request.getParameter("adresse");
            String newNom = request.getParameter("nom");
            String newPrenom = request.getParameter("prenom");
            String loginParent = request.getParameter("login");
            parentDAO.modifierInfo(newAdresse, newNom, newPrenom, loginParent);
            FicheParent ficheParent = parentDAO.getFicheParent(loginParent);
            request.setAttribute("parent", ficheParent);
            request.setAttribute("modifOK", 1);
            request.getRequestDispatcher("/WEB-INF/parent.jsp").forward(request, response);
        }

    }
    
    /**
     * Actions possibles en GET : ajoutEnfant
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
            /*if (request.getParameter("Lu").equals(true)) {
                cantine += "lu/";
            }
            if (request.getParameter("Ma").equals(true)) {
                cantine += "ma/";
            }
            if (request.getParameter("Me").equals(true)) {
                cantine += "me/";
            }
            if (request.getParameter("Je").equals(true)) {
                cantine += "je/";
            }
            if (request.getParameter("Ve").equals(true)) {
                cantine += "ve/";
            }*/
            cantine = "lu/ma";
            String divers = request.getParameter("divers");
            Cantine cantineElement = new Cantine(cantine);
            FicheParent parents = parentDAO.getFicheParent(loginParent);
            FicheEnfant ficheEnfant = new FicheEnfant(sexe, classe, dateNaissance,
                    divers, regime, parents, cantineElement, null, nomEnfant, prenomEnfant);
            parentDAO.ajoutEnfant(ficheEnfant, loginParent);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
