<%-- 
    Document   : vueFacture
    Created on : 19 avr. 2018, 16:49:52
    Author     : LucBR
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="style.css" />
        <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Facture</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Page Facture</title>
    </head>
    <body>
        <h1>Facture</h1>
        <h2> Période du ${facture.getPeriode().debutToString()} --> ${facture.getPeriode().finToString()}</h2>
        <h4> Cette facture concerne les enfants de ${prenomParent} ${nomParent}</h4>
        <br>
        <div class="container">    
            <div class="row theme_classique">
                <table class="table table-bordered table-hover">
                    <tbody>    
                        <tr>
                           <th>Montant Total sur la période</th>
                           <td>${facture.getPrixTotal()} €</td>
                        </tr>
                        <tr>
                           <th>Montant à rembourser car annulation 48h en avance</th>
                           <td>${facture.getMontantEnleve()} €</td>
                        </tr>
                        <tr>
                          <th>Montant Final à Payer</th>
                          <td>${facture.getMontantFinal()} €</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="row theme_classique">
                Pour imprimer appuyer sur ctrl + p ou sur votre navigateur : options -> imprimer
            </div>
        </div>
    </body>
</html>
