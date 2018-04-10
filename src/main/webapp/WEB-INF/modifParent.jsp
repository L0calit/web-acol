<%-- 
    Document   : modifParent
    Created on : Apr 10, 2018, 12:15:58 PM
    Author     : bernnico
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modification Fiche Parent</title>
    </head>
    <body>
        <h1>Modifier les informations des parents</h1>
      <table border="0">
      <tbody>
          <tr>
              <td> 
                  Informations modifiables
                  <form method="post" action="controleurMairie" accept-charset="UTF-8">
                    <ul>
                      <li> Entrez un Nouveau Nom <input type="text" name="nom" value="${ficheParent.getNom()}"/></li>
                      <li> Entrez un Nouveau Prénom: <input type="text" name="prenom" value="${ficheParent.getPrenom()}"/></li>
                      <li> Entrez un Nouveau Login: <input type="text" name="login" value="${ficheParent.getLogin()}"/></li>
                      <li> Entrez un Nouveau Mot de passe: <input type="password" name="motdepasse"/></li>
                      <li> Entrez une Nouvelle Adresse: <input type="text" name="adresse" value="${ficheParent.getAdresse()}"/></li>
                    </ul>
                    <input type="submit" value="Login" />
                    <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                    <input type="hidden" name="action" value="modifInfo" />
                  </form>
              </td>
          </tr>
      </tbody>
  </table>
    </body>
</html>
