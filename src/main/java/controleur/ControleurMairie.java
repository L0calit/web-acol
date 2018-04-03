/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import dao.DAOException;
import dao.RegimeDAO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

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
     * Actions possibles en GET : 
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        RegimeDAO regimeDAO = new RegimeDAO(ds);
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action.equals("regimeSupprimer")) {
            regimeDAO.supprimerRegime(request.getParameter("regime"));
            List<String> regimes = regimeDAO.getListeRegime();
            request.setAttribute("regimes", regimes);
            request.getRequestDispatcher("/WEB-INF/mairie.jsp").forward(request, response);
        } else if (action.equals("regime")) {
            regimeDAO.ajouterRegime("Sans porc");
            List<String> regimes = regimeDAO.getListeRegime();
            request.setAttribute("regimes", regimes);
            request.getRequestDispatcher("/WEB-INF/mairie.jsp").forward(request, response);
        }
    }
    
    /**
     * Actions possibles en POST : connexion
     */
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        RegimeDAO regimeDAO = new RegimeDAO(ds);
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            invalidParameters(request, response);
            return;
        } else if (action.equals("connexion")) {
            // tester si mdp et login corrects
            List<String> regimes = regimeDAO.getListeRegime();
            request.setAttribute("regimes", regimes);
            request.getRequestDispatcher("/WEB-INF/mairie.jsp").forward(request, response);
        }


    }

}

