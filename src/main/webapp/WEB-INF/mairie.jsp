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
        <!-- Afficher le catalogue des activités -->
        <h2>Activités</h2>
        <table border="1">
            <tr>
                <th>Nom</th>
                <th>Créneaux</th>
                <th>Classe</th>
                <th>Prix</th>
                <th>Effectif</th>
            </tr>
            <c:forEach items="${activites}" var="activite">
                <tr>
                    <td>${activite.nom}</td>
                    <td>${activite.creneaux}</td>
                    <td>${activite.classe}</td>
                    <td>${activite.prix}</td>
                    <td>${activite.effectif}</td>
                    <td><a href="controleurMairie?action=activiteSupprimer&activite=${activite}">supprimer</a></td>
                </tr>
            </c:forEach>
        </table>
        <br/>
        <form method="get" action="controleurMairie" accept-charset="UTF-8">
            Nom de l'activité : <input type="text" name="nom"/>
            <input type="submit" value="Ajouter" />
            <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
            <input type="hidden" name="action" value="regimeAjouter" />
        </form>
        <!-- Afficher les regimes -->
        <h2>Regime</h2>
        <table border="1">
            <c:forEach items="${regimes}" var="regime">
                <tr>
                    <td>${regime}</td>
                    <td><a href="controleurMairie?action=regimeSupprimer&regime=${regime}">supprimer</a></td>
                </tr>
            </c:forEach>                            
        </table>
        <br/>
          <form method="get" action="controleurMairie" accept-charset="UTF-8">
            Regime à ajouter : <input type="text" name="regime"/>
            <input type="submit" value="Ajouter" />
            <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
            <input type="hidden" name="action" value="regimeAjouter" />
          </form>

    </body>
</html>
