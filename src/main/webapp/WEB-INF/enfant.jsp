<%-- 
    Document   : enfant
    Created on : Apr 10, 2018, 7:39:06 PM
    Author     : boussanl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Enfant</title>
    </head>
    <body>
        <h1>Informations de l'enfant</h1>
        <ul>
            <li>Nom : ${ficheEnfant.getNom()}</li>
            <li>Prenom : ${ficheEnfant.getPrenom()}</li>
            <li>Sexe : ${ficheEnfant.getSexe()}</li>
            <li>Classe : ${ficheEnfant.getClasse()}</li>
            <li>Regime : ${ficheEnfant.getRegime()}</li>
            <li>Cantine : ${ficheEnfant.getCantine().toString()}</li>
            <li>Date de Naissance : ${ficheEnfant.getDateNaissance()}</li>
            <li>Divers : ${ficheEnfant.getDivers()}</li>
        </ul>
        
        <div id="enfant" class="row theme_classique">
            <table>
                <c:forEach items="${activites}" var="activite">
                    <tr>
                        <td>${activite}</td>
                        <td><a href="controleurParent?action=activiteSupprimer&prenomEnfant=${ficheEnfant.getPrenom().trim()}&loginParent=${loginParent.trim()}&nomActivite=${activite.getNom()}&creneauxJour=${activite.getCreneauxJour()}&creneauxHeure=${activite.getCreneauxHeure()}"><input type="button"value="supprimer"></a></td>
                    </tr>
                </c:forEach>                   
            </table>
        </div>
        <form method="get" action="controleurParent">
            <input type="submit" value="Ajouter activité">
            <input type="hidden" name="action" value="ajoutActivite" />
            <input type="hidden" name="nom" value="${ficheEnfant.getNom()}" />
            <input type="hidden" name="prenom" value="${ficheEnfant.getPrenom()}" />
            <input type="hidden" name="loginParent" value="${loginParent}" />
        </form>
        <div class="container">    
            <div class="row">
                <form>
                    <a  href="controleurParent?action=retourParent&loginParent=${loginParent}"><input id="retour" type="button" value="retour"></a>
                </form>
            </div>    
        </div>
    </body>
</html>