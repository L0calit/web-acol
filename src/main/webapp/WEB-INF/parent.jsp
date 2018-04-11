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
        <link rel="stylesheet" type="text/css" href="style.css" />	
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Parent</title>
    </head>
    <body>
        <h1>Vous êtes bien connectés sur la fiche de ${parent.getPrenom()} ${parent.getNom()}</h1>
        <div class="container">    
            <div class="row">
                <form>
                   <a  href="controleurParent?action=logout"><input id="logout" type="button" value="LogOut"></a>
                </form>
            </div>    
        </div>
        <div id="container_parent" class="container">
           <div id="parent" class="row theme_classique">
                  <c:if test="${modifOK == 1}">
                      <div style="color:green;">
                          Changement pris en compte
                      </div>
                  </c:if>
               Modifier les informations parents<br/>
                  <form method="post" action="controleurParent" accept-charset="UTF-8">
                    <input type="submit" value="Modifier" />
                    <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                    <input type="hidden" name="action" value="modifParent" />
                    <input type="hidden" name="currentLogin" value="${parent.getLogin()}" />
                  </form>
           </div>
            <div id="enfant" class="row theme_classique">
                <!-- Afficher la liste des enfants avec chaque nom 
                        cliquable et qui renvoit vers la fiche d'un enfant
                        + possibilité d'ajouter ou supprimer ou modifier un enfant-->
                        <table>
                            <c:forEach items="${parent.getEnfants()}" var="enfant">
                                <tr>
                                    <td><a href="controleurParent?action=enfantInfo&enfant=${enfant.getPrenom()}&loginParent=${parent.getLogin()}"><input type="button" value="${enfant.getPrenom()}"></a></td>
                                    <td><a href="controleurParent?action=enfantSupprimer&enfant=${enfant.getPrenom()}&loginParent=${parent.getLogin()}"><input type="button"value="supprimer"</a></td>
                                </tr>
                            </c:forEach>                   
                        </table>
            </div>
            <div id="ajout_enfant" class ="row theme_classique">
                    <a href="controleurParent?action=ajoutEnfant&loginParent=${parent.getLogin()}"><input type="button" value="Ajouter un enfant"></a>
            </div>      
            <div id="facture" class="row theme_classique">    
                        Historique des factures
                        <!-- Afficher l'historique des factures + bouton
                        cliquable pour éditer une certaine facture-->
            </div>
        </div>
    </body>

</html>
