<%-- 
    Document   : ajoutEnfant
    Created on : Apr 10, 2018, 3:22:41 PM
    Author     : boussanl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ajout Fiche Enfant</title>
    </head>
    <body>
        <h1>Ajouter les informations de l'enfant</h1>
      <table border="0">
      <tbody>
          <tr>
              <td> 
                  Informations 
                  <form method="get" action="controleurParent?action=enfantAjouter&loginParent=${loginParent}" accept-charset="UTF-8">
                    <ul>
                      <li> Entrez un Nom: <input type="text" name="nom"/></li>
                      <li> Entrez un Prénom: <input type="text" name="prenom"/></li>
                      <li> Entrez le sexe de l'enfant:
                        <select name="sexe">
                            <option value="M" selected>Masculin</option>
                            <option value="F">Féminin</option>
                            <option value="A">Autres</option>
                        </select>
                      </li>
                      <li> Entrez une date de Naissance (format JJ/MM/AAAA) <input type="date" name="dateNaissance"/></li>
                    </ul>
                    <input type="submit" value="Login" />
                  </form>
              </td>
          </tr>
      </tbody>
  </table>
    </body>
</html>
