<%-- 
    Document   : ajoutParent
    Created on : Apr 10, 2018, 4:22:27 PM
    Author     : bernnico
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Création de Fiche Parent</title>
    </head>
    <body>
        <h1>Création de Fiche Parent</h1>
        <table>
              <tbody>
          <tr>
              <td> 
                  Informations modifiables
                  <form method="post" action="controleurParent" accept-charset="UTF-8">
                    <ul>
                      <li> Entrez un Nouveau Prénom: <input type="text" name="prenom" required/></li>
                      <li> Entrez un Nouveau Nom <input type="text" name="nom" required/></li>
                      <li> Entrez une Nouvelle Adresse: <input type="text" name="adresse" required/></li>
                    </ul>
                    <input type="hidden" name="login" value="${login}"/></li>
                    <input type="hidden" name="motdepasse" value="${password}"/>
                    <input type="submit" value="Valider les informations"/>
                    <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                    <input type="hidden" name="action" value="creationFiche" />
                  </form>
              </td>
          </tr>
      </tbody>
  </table>
    </body>
</html>
