package controleur;

import dao.DAOException;
import dao.ParentDAO;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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
        }


    }
}
