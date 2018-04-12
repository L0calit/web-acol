<%-- 
    Document   : ajoutActivite
    Created on : Apr 11, 2018, 2:26:44 PM
    Author     : boussanl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ajout d'une activité</title>
    </head>
    <body>
        <h1>Ajout d'une activité</h1>
        <table>
              <tbody>
          <tr>
              <td> 
                  <form method="get" action="controleurParent" accept-charset="UTF-8">
                    <select name="activiteChoisi" required>
                        <c:choose>
                            <c:when test="${fn:length(activites) > 0}">
                                <c:forEach items="${activites}" var="activite">
                                    <option value="${activite}">${activite}</option>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <option value="Impossible">Aucune activité ne peut être choisi</option>
                            </c:otherwise>
                        </c:choose>
                            
                    </select>
                    <c:choose>
                        <c:when test="${fn:length(activites) > 0}">
                            <input type="submit" value="Ajouter activité"/>
                        </c:when>
                        <c:otherwise>
                            <input type="submit" value="Ajouter activité" disabled/>
                        </c:otherwise>
                    </c:choose>
                    <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                    <input type="hidden" name="action" value="activiteAjouter" />
                    <input type="hidden" name="prenomEnfant" value="${prenomEnfant}" />
                    <input type="hidden" name="loginParent" value="${loginParent}" />
                  </form>
              </td>
          </tr>
      </tbody>
  </table>
        <form>
            <input type="button" value="Annuler" onclick="history.go(-1)">
        </form>
    </body>
</html>
