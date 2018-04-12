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
        <link rel="stylesheet" type="text/css" href="style.css" />
        <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">

        <title>Enfant</title>
    </head>
    <body>
        <div id="container_enfant" class="container">
           <div id="enfant" class="row theme_classique">
               <div class="col">
                   <div class="row">
                       <h1>Informations de l'enfant - ${ficheEnfant.getPrenom()} ${ficheEnfant.getNom()}</h1>             
                   </div>
                   <div class="row">
                       <ul>
                            <li><b>Nom : </b>${ficheEnfant.getNom()}</li>
                            <li><b>Prenom : </b>${ficheEnfant.getPrenom()}</li>
                            <li><b>Sexe : </b>${ficheEnfant.getSexe()}</li>
                            <li><b>Classe : </b>${ficheEnfant.getClasse()}</li>
                            <li><b>Regime : </b>${ficheEnfant.getRegime()}</li>
                            <li><b>Cantine : </b>${ficheEnfant.getCantine().toString()}</li>
                            <li><b>Date de Naissance : </b>${ficheEnfant.getDateNaissance()}</li>
                            <li><b>Divers : </b>${ficheEnfant.getDivers()}</li>
                       </ul>
                   </div>
                   <div class="row">
                        <table class="table table-bordered table-hover">
                             <thead class="thead-light">
                                <tr>
                                  <th scope="col">Activité demandée pour la prochaine période</th>
                                </tr>
                             </thead>
                            <c:forEach items="${activites}" var="activite">
                                <tr>
                                    <td>${activite}</td>
                                    <td><a href="controleurParent?action=activiteSupprimer&prenomEnfant=${ficheEnfant.getPrenom().trim()}&loginParent=${loginParent.trim()}&nomActivite=${activite.getNom()}&creneauxJour=${activite.getCreneauxJour()}&creneauxHeure=${activite.getCreneauxHeure()}"><input type="button"value="supprimer"></a></td>
                                </tr>
                            </c:forEach>                   
                        </table>
                    </div>
                    <div class="row">
                        <table class="table table-bordered table-hover">
                             <thead class="thead-light">
                                <tr>
                                  <th scope="col">Activité reservée pour la période en cours</th>
                                </tr>
                             </thead>
                            <c:forEach items="${activites}" var="activite">
                                <tr>
                                    <td>${activite}</td>
                                    <td><a href="controleurParent?action=activiteSupprimer&prenomEnfant=${ficheEnfant.getPrenom().trim()}&loginParent=${loginParent.trim()}&nomActivite=${activite.getNom()}&creneauxJour=${activite.getCreneauxJour()}&creneauxHeure=${activite.getCreneauxHeure()}"><input type="button"value="Ne vient pas cette semaine"></a></td>
                                </tr>
                            </c:forEach>                   
                        </table>
                    </div>
                    <div class="row">
                        <form method="get" action="controleurParent">
                            <div class="form-group">
                                <input type="submit" value="Ajouter activité" <c:if test="${estEnCours == true}">disabled</c:if> />
                            </div>
                            <input type="hidden" name="action" value="ajoutActivite" />
                            <input type="hidden" name="nom" value="${ficheEnfant.getNom()}" />
                            <input type="hidden" name="prenom" value="${ficheEnfant.getPrenom()}" />
                            <input type="hidden" name="loginParent" value="${loginParent}" />
                        </form>
                    </div>
               </div>
           </div>
        </div>
        <div class="container">    
            <div class="row">
                <form>
                    <a  href="controleurParent?action=retourParent&loginParent=${loginParent}"><input id="retour" type="button" value="retour"></a>
                </form>
            </div>    
        </div>
    </body>
</html>