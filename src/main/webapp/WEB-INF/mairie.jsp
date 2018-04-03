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
                          <form method="get" action="controleurMairie" accept-charset="UTF-8">
                            Regime à ajouter : <input type="text" name="regime"/>
                            <input type="submit" name="Ajouter" />
                            <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                            <input type="hidden" name="action" value="regimeAjouter" />
                          </form>
                    </td>
                </tr>
            </tbody>
        </table>

    </body>
</html>
