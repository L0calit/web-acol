<%-- 
    Document   : ajoutActivite
    Created on : Apr 11, 2018, 2:26:44 PM
    Author     : boussanl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                  Informations modifiables
                  <form method="post" action="controleurParent" accept-charset="UTF-8">

                    <input type="submit" value="Ajouter activité"/>
                    <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                    <input type="hidden" name="action" value="activiterAjouter" />
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
