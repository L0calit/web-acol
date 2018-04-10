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
                <th>Mail de l'accompagnateur 1</th>
                <th>Mail de l'accompagnateur 2</th>
            </tr>
            <c:forEach items="${activites}" var="activite">
                <tr>
                    <td>${activite.getNom()}</td>
                    <td>${activite.getCreneaux()}</td>
                    <td>${activite.getClasse()}</td>
                    <td>${activite.getPrix()}</td>
                    <td>${activite.getEffectif()}</td>
                    <td>${activite.getAccompagnateur1()}</td>
                    <td>${activite.getAccompagnateur2()}</td>
                    <td><a href="controleurMairie?action=activiteSupprimer&actiNom=${activite.getNom()}&actiCreneaux=${activite.getCreneaux()}">supprimer</a></td>
                </tr>
            </c:forEach>
        </table>
        <br/>
        <h3>Ajouter une activité</h3>
        <form method="get" action="controleurMairie" accept-charset="UTF-8">
            Nom de l'activité : <input type="text" name="nom"/>
            <br/>
            Creneaux de l'activite : <input type='text' name="creneaux" />
            <br/>
            Classe(s) concernées : <input type='text' name="classes" />
            <br/>
            Prix de l'activité : <input type='text' name="prix" />
            <br/>
            Effectif de l'activité : <input type='text' name="effectif" />
            <br/>
            Mail de l'accompagnateur1 : <input type='text' name="mail1" />
            <br/>
            Mail de l'accompagnateur2: <input type='text' name="mail2" />
            <br/>
            <input type="submit" value="Ajouter" />
            <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
            <input type="hidden" name="action" value="activiteAjouter" />
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
        <h3>Ajouter un régime</h3>
          <form method="get" action="controleurMairie" accept-charset="UTF-8">
            Regime à ajouter : <input type="text" name="regime"/>
            <input type="submit" value="Ajouter" />
            <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
            <input type="hidden" name="action" value="regimeAjouter" />
          </form>
        <h3> Rajouter un employé de la mairie: </h3> <br/>
                  <form method="post" action="controleurMairie" accept-charset="UTF-8">
                    <ul>
                      <li> Créez un Login : <input type="text" name="login"/></li>
                      <li> Créez un Mot de passe : <input type="password" name="password1"/></li>
                      <li> Confirmez le Mot de passe : <input type="password" name="password2"/></li>
                    </ul>
                   <c:if test="${differentPassword == 1}">
                      <div style="color:red;">
                          Saisissez le même mot de passe
                      </div>
                   </c:if>
                   <c:if test="${loginUsed == 1}">
                      <div style="color:red;">
                          Ce login est déjà pris merci d'en choisir un autre !
                      </div>
                   </c:if>
                    <input type="submit" value="Création" />
                    <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                    <input type="hidden" name="action" value="creationCompteMairie" />
                  </form>
    </body>
</html>
