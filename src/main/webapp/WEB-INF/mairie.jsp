<%-- 
    Document   : mairie
    Created on : Apr 3, 2018, 5:07:03 PM
    Author     : boussanl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mairie</title>
    </head>
    <body>
        <h1>Vous êtes bien connectés</h1>
        <table border="1">
            <tbody>
                <tr>
                    <td>
                        <!-- Afficher le catalogue des activités -->
                    </td>
                    <td>
                        <!-- Afficher les regimes -->
                        <table>
                            <c:forEach items="${regimes}" var="regime">
                                <tr>
                                    <td>${regime}</td>
                                    <td><a href="controleurMairie?action=regimeSupprimer&regime=${regime}">supprimer</a></td>
                                </tr>
                            </c:forEach>                            
                        </table>
                        <a href="controleurMairie?action=regime&view=ajouter">Ajouter</a>
                    </td>
                </tr>
            </tbody>
        </table>

    </body>
</html>
