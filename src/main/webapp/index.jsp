<%-- 
    Document   : index
    Created on : Apr 10, 2018, 12:00:16 PM
    Author     : boussanl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8" />
  <link rel="stylesheet" type="text/css" href="style.css" />	
  <title>Connexion</title>
</head>
<body>
  <h1>Connexion</h1>
  <table border="0">
      <tbody>
          <tr>
              <td> 
                  Je suis un employé de la mairie : <br/>
                  <c:if test="${erreurLoginMairie == 1}">
                      <div style="color:red;">
                          Mauvais login/mot de passe
                      </div>
                  </c:if>
                  <form method="post" action="controleurMairie" accept-charset="UTF-8">
                    <ul>
                      <li> Login : <input type="text" name="login"/></li>
                      <li> Mot de passe : <input type="password" name="password"/></li>
                    </ul>
                    <input type="submit" value="Login" />
                    <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                    <input type="hidden" name="action" value="connexion" />
                  </form>
              </td>
              <td>
                  Je suis un parent d'élève : <br/>
                  <c:if test="${erreurLoginParent == 1}">
                      <div style="color:red;">
                          Mauvais login/mot de passe
                      </div>
                  </c:if>
                  <form method="post" action="controleurParent" accept-charset="UTF-8">
                    <ul>
                      <li> Login : <input type="text" name="login"/></li>
                      <li> Mot de passe : <input type="password" name="password"/></li>
                    </ul>
                    <input type="submit" value="Login" />
                    <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                    <input type="hidden" name="action" value="connexion" />
                  </form>
              </td>
              <td>
                  Je n'ai pas de compte Parent - Je veux en créer un : <br/>
                  <form method="post" action="controleurParent" accept-charset="UTF-8">
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
                    <input type="hidden" name="action" value="creationCompteParent" />
                  </form>
              </td>
          </tr>
      </tbody>
  </table>
</body>
</html>
