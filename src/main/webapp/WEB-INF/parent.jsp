<%-- 
    Document   : parent
    Created on : Apr 5, 2018, 10:35:39 AM
    Author     : bernnico
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Parent</title>
    </head>
    <body>
        <h1>Vous êtes bien connectés</h1>
        <table border="1">
            <tbody>
                <tr>
                    <td>
                        <!-- Afficher les coordonnées parents avec un bouton 
                        en dessous pour modifier la fiche-->
                        Fiche de ${parent.getPrenom()}
                        ${parent.getNom()}
                    </td>                    
                    <td>
                        <!-- Afficher la liste des enfants avec chaque nom 
                        cliquable et qui renvoit vers la fiche d'un enfant
                        + possibilité d'ajouter ou supprimer ou modifier un enfant-->
                        <table>
                            <c:forEach items="${parent.getEnfants()}" var="enfant">
                                <tr>
                                    <td>${enfant.getPrenom()}</td>
                                    <td><a href="controleurParent?action=enfantModifier&enfant=${enfant.getPrenom()}">modifier</a></td>
                                    <td><a href="controleurParent?action=enfantSupprimer&enfant=${enfant.getPrenom()}">supprimer</a></td>
                                </tr>
                            </c:forEach>                   
                        </table>
                    </td>
                    <td>
                        <!-- Afficher l'historique des factures + bouton
                        cliquable pour éditer une certaine facture-->
                    </td>    
                </tr>
            </tbody>
        </table>
    </body>
</html>
